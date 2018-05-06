package com.microwise.msp.hardware.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 车辆dao
 *
 * @author sun.cong
 * @date 18-3-26 上午9:23
 */
public interface CarDao {

    /**
     * 通过位置点id查找车辆id
     *
     * @param locationId
     * @return
     */
    public int findCarIdByLocationId(@Param("locationId") String locationId);
}
