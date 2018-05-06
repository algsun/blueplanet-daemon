package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.businessbean.*;

import java.util.List;

/**
 * @author bastengao
 * @date 14-3-19 下午2:54
 */
public interface ThresholdService {

    public List<Threshold> findThresholds(String deviceId);


    /**
     * 添加报警历史
     *
     * @param history
     */
    void addAlarmHistory(ThresholdAlarmHistory history);
}
