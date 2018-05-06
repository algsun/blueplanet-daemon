package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.cache.AppCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存 controller
 *
 * @author gaohui
 * @date 13-11-11 13:52
 */
@Component
@Scope("prototype")
@Route("/struts")
public class CacheController {
    public static final Logger log = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    private AppCache appCache;

    @Route("/caches")
    public String cache() {
        return Results.ftl("/pages/caches.ftl");
    }

    @Route(value = "/caches/device", params = {"_method=delete"})
    public String evictDeviceCache() {
        appCache.evictDevices();
        return Results.redirect("/struts/caches");
    }

    @Route(value = "/caches/device-sensors", params = {"_method=delete"})
    public String evictDeviceSensors() {
        appCache.evictDeviceSensors();
        return Results.redirect("/struts/caches");
    }

    @Route(value = "/caches/formula", params = {"_method=delete"})
    public String evictFormula() {
        appCache.evictFormula();
        return Results.redirect("/struts/caches");
    }

    @Route(value = "/caches/device-custom-formula", params = {"_method=delete"})
    public String evictAllDeviceCustomFormula() {
        appCache.evictCustomFormula();
        return Results.redirect("/struts/caches");
    }

    // input
    private String deviceId;

    @Route(value = "/caches/device-custom-formula", params = {"_method=delete", "deviceId"})
    public String evictDeviceCustomFormula() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            appCache.evictCustomFormula(deviceId);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(result).done();
    }

    @Route(value = "/caches/sensors", params = {"_method=delete"})
    public String evictSensorCache() {
        appCache.evictSensors();
        return Results.redirect("/struts/caches");
    }

    @Route(value = "/caches/threshold-alarms", params = {"_method=delete"})
    public String evictThresholdAlarm() {
        // TODO 已经去掉缓存，暂时保留接口 @gaohui 2014-03-21
        // appCache.evictThresholdCaches();
        return Results.redirect("/struts/caches");
    }

    @Route(value = "/caches/evictAllFloatSensor", params = {"_method=delete"})
    public String evictAllFloatSensor() {
        try {
            appCache.evictFloatSensor();
        } catch (Exception e) {
            log.error("", e);
        }
        return Results.redirect("/struts/caches");
    }

    @Route(value = "/caches/evictFloatSensor", params = {"_method=delete","deviceId"})
    public String evictFloatSensor() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            appCache.evictFloatSensor(deviceId);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(result).done();
    }

    @Route(value = "/caches/evictDeviceById", params = {"_method=delete","deviceId"})
    public String evictDeviceById() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            appCache.evictDevice(deviceId);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            log.error("", e);
        }
        return Results.json().asRoot(result).done();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
