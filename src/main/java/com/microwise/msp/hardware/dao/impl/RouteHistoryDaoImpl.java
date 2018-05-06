package com.microwise.msp.hardware.dao.impl;

import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.dao.CarDao;
import com.microwise.msp.hardware.dao.RouteHistoryDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Repository
@Scope("prototype")
public class RouteHistoryDaoImpl extends BaseDaoImpl implements RouteHistoryDao {

    @Override
    public void insert(int carId, double longitude, double latitude, Date time) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("carId", carId);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("time", time);
        getSqlSession().insert("RouteHistory.insertRouteHistory", params);
    }
}
