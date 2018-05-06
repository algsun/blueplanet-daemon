package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.dao.CarDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository
@Scope("prototype")
public class CarDaoImpl extends BaseDaoImpl implements CarDao {

    @Override
    public int findCarIdByLocationId(String locationId) {
        return getSqlSession().selectOne("Car.findCarIdByLocationId", locationId);
    }
}
