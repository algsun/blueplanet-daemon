package com.microwise.msp.hardware.dao.impl;

import com.google.common.collect.Maps;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.dao.ThresholdDao;
import com.microwise.msp.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阈值报警Dao实现
 *
 * @author: wanggeng
 * @date: 13-8-26 下午2:45
 * @check xu.baoji 2013-09-02 #5353
 */
@Component
@Scope("prototype")
public class ThresholdDaoImpl extends BaseDaoImpl implements ThresholdDao {

    @Override
    public List<Threshold> findThresholds(String locationId) {
        return getSqlSession().selectList("Threshold.findThresholds", locationId);
    }

    @Override
    public void appendAlarmHistory(ThresholdAlarmHistory history) {
        getSqlSession().insert("Threshold.appendThresholdAlarmHistory", history);
    }

//    @Override
//    public void appendAlarmHistoryMeasure(String alarmHistoryId, String measureId) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("id", StringUtil.uuid());
//        params.put("alarmHistoryId", alarmHistoryId);
//        params.put("measureId", measureId);
//
//        getSqlSession().insert("Threshold.appendAlarmHistoryMeasure", params);
//    }

//    @Override
//    public List<Measure> findMeasuresByZoneId(String zoneId) {
//        return getSqlSession().selectList("Threshold.findMeasuresByZoneId", zoneId);
//    }

    @Override
    public int addOrUpdateThreshold(Threshold threshold) {
        return getSqlSession().insert("Threshold.addOrUpdateThreshold", threshold);
    }

}
