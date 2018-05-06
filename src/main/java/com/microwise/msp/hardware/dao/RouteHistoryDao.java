package com.microwise.msp.hardware.dao;

import java.util.Date;

/**
 * 车辆dao
 *
 * @author sun.cong
 * @date 18-3-26 上午9:23
 */
public interface RouteHistoryDao {

    /**
     * 通过位置点id查找车辆id
     *
     * @param carId     车辆id
     * @param longitude 经度
     * @param latitude  纬度
     * @param time      时间戳
     * @return
     */
    public void insert(int carId, double longitude, double latitude, Date time);
}
