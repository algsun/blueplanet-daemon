package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.rabbitmq.Location;
import com.microwise.msp.hardware.businessbean.User;

import java.util.List;

/**
 * @author lijianfei
 * @date 2016-07-13
 */
public interface LocationDao {

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

    public List<Location> findLocations(String zoneId);
}
