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


char a2x(char ch)
{
    switch(ch)
    {
    case '1':
        return 1;
    case '2':
        return 2;
    case '3':
        return 3;
    case '4':
        return 4;
    case '5':
        return 5;
    case '6':
        return 6;
    case '7':
        return 7;
    case '8':
        return 8;
    case '9':
        return 9;
    case 'A':
    case 'a':
        return 10;
    case 'B':
    case 'b':
        return 11;
    case 'C':
    case 'c':
        return 12;
    case 'D':
    case 'd':
        return 13;
    case 'E':
    case 'e':
        return 14;
    case 'F':
    case 'f':
        return 15;
    default:
        break;;
    }
 
    return 0;
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
    FILE * fp;
    unsigned char lineBuf[100];
    unsigned int temp;
    unsigned int sum = 0;
    
    if ((fp = fopen(argv[1],"r")) == NULL)
    {
        printf("error\n");
        return FALSE;
    }

    while(fgets(lineBuf, sizeof(lineBuf), fp))
    {
        temp = 0;
        sscanf(lineBuf,"%x",&temp);
        sum += temp;                
    }
    
    printf("%d\n",sum);
    
    return 0;
}

