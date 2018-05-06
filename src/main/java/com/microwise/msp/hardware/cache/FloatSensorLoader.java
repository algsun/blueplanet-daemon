package com.microwise.msp.hardware.cache;

import com.google.common.base.Strings;
import com.google.common.cache.CacheLoader;
import com.microwise.msp.hardware.businessbean.FloatSensor;
import com.microwise.msp.hardware.businessservice.FloatSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监测指标浮动值缓存
 *
 * @author liuzhu
 * @date 2016-5-23
 */
@Component
@Scope("prototype")
public class FloatSensorLoader extends CacheLoader<String, List<FloatSensor>> {
    @Autowired
    private FloatSensorService floatSensorService;

    @Override
    public List<FloatSensor> load(String deviceId) throws Exception {
        List<FloatSensor> floatSensors = floatSensorService.floatSensorList(deviceId);
        if (Strings.isNullOrEmpty(deviceId)) {
            throw new Exception("未找到设备监测指标浮动值: " + deviceId);
        }
        return floatSensors;
    }
}
