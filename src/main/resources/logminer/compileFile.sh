#!/bin/bash

###########################################################
# Decription:
#     Output reports to user.
# Author: sunbo
# Date: 2016-08-10
###########################################################
#set -x

VERSION=1.0.0
if [ x$1 = x"-v" ]; then
    echo "userReport Version: $VERSION"
    exit 0
fi

cd analysisSrc && make && cp analysis ..
cd ../resendSrc   && make && cp resendAnalysis ..
cd ../searchtimeSrc  && make && cp searchtime ..

exit 0
