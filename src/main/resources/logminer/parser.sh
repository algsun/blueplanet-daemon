#!/bin/bash

###########################################################
# Decription:
#     The script can parser logs.
# Author: quandk
# Date: 2015-06-16
###########################################################
#set -x

Cur_Dir=$(pwd)

VERSION=1.0.0
if [ x$1 = x"-v" ]; then
    echo "parser Version: $VERSION"
    exit 0
fi

DOWNLOADED_LOGS=logs
USER_REPORT=reports

#--------------- Read Configuration File ------------------
node_ids=$(echo "$2" | sed 's/,/ /g') #逗号分隔符替换为空格分隔符
time_beg=$3
time_end=$4
TargetPath=$6 #读取结果存放目录
node_array=($node_ids)

#echo $node_ids $time_beg $time_end $TargetPath

#--------------- Format Time ------------------
yea=$(printf  "%02X\n" $((10#${time_beg:2:2})))
mon=$(printf  "%02X\n" $((10#${time_beg:5:2})))
day=$(printf  "%02X\n" $((10#${time_beg:8:2})))
hor=$(printf  "%02X\n" $((10#${time_beg:11:2})))
min=$(printf  "%02X\n" $((10#${time_beg:14:2})))
begTime=$yea$mon$day$hor$min

yea=$(printf  "%02X\n" $((10#${time_end:2:2})))
mon=$(printf  "%02X\n" $((10#${time_end:5:2})))
day=$(printf  "%02X\n" $((10#${time_end:8:2})))
hor=$(printf  "%02X\n" $((10#${time_end:11:2})))
min=$(printf  "%02X\n" $((10#${time_end:14:2})))
endTime=$yea$mon$day$hor$min

#1. 创建report目录,进入logs目录
cd $TargetPath

if [ ! -d $USER_REPORT ]; then
    mkdir $USER_REPORT
else
    rm -r $USER_REPORT
    mkdir $USER_REPORT
fi

if [ ! -d $DOWNLOADED_LOGS ]; then
    echo  No log can be parsered.
else
    cd $DOWNLOADED_LOGS
fi

for((i=0;i<${#node_array[@]};i++))
do
    #-- Step 2 : get Given Node log --
    temp=${node_array[$i]}:
    grep -a ${temp} allDevice.trc > ${node_array[$i]}.server.trc
    
    #-- Step 3 : sorted by timeStamp --
    awk  '{print $NF}' ${node_array[$i]}.server.trc | sort > ${node_array[$i]}.dataPacket.tmp

    cut -c 43-54 ${node_array[$i]}.dataPacket.tmp > ${node_array[$i]}.timeSortStamp.tmp
    paste -d" " ${node_array[$i]}.timeSortStamp.tmp ${node_array[$i]}.dataPacket.tmp | sort > ${node_array[$i]}.sort.tmp    
    ${Cur_Dir}/resendAnalysis ${node_array[$i]}.sort.tmp $begTime $endTime >> ../$USER_REPORT/${node_array[$i]}.resend
    
    uniq ${node_array[$i]}.dataPacket.tmp > ${node_array[$i]}.packet.tmp
    
    cut -c 43-54 ${node_array[$i]}.packet.tmp > ${node_array[$i]}.timeStamp.tmp
    cut -c 59-62 ${node_array[$i]}.packet.tmp > ${node_array[$i]}.subPkt.tmp
    cut -c 23-24 ${node_array[$i]}.packet.tmp > ${node_array[$i]}.sequence.tmp
    cut -c 67-70 ${node_array[$i]}.packet.tmp > ${node_array[$i]}.searchtime.tmp
    
    paste -d" " ${node_array[$i]}.timeStamp.tmp ${node_array[$i]}.subPkt.tmp ${node_array[$i]}.sequence.tmp ${node_array[$i]}.searchtime.tmp ${node_array[$i]}.packet.tmp | sort | uniq -w 20 | awk ' $1 >= "'$begTime'" && $1 <= "'$endTime'"'> ${node_array[$i]}.log
    

    cut -c 97- ${node_array[$i]}.log | sed 's/A002/ /' | awk '$1 ~/A005/ {pos=index($0,"A005")-1;if((pos%8)==0) print}' > ../$USER_REPORT/${node_array[$i]}.sensorErr
    rm *.tmp
    
    #-- Step 5 : analysis --
    echo "测试距离" = $test_len >> ../$USER_REPORT/${node_array[$i]}.report
    echo "开始时间" = $time_beg >> ../$USER_REPORT/${node_array[$i]}.report
    echo "结束时间" = $time_end >> ../$USER_REPORT/${node_array[$i]}.report
    
    ${Cur_Dir}/analysis ${node_array[$i]}.log  $begTime $endTime >> ../$USER_REPORT/${node_array[$i]}.report
done

exit
