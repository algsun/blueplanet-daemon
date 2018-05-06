package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.dao.CarDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 车辆service
 *
 * @author sun.cong
 * @date 18-3-26
 */
@Component
@Scope("prototype")
public class CarService {

    @Autowired
    private CarDao carDao;

    /**
     * 通过位置点id查找车辆id
     *
     * @param locationId 位置点id
     * @return
     */
    public int findCarIdByLocationId(String locationId) {
        return carDao.findCarIdByLocationId(locationId);
    }

}
