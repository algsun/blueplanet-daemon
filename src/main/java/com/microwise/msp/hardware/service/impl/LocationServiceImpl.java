package com.microwise.msp.hardware.service.impl;

import com.google.common.base.Strings;
import com.microwise.msp.hardware.businessbean.rabbitmq.Location;
import com.microwise.msp.hardware.businessbean.User;
import com.microwise.msp.hardware.dao.LocationDao;
import com.microwise.msp.hardware.dao.ZoneDao;
import com.microwise.msp.hardware.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author li.jianfei
 * @date 2016-07-14
 */

@Service
@Scope("prototype")
@Transactional
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private ZoneDao zoneDao;

    @Override
    public Location findLocation(String id) {
        Location location = locationDao.findLocation(id);
        if (!Strings.isNullOrEmpty(location.getZoneId()))
            location.setZone(zoneDao.findZone(location.getZoneId()));
        return location;
    }

    @Override
    public List<User> findFollowers(String locationId) {
        return locationDao.findFollowers(locationId);
    }

    @Override
    public List<Location> findLocations(String zoneId) {
        return locationDao.findLocations(zoneId );
    }

}
