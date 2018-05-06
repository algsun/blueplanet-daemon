package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.businessbean.rabbitmq.Location;
import com.microwise.msp.hardware.businessbean.User;

import java.util.List;

/**
 * @author li.jianfei
 * @date 2016-07-14
 */
public interface LocationService {

    /**
     * 查询位置点信息
     *
     * @param id 位置点ID
     * @return
     */
    public Location findLocation(String id);

    /**
     * 查询位置点关注人
     *
     * @param locationId 位置点ID
     * @return
     */
    public List<User> findFollowers(String locationId);

    /**
     * 查询区域或文物关联位置点集合
     *
     * @param zoneId
     * @return
     */
    public List<Location> findLocations(String zoneId);

}
