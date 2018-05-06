package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.Measure;
import com.microwise.msp.hardware.businessbean.Threshold;
import com.microwise.msp.hardware.businessbean.ThresholdAlarmHistory;

import java.util.List;

/**
 * 阈值报警Dao
 *
 * @author: wanggeng
 * @date: 13-8-26 下午2:41
 */
public interface ThresholdDao {

    /**
     * 按报警类型查询报警条件
     *
     * @param locationId 位置点ID
     * @return 报警条件集合
     */
    public List<Threshold> findThresholds(String locationId);

    /**
     * 添加报警记录
     * */
    void appendAlarmHistory(ThresholdAlarmHistory history);

//    /**
//     * 添加报警记录对应的措施
//     * @param alarmHistoryId 报警记录Id
//     * @param  measureId 措施id
//     * */
//    void appendAlarmHistoryMeasure(String alarmHistoryId, String measureId);

//    /**
//     * 获取区域下的措施集合
//     * @param zoneId  区域id
//     * @return  措施集合
//     * */
//    List<Measure> findMeasuresByZoneId(String zoneId);

    /**
     * 添加或修改设备监测指标阈值
     *
     * @param threshold
     * @return
     */
    public int addOrUpdateThreshold(Threshold threshold);


}
