#!/bin/bash

###########################################################
# Decription:
#     Output reports to user.
# Author: quandk
# Date: 2015-06-16
###########################################################
#set -x

#$0-userReport.sh的绝对路径调用
#$1-siteId
#$2-deviceId
#$3-beginTime
#$4-endTime
#$5-sourcePath
#S6-targetPath

VERSION=1.0.0
if [ x$1 = x"-v" ]; then
    echo "userReport Version: $VERSION"
    exit 0
fi

#输入参数个数校验
if [ $# != 6 ]; then
    echo "参数个数应为6"
    exit 1
fi

apt-get install gcc
apt-get install make

basepath=$(cd `dirname $0`; pwd)
cd $basepath

if [ ! -f "compileFile.sh" ]; then
    echo "compileFile.sh is not exist."
    exit 1
fi

chmod +x compileFile.sh && bash ./compileFile.sh

# setp 1: User files and rights
if [ ! -f "download.sh" ]; then
    echo "download.sh is not exist."
    exit 2
fi

if [ ! -f "parser.sh" ]; then
    echo "parser.sh is not exist."
    exit 3
fi

if [ ! -f "makeReport.sh" ]; then
    echo "makeReport.sh is not exist."
    exit 4
fi

if [ ! -f "analysis" ]; then
    echo "analysis is not exist."
    exit 5
fi

if [ ! -f "resendAnalysis" ]; then
    echo "resendAnalysis is not exist."
    exit 6
fi

if [ ! -f "searchtime" ]; then
    echo "searchtime is not exist."
    exit 7
fi



chmod +x download.sh parser.sh  makeReport.sh analysis resendAnalysis searchtime

# setp 2: Downloading logs
echo ====== Downloading BEG ======
bash ./download.sh $1 "$2" "$3" "$4" $5 $6
echo ====== Downloading END ======

# setp 3: Parser logs
echo ====== Analysis BEG ======
bash ./parser.sh $1 "$2" "$3" "$4" $5 $6
echo ====== Analysis END ======

# setp 4: make report
echo ====== Report BEG ======
bash ./makeReport.sh $1 "$2" "$3" "$4" $5 $6
echo ====== Report END ======

exit 0
