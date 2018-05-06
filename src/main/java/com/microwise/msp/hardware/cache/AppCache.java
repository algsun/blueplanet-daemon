package com.microwise.msp.hardware.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NetInfo;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 整个应用的缓存
 * <p/>
 * 注意：单例
 *
 * @author gaohui
 * @date 13-7-19 10:07
 */
@Component
@Scope("singleton")
public class AppCache {
    public static final Logger log = LoggerFactory.getLogger(AppCache.class);

    /**
     * 监测指标缓存
     * <p/>
     * sensorPhysicalId => sensor
     */
    private LoadingCache<Integer, Sensor> sensorCache;

    /**
     * 默认公式
     */
    private LoadingCache<Integer, Formula> defaultFormulaCache;

    /**
     * 设备自定义公式
     */
    private LoadingCache<String, Map<Integer, CustomFormula>> customFormulaCache;

    /**
     * 设备监测指标缓存
     */
    private LoadingCache<String, List<NodeSensor>> deviceSensorCache;

    private LoadingCache<String, List<LocationSensor>> locationSensorCache;

    private LoadingCache<String,List<FloatSensor>> floatSensorCache;

    /**
     * 连接信息缓存
     * <p/>
     * port => netinfo
     */
    private LoadingCache<Integer, NetInfo> netInfoCache;

    /**
     * 站点缓存
     */
    private LoadingCache<String, Site> siteCache;

    /**
     * 设备上传状态缓存
     */
    private LoadingCache<String, Integer> deviceUploadStateCache;

    /**
     * 设备上传缓存
     */
    private LoadingCache<String, DeviceBean> deviceCache;

    /**
     * 初始化监测指标缓存
     *
     * @param sensorCacheLoader
     */
    @Autowired
    public void initSensorCache(SensorCacheLoader sensorCacheLoader) {
        sensorCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(sensorCacheLoader);
    }

    /**
     * 如果无返回 null
     *
     * @param sensorPhysicalId
     * @return
     */
    public Sensor loadSensor(int sensorPhysicalId) {
        try {
            return sensorCache.get(sensorPhysicalId);
        } catch (UncheckedExecutionException e) {
            log.debug("加载监测指标", e);
            return null;
        } catch (ExecutionException e) {
            log.error("加载监测指标", e);
            return null;
        }
    }

    /**
     * 清除所有缓存
     */
    public void evictSensors() {
        sensorCache.invalidateAll();
    }


    /**
     * 初始化监测指标缓存
     *
     * @param
     */
    @Autowired
    public void initUploadStateCache(DeviceUploadStateLoader deviceUploadStateLoader) {
        deviceUploadStateCache = CacheBuilder.newBuilder().build(deviceUploadStateLoader);
    }

    /**
     * 如果无返回 null
     *
     * @return
     */
    public Integer loadUploadState(String deviceId) {
        try {
            return deviceUploadStateCache.get(deviceId);
        } catch (Exception e) {
            log.error("加载监测指标", e);
            return null;
        }
    }

    /**
     * 清除缓存
     */
    public void evictUploadState(String deviceId) {
        deviceUploadStateCache.invalidate(deviceId);
    }

    /**
     * 初始化公式系数
     *
     * @param defaultFormulaLoader
     */
    @Autowired
    public void initFormulaCache(DefaultFormulaLoader defaultFormulaLoader) {
        defaultFormulaCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(defaultFormulaLoader);
    }

    /**
     * 加载监测指标默认公式
     *
     * @param sensorId
     * @return
     */
    public Formula loadFormula(int sensorId) {
        try {
            return defaultFormulaCache.get(sensorId);
        } catch (ExecutionException e) {
            return null;
        }
    }

    /**
     * 清除监测指标默认公式
     */
    public void evictFormula() {
        defaultFormulaCache.invalidateAll();
    }

    /**
     * 初始化设备自定义公式
     *
     * @param customFormulaCacheLoader
     */
    @Autowired
    public void initCustomFormulaCache(CustomFormulaCacheLoader customFormulaCacheLoader) {
        customFormulaCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(customFormulaCacheLoader);
    }

    /**
     * 根据设备ID返回自定义公式系数, 如果没有返回空的 Map，而不是 null。
     *
     * @param deviceId
     * @return
     */
    public Map<Integer, CustomFormula> loadCustomFormula(String deviceId) {
        try {
            return customFormulaCache.get(deviceId);
        } catch (ExecutionException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 清除缓存
     */
    public void evictCustomFormula() {
        customFormulaCache.invalidateAll();
    }

    /**
     * 清除某个设备的缓存
     *
     * @param deviceId
     */
    public void evictCustomFormula(String deviceId) {
        customFormulaCache.invalidate(deviceId);
    }

    /**
     * 初始化设备浮动值
     *
     * @param floatSensorLoader
     */
    @Autowired
    public void initFloatSensorCache(FloatSensorLoader floatSensorLoader) {
        floatSensorCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(floatSensorLoader);
    }

    /**
     *
     *
     * @param deviceId
     * @return
     */
    public List<FloatSensor> loadFloatSensor(String deviceId) {
        try {
            return floatSensorCache.get(deviceId);
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }

    /**
     * 清除缓存
     */
    public void evictFloatSensor() {
        floatSensorCache.invalidateAll();
    }

    /**
     * 清除某个设备浮动值缓存
     *
     * @param deviceId
     */
    public void evictFloatSensor(String deviceId) {
        floatSensorCache.invalidate(deviceId);
    }

    /**
     * 初始化缓存
     *
     * @param deviceSensorLoader
     */
    @Autowired
    public void initDeviceSensorCache(DeviceSensorLoader deviceSensorLoader) {
        deviceSensorCache = CacheBuilder.newBuilder()
                // 访问一个小时后过期
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build(deviceSensorLoader);
    }

    /**
     * 初始化缓存
     *
     * @param locationSensorLoader
     */
    @Autowired
    public void initLocationSensorCache(LocationSensorLoader locationSensorLoader) {
        locationSensorCache = CacheBuilder.newBuilder()
                // 访问一个小时后过期
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build(locationSensorLoader);
    }

    /**
     * 返回设备的监测指标
     *
     * @param deviceId
     * @return
     */
    public List<NodeSensor> loadDeviceSensors(String deviceId) {
        try {
            return deviceSensorCache.get(deviceId);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 返回设备的监测指标
     *
     * @param locationId
     * @return
     */
    public List<LocationSensor> loadLocationSensors(String locationId) {
        try {
            return locationSensorCache.get(locationId);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * 清除某个设备的监测指标缓存
     *
     * @param deviceId
     */
    public void evictDeviceSensors(String deviceId) {
        deviceSensorCache.invalidate(deviceId);
    }

    /**
     * 清除某个设备的监测指标缓存
     *
     * @param locationId
     */
    public void evictLocationSensors(String locationId) {
        deviceSensorCache.invalidate(locationId);
    }

    /**
     * 清除所有设备的监测指标缓存
     */
    public void evictDeviceSensors() {
        deviceSensorCache.invalidateAll();
    }

    /**
     * 清除所有设备的监测指标缓存
     */
    public void evictLocationSensors() {
        locationSensorCache.invalidateAll();
    }


    /**
     * 初始化缓存
     *
     * @param netInfoCacheLoader
     */
    @Autowired
    public void initNetInfoCache(NetInfoCacheLoader netInfoCacheLoader) {
        netInfoCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(netInfoCacheLoader);
    }

    /**
     * 根据UDP端口查找对应链接信息, 如果没有返回 null
     *
     * @param port
     * @return
     */
    public NetInfo loadNetInfo(int port) {
        try {
            return netInfoCache.get(port);
        } catch (ExecutionException e) {
            //未找到对应端口信息
            return null;
        }
    }

    /**
     * 清除缓存
     */
    public void evictNetInfos() {
        netInfoCache.invalidateAll();
    }

    /**
     * 初始化站点缓存
     *
     * @param siteCacheLoader
     */
    @Autowired
    public void initSiteCache(SiteCacheLoader siteCacheLoader) {
        siteCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build(siteCacheLoader);
    }

    /**
     * 根据siteId 查找 Site
     *
     * @param siteId
     * @return
     */
    public Site loadSite(String siteId) {
        try {
            return siteCache.get(siteId);
        } catch (Exception e) {
            log.debug("加载站点", e);
            return null;
        }
    }

    /**
     * 初始化设备
     *
     * @param
     */
    @Autowired
    public void initDevicesCache(DeviceCacheLoader deviceCacheLoader) {
        deviceCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(deviceCacheLoader);

    }

    /**
     * 加载设备
     *
     * @param deviceId 设备编号
     */
    public DeviceBean loadDevice(String deviceId) {
        try {
            return deviceCache.get(deviceId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取设备缓存
     */
    public LoadingCache<String, DeviceBean> getDeviceCache() {
        return deviceCache;
    }

    /**
     * 清除单个设备
     */
    public void evictDevice(String deviceId) {
        deviceCache.invalidate(deviceId);
    }

    /**
     * 清除所有设备
     */
    public void evictDevices() {
        deviceCache.invalidateAll();
    }
}
