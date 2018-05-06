/****************************************************************************
* Copyright (c) 2012, Microwise System Co., Ltd.
* All rights reserved.
*
* 文 件 名:  main.c
* 描    述:  analysis log
*
* 当前版本:  V1.0
* 作    者:  wang.mg
* 完成日期:  2012-10-15
* 编译环境:  x86+linux
*****************************************************************************
*/
#include   <stdlib.h>
#include   <stdio.h>
#include   <unistd.h>
#include   <string.h>
#include   <fcntl.h>
#include   <pthread.h>
#include   <signal.h>

#define  FALSE 0
#define  TRUE  1
#define  VERSION  "1.0.0"

/********************************************************************
* 函 数 名:  show_version
* 描    述:  显示本进程版本号
* 输入参数:  argc -- 参数个数
*            argv -- 具体参数
* 输出参数:  无
* 返 回 值:  无
********************************************************************/
static void show_version(int argc, char** argv)
{
    if(argc >= 2)
    {
        if(strcmp(argv[1],"-v")==0)
         {
            printf("analysis version = %s\n", VERSION);
            exit(0);
         }
    }
}

/******************************************************************************
* 函 数 名: showRebootingTime
* 描    述: 输出设备重启的时间点
* 输入参数: packet -- 当前数据包
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
static void showRebootingTime(unsigned char * packet)
{
    printf("[Warning]: rebooting after the packet = %s", packet);
}

/******************************************************************************
* 函 数 名: showLosePacketTime
* 描    述: 输出丢包的时间点
* 输入参数: packet -- 当前数据包
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
static void showLosePacketTime(unsigned char * packet, unsigned char difference)
{
    printf("[Warning]: Lost = [%03d], before the packet = %s", difference, packet);
}

/******************************************************************************
* 函 数 名: getTimestamp
* 描    述: 获取时间戳
* 输入参数: packet -- 数据包
* 输出参数: stamp  -- 时间戳
* 返 回 值: 包序列号
******************************************************************************/
static void getTimestamp(unsigned char * packet, unsigned char *stamp)
{
    memcpy(stamp, &packet[0], 12);
}

/******************************************************************************
* 函 数 名: getSequence
* 描    述: 获取包序列号
* 输入参数: packet -- 数据包
* 输出参数: 无
* 返 回 值: 包序列号
******************************************************************************/
static unsigned int getSequence(unsigned char * packet)
{
    unsigned int seq = 0;

    sscanf(&packet[18], "%2x", &seq);

    return seq;
}

/******************************************************************************
* 函 数 名: getSearchNetCnt
* 描    述: 获取搜网次数
* 输入参数: packet -- 数据包
* 输出参数: 无
* 返 回 值: 包序列号
******************************************************************************/
static unsigned int getSearchNetCnt(unsigned char * packet)
{
    unsigned int cnt = 0;

    sscanf(&packet[21], "%4x", &cnt);
    
    return cnt;
}

/******************************************************************************
* 函 数 名: hasReboot
* 描    述: 本包之前是否发生过重启
* 输入参数: packet  -- 当前包
* 输出参数: 无
* 返 回 值: 0 -- 无 / 1 -- 重启
******************************************************************************/
static unsigned int hasReboot(unsigned char *packet)
{
    unsigned char thisSeq = 0;
    unsigned int thisSearchTime=0;
    static unsigned char lastSeq = 0;
    static unsigned int lastSearchTime = 0;
    
    thisSeq = getSequence(packet);
    thisSearchTime = getSearchNetCnt(packet);
    
    if(thisSeq == 0)
    {
        lastSeq = thisSeq;
        lastSearchTime = thisSearchTime;
        return 1;
    }
    
    if(thisSeq == 1)
    {
        if(lastSeq == 0xff)
        {
            lastSeq = thisSeq;
            lastSearchTime = thisSearchTime;
            return 0;
        }
    }
    
    if((thisSeq < lastSeq)&&(thisSearchTime< lastSearchTime))
    {
        {
            lastSeq = thisSeq;
            lastSearchTime = thisSearchTime;
            return 1;
        }
    }
    
    lastSeq = thisSeq;
    lastSearchTime = thisSearchTime;
    return 0;
}

/******************************************************************************
* 函 数 名: getRebootCount
* 描    述: 获取重启次数
* 输入参数: packet  -- 当前包
* 输出参数: 无
* 返 回 值: 重启次数
******************************************************************************/
static unsigned int getRebootCount(unsigned char *packet)
{
    static unsigned int  rebootCnt = 0;
    static unsigned char lastLine[200] = {"Line 1 -- RESET\r\n"}; // 缓存上一行数据
    
    if(hasReboot(packet))
    {
        rebootCnt++;
        showRebootingTime(lastLine);
    }
    
    strcpy(lastLine, packet);
    return rebootCnt;
}

/******************************************************************************
* 函 数 名: getLosePacketInfo
* 描    述: 获取丢包事件
* 输入参数: packet  -- 当前包
*           gotCnt  -- 获取包数
*           lostCnt -- 丢包数
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
static unsigned int getLosePacketInfo(unsigned char *packet, unsigned int *gotCnt, unsigned int *lostCnt)
{
    unsigned char difference   = 0;
    unsigned char thisSequence = 0;
    static unsigned char lastSequence = 0;
    
    (*gotCnt)++;
    
    thisSequence = getSequence(packet);
    
    if(1 == *gotCnt) //first line
    {
        lastSequence = thisSequence;
        return 0;
    }

    if(hasReboot(packet)) //reboot
    {
        lastSequence = thisSequence;
        return 0;
    }
    
    if((thisSequence == 1) && (lastSequence == 0xFF)) //序列号轮回
    {
        lastSequence = thisSequence;
        return 0;
    }
    
    if(thisSequence == (lastSequence+1)) //序列号连续
    {
        lastSequence++;
        return 0;
    }
    
    //发生丢包
    if(thisSequence > lastSequence)
    {
        difference = thisSequence - lastSequence - 1;
    }
    else if(thisSequence < lastSequence)
    {
        difference = thisSequence - lastSequence - 2; // FE FF 01 except 00
    }
    else
    {
        return 0; //回补数据包
    }
    
    *lostCnt += difference;
    
    lastSequence = thisSequence;
    
    showLosePacketTime(packet, difference);
    
    return 0;
}

/******************************************************************************
* 函 数 名: analysisRebootEvent
* 描    述: 分析重启事件
* 输入参数: fileName -- 文件名
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
int analysisRebootEvent(char *fileName, char *begTime, char *endTime)
{
    FILE * fp;
    
    unsigned char timeBuf[12];
    unsigned char lineBuf[254];
    unsigned int  rebootCnt = 0;

    if ((fp = fopen(fileName,"r")) == NULL)
    {
        return FALSE;
    }
    
    /*分析重启次数*/
    printf("==================== Reboot BEG ==================== \n");
    while(fgets(lineBuf, sizeof(lineBuf), fp))
    {
        getTimestamp(lineBuf, timeBuf);
        if(memcmp(timeBuf, begTime, sizeof(timeBuf)) < 0) // 采样时间早于用户指定的开始时间
        {
            continue;
        }
        
        if(memcmp(timeBuf, endTime, sizeof(timeBuf)) > 0) // 采样时间晚于用户指定的结束时间
        {
            break;
        }
        
        //reboot info: time and count
        rebootCnt = getRebootCount(lineBuf);
    }
    
    printf("[NOTE]: reboot count = %d\n", rebootCnt);
    printf("==================== Reboot END ==================== \n\n");
    
    fclose(fp);
}

/******************************************************************************
* 函 数 名: analysisLosePacketEvent
* 描    述: 分析丢包事件
* 输入参数: fileName -- 文件名
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
int analysisLosePacketEvent(char *fileName, char *begTime, char *endTime)
{
    FILE * fp;
    
    unsigned char timeBuf[12]={0};
    unsigned char lineBuf[254];
    
    float ratio = 0.0;
    unsigned int totalPacketCnt = 0;
    unsigned int losingPacketCnt = 0;

    if ((fp = fopen(fileName,"r")) == NULL)
    {
        return FALSE;
    }
    
    /*分析丢包率*/
    printf("==================== Lost   BEG ==================== \n");
    while(fgets(lineBuf, sizeof(lineBuf), fp)) //与重启分开处理以优化分析报告格式
    {
        memset(timeBuf, 0, sizeof(timeBuf));
        
        getTimestamp(lineBuf, timeBuf);
        if(memcmp(timeBuf, begTime, sizeof(timeBuf)) < 0) // 采样时间早于用户指定的开始时间
        {
            continue;
        }
        
        if(memcmp(timeBuf, endTime, sizeof(timeBuf)) > 0) // 采样时间晚于用户指定的结束时间
        {
            break;
        }
        
        //lose packet: time and ratio
        getLosePacketInfo(lineBuf, &totalPacketCnt, &losingPacketCnt);
    }
    
    totalPacketCnt += losingPacketCnt;
    ratio = (float)losingPacketCnt / totalPacketCnt;
    
    printf("total packet count = %d\n", totalPacketCnt);
    printf("lost  packet count = %d\n", losingPacketCnt);
    printf("lost  packet ratio = %f\n", ratio);
    printf("==================== Lost   END ==================== \n");
    
    fclose(fp);
}

/******************************************************************************
* 函 数 名: main
* 描    述: 分析日志文件
* 输入参数: argv[1] -- 文件名
*           argv[2] -- 日志对应的协议版本
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
int  main(int argc ,char *argv[])
{
    show_version(argc, argv);
    
    if(argc != 4)
    {
        printf("Error: argc=%d\n", argc);
        printf("Usage: analysis fileName begTime endTime\n");
        return 1;
    }
    
    analysisRebootEvent(argv[1], argv[2], argv[3]);
    analysisLosePacketEvent(argv[1], argv[2], argv[3]);
    
    return 0;
}

