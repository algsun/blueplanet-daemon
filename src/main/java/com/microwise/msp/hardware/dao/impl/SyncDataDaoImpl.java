package com.microwise.msp.hardware.dao.impl;

import com.google.common.collect.Maps;
import com.microwise.msp.hardware.dao.SyncDataDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Obelisk 数据同步 Dao 实现
 *
 * @author li.jianfei
 * @date 2016-01-26
 */
@Component
@Scope("prototype")
public class SyncDataDaoImpl extends BaseDaoImpl implements SyncDataDao {

    @Override
    public void convertHistoryDataToSyncData(String locationId, Date date) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("tableName", locationId);
        params.put("createTime", date);
        getSqlSession().insert("SyncData.convertHistoryDataToSyncData", params);
    }
}
