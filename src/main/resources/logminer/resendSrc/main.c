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
* 函 数 名: showResendPacket
* 描    述: 输出相同数据行
* 输入参数: packet -- 当前数据包; cnt -- 相同的包数
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
static void showResendPacket(unsigned char * packet, unsigned char cnt)
{
    printf("[Resend][%d]:packet=%s", cnt, packet);
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
    memcpy(stamp, packet, 12);
}

/******************************************************************************
* 函 数 名: analysisResendEvent
* 描    述: 分析重复发送事件
* 输入参数: fileName -- 文件名
*           version     -- 日志对应的协议版本，用于分析重启次数
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
static int analysisResendEvent(char *fileName,char *begTime, char *endTime)
{
    FILE * fp;
    
    unsigned char lastLine[254] = {0};
    unsigned char thisLine[254] = {0};
    unsigned char sameLineCount = 0;
    unsigned char timeBuf[12]={0};
    
    
    if ((fp = fopen(fileName,"r")) == NULL)
    {
        return -1;
    }
    
    /*分析重复发送事件*/
    printf("==================== Resend BEG ==================== \n");
    while(fgets(thisLine, sizeof(thisLine), fp))
    {
        memset(timeBuf, 0, sizeof(timeBuf));
        
        getTimestamp(thisLine, timeBuf);
        
        if(memcmp(timeBuf, begTime, sizeof(timeBuf)) < 0) // 采样时间早于用户指定的开始时间
        {
            continue;
        }
        
        if(memcmp(timeBuf, endTime, sizeof(timeBuf)) > 0) // 采样时间晚于用户指定的结束时间
        {
            break;
        }
        
        if(0 != memcmp(lastLine, thisLine, sizeof(thisLine))) //本行与上一行不同
        {
            if(0 != sameLineCount)
            {
                showResendPacket(lastLine, sameLineCount);
                sameLineCount = 0;
            }
            
            memcpy(lastLine, thisLine, sizeof(thisLine));
            memset(thisLine, 0, sizeof(thisLine));
        }
        else
        {
            sameLineCount++;
        }
    }
    
    if(0 != sameLineCount) //最后一些重复行
    {
        showResendPacket(lastLine, sameLineCount);
        sameLineCount = 0;
    }
    
    printf("==================== Resend END ==================== \n\n");
    
    fclose(fp);
}

/******************************************************************************
* 函 数 名: main
* 描    述: 分析日志文件，查找重复包
* 输入参数: argv[1] -- 文件名
* 输出参数: 无
* 返 回 值: 无
******************************************************************************/
int  main(int argc ,char *argv[])
{
    show_version(argc, argv);
    
    if(argc != 4)
    {
        printf("Error: argc=%d\n", argc);
        printf("Usage: analysisResend fileName\n");
        return 1;
    }
    
    analysisResendEvent(argv[1],argv[2],argv[3]);
    
    return 0;
}

