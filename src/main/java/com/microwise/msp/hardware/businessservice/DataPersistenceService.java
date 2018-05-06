package com.microwise.msp.hardware.businessservice;

import com.google.common.base.Strings;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.dao.DataPacketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据持久化Service
 *
 * @author he.ming
 * @since Feb 18, 2013
 */
//TODO 需要过滤位置点信息
@Component
@Scope("prototype")
@Transactional
public class DataPersistenceService {
    /**
     * 解析 dao
     */
    @Autowired
    private AnalysisDao dao;

    /**
     * 原始数据包dao 2013-1-21
     */
    @Autowired
    private DataPacketDao packetDao;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RouteHistoryService routeHistoryService;

    /**
     * 批量入库
     *
     * @param deviceBeans
     */
    public void saveBatch(List<DeviceBean> deviceBeans) {
        for (DeviceBean deviceBean : deviceBeans) {
            persistence(deviceBean);
        }
    }

    /**
     * 批量缓存
     *
     * @param deviceBeans
     */
    public void saveCacheBatch(List<DeviceBean> deviceBeans) {
        for (DeviceBean deviceBean : deviceBeans) {
            cacheDataPacket(deviceBean);
        }
    }

    /**
     * 原始数据包缓存
     *
     * @param deviceBean
     * @author he.ming
     * @since 2013-1-21
     */
    public void cacheDataPacket(DeviceBean deviceBean) {
        // ①是否打开缓存、②缓存表是否存在(创建缓存表)、③缓存数据是否存在(已存在的不缓存)
        String tableName = "packet_".concat(deviceBean.deviceid);
        boolean hasTable = false;
        if (SysConfig.getInstance().getCacheTableMap().containsKey(tableName)) {
            hasTable = true;
        } else {
            hasTable = packetDao.isExistTable(tableName);
        }
        if (!hasTable) {
            hasTable = packetDao.createPacketTable(tableName);
        }
        if (hasTable) {
            SysConfig.getInstance().getCacheTableMap().put(tableName, tableName);
            boolean hasPacket = packetDao.isExistPacket(deviceBean);
            if (!hasPacket) {
                packetDao.savePacket(deviceBean);
            }
        }
    }

    /**
     * 历史数据入库
     *
     * @param deviceBean
     * @author he.ming
     * @since Feb 18, 2013
     */
    public void persistence(DeviceBean deviceBean) {
        if (Strings.isNullOrEmpty(deviceBean.locationId)) {
            deviceBean.locationId = appCache.loadDevice(deviceBean.deviceid).locationId;
        }
        if (!deviceBean.sensorData.values().isEmpty() && deviceBean.isConfigLocation()) {
            dao.addNodeDatas(deviceBean);
            routeHistoryService.insert(deviceBean);//车辆运行路线数据入库
        }
    }

    public void setPacketDao(DataPacketDao packetDao) {
        this.packetDao = packetDao;
    }

    public void setDao(AnalysisDao dao) {
        this.dao = dao;
    }

    public void setAppCache(AppCache appCache) {
        this.appCache = appCache;
    }

}
