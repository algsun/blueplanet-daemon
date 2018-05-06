#!/bin/bash

###########################################################
# Decription:
#     Downloading logs from Internet.
# Author: sunbo
# Date: 2016-07-10
###########################################################
#set -x

VERSION=1.0.1
if [ x$1 = x"-v" ]; then
    echo "download Version: $VERSION"
    exit 0
fi

siteId=$(echo $1 |awk '{printf("%08X\n", $0)}') #接入点号转换(十进制->十六进制)
date_beg=$(echo $3|cut -c1-10) #$3 获取日志起始时间，格式YY-MM-DD
date_end=$(echo $4|cut -c1-10) #$4 获取日志结束时间，格式YY-MM-DD
SourceLogPath=$5 #读取日志存放路径
TargetPath=$6 #读取结果存放目录

DOWNLOADED_LOGS=logs

#1. 创建Log目录,进入logs目录
[ -d $TargetPath ] || mkdir -p $TargetPath
cd $TargetPath
if [ ! -d $DOWNLOADED_LOGS ]; then
    mkdir $DOWNLOADED_LOGS
else
    rm -r $DOWNLOADED_LOGS
    mkdir $DOWNLOADED_LOGS
fi
cd $DOWNLOADED_LOGS

#2. 读取站点下的上行数据包日志至allDevice.trc文件
dateBeg=$(date -d "$date_beg" +%s) #标准时间转换为时间戳
dateEnd=$(date -d "$date_end" +%s) #标准时间转换为时间戳
while [ "$dateBeg" -le "$dateEnd" ]
do
    year=$(date -d @$dateBeg "+%y") #时间戳转换为标准时间
    let year=$year+2000
    mmdd=$(date -d @$dateBeg "+%m-%d") #时间戳转换为标准时间
    
    cat ${SourceLogPath}/packet-${year}-${mmdd}/*.log | grep -a $siteId | grep '\[01\]' >> allDevice.trc #过滤日志

    dateBeg=$(($dateBeg+86400))
    
done
    cat ${SourceLogPath}/packet.log | grep -a $siteId | grep '\[01\]' >> allDevice.trc #此处可能会多读取日志，但不影响最终结果
exit
