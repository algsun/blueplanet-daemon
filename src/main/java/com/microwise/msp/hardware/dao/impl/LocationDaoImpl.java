package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.rabbitmq.Location;
import com.microwise.msp.hardware.businessbean.User;
import com.microwise.msp.hardware.dao.LocationDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 位置点
 *
 * @author li.jianfei
 * @date 2016-07-13
 */
@Component
@Scope("prototype")
public class LocationDaoImpl extends BaseDaoImpl implements LocationDao {
    public Location findLocation(String id) {
        return getSqlSession().selectOne("Location.findById", id);
    }

    @Override
    public List<User> findFollowers(String locationId) {
        return getSqlSession().selectList("Location.findFollowers", locationId);
    }

    @Override
    public List<Location> findLocations(String zoneId) {
        return getSqlSession().selectList("Location.findLocations", zoneId);
    }

}
