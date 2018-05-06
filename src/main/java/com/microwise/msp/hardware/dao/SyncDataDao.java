package com.microwise.msp.hardware.dao;

import java.util.Date;

/**
 * Obelisk 数据同步 Dao
 *
 * @author li.jianfei
 * @date 2016-01-26
 */
public interface SyncDataDao {

    /**
     * 转换指定时间点的历史数据到同步数据表
     *
     * @param date
     */
    public void convertHistoryDataToSyncData(String locationId, Date date);

}
