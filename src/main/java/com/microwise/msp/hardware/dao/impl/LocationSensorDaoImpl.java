package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.dao.LocationSensorDao;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LocationSensorDao实现
 *
 * @author liuzhu
 * @date 2014-12-11
 */

@Component
@Scope("prototype")
public class LocationSensorDaoImpl extends BaseDaoImpl implements LocationSensorDao {

    private static Logger log = LoggerFactory.getLogger(AnalysisDaoImpl.class);

    @Override
    public List<LocationSensor> findLocationSensor(String locationId) {
        return getSqlSession().selectList("LocationSensor.findLocationSensor", locationId);
    }


    @Override
    public boolean initLocationSensor(List<LocationSensor> sensorList) {
        boolean isTrue = true;
        try {
            getSqlSession().insert("LocationSensor.initLocationSensorList", sensorList);
        } catch (DataAccessException e) {
            log.error("\n\n 批量-初始化节点传感信息(initLocationSensor)出错 \n\n", e);
            isTrue = false;
        }
        return isTrue;
    }

    /**
     * 过滤double中多余的小数位
     * <p/>
     *
     * @param overMathValue    数值
     * @param sensorPhysicalid 传感标识
     * @return
     */
    private String getStringSensorValue(double overMathValue, int sensorPhysicalid) {
        // 获取精度
        int precision = getPrecison(sensorPhysicalid);
        // 精度处理
        return StringUtil.round(overMathValue, precision);
    }

    private int getPrecison(int sensorPhysicalid) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("sensorPhysicalid", sensorPhysicalid);
        Integer percison = 0;
        try {
            percison = (Integer) getSqlSession().selectOne("Analysis.getPrecison", paraMap);
        } catch (DataAccessException e) {
            percison = 0;
            log.error("\n\n 获取精度getPrecison(" + sensorPhysicalid + ")出错 \n\n", e);
        }
        return percison;
    }


    @Override
    public void updateLocationSensorMemory(DeviceBean device, SensorPhysicalBean sensorPhysicalBean) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        String sensorValue = getStringSensorValue(sensorPhysicalBean.sensor_Value, sensorPhysicalBean.sensorPhysical_id);
        paraMap.put("sensorValue", sensorValue);
        paraMap.put("state", sensorPhysicalBean.sensor_State);
        paraMap.put("stamp", device.timeStamp);
        paraMap.put("locationId", device.locationId);
        paraMap.put("sensorId", sensorPhysicalBean.sensorPhysical_id);
        try {
            getSqlSession().update("LocationSensor.updateSensorMemory",
                    paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 更新locationsensor(updateSensorMemory)出错 \n\n", e);
        }
    }

    @Override
    public LocationSensor findLocationSensorData(String locationId, int sensorId) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("sensorId", sensorId);
        return (LocationSensor) getSqlSession().selectOne("LocationSensor.findLocationSensorData",
                paraMap);
    }

    @Override
    public void deleteInsertData(String locationId, Date stamp) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("stamp", stamp);
        Date date = getSqlSession().selectOne("LocationSensor.findStamp", paramMap);
        paramMap.put("date", date);
        getSqlSession().delete("LocationSensor.deleteLocationData", paramMap);
    }
}
