#!/bin/bash

###########################################################
# Decription:
#     all reports in one.
# Author: quandk
# Date: 2016-02-16
###########################################################
#set -x

Cur_Dir=$(pwd)

VERSION=1.0.0
if [ x$1 = x"-v" ]; then
    echo "makeReport Version: $VERSION"
    exit 0
fi

DOWNLOADED_LOGS=logs
USER_REPORT=reports

node_ids=$(echo "$2" | sed 's/,/ /g') #逗号分隔符替换为空格分隔符
time_beg=$3
time_end=$4
TargetPath=$6 #读取结果存放目录
node_array=($node_ids)

cd $TargetPath

echo "Collect Time" = $(date) >> $USER_REPORT/all.report
echo "Test Distance" = $test_len >> $USER_REPORT/all.report
echo "Begin Time" = $time_beg >> $USER_REPORT/all.report
echo "End Time" = $time_end >> $USER_REPORT/all.report
echo =============================== Report =============================== >> $USER_REPORT/all.report
echo "ID  Total Loss Ratio  Reboot Search sensorErrorFlg" | awk '{printf "%8s %8s %8s %9s %8s %8s %15s\n",$1,$2,$3,$4,$5,$6,$7}' >> $USER_REPORT/all.report
echo   >> $USER_REPORT/all.report

for((i=0;i<${#node_array[@]};i++))
do
    #-- Step 3 : get Given Node log --
    id=${node_array[$i]}
    total=$(grep -a "total packet count" ${USER_REPORT}/${node_array[$i]}.report | awk  '{print $NF}')
    lost=$(grep  -a "lost  packet count" ${USER_REPORT}/${node_array[$i]}.report | awk  '{print $NF}')
    ratio=$(grep -a "lost  packet ratio" ${USER_REPORT}/${node_array[$i]}.report | awk  '{print $NF}')
    rebootTimes=$(grep -a "reboot count" ${USER_REPORT}/${node_array[$i]}.report | awk  '{print $NF}')
    
    searchTimesBeg=$(echo $((16#$(head -1 ${DOWNLOADED_LOGS}/${node_array[$i]}.log | awk  '{print $4}'))))
    searchTimesEnd=$(echo $((16#$(tail -1 ${DOWNLOADED_LOGS}/${node_array[$i]}.log | awk  '{print $4}'))))
    awk '$0 ~ /rebooting/ {print $10}' $USER_REPORT/${node_array[$i]}.report > searchtime.tmp
    searchTimesMid=$(${Cur_Dir}/searchtime searchtime.tmp)
    rm searchtime.tmp
    let times=$searchTimesEnd+$searchTimesMid-$searchTimesBeg
    sensorErrTimes=$(awk 'END {print NR}' $USER_REPORT/${node_array[$i]}.sensorErr)
    echo "${id} ${total} ${lost} ${ratio} ${rebootTimes} ${times} ${sensorErrTimes}" | awk '{printf "%8s %8s %8s %8.2f%% %8s %8s %15s\n",$1,$2,$3,100*$4,$5,$6,$7}' >> $USER_REPORT/all.report.tmp
done
    sort $USER_REPORT/all.report.tmp >> $USER_REPORT/all.report
    rm  $USER_REPORT/all.report.tmp

exit
