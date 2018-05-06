package com.microwise.msp.hardware.service;


/**
 * author:chen.yaofei
 * date : 2016-07-04
 */
public interface LogAnalysisService {
    /**
     * 执行shell文件
     */
    public boolean doLogAnalysis(String cmdArray[]);
}
