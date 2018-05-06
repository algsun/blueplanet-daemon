package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.Zone;

/**
 * @author bastengao
 * @date 14-3-19 下午4:28
 */
public interface ZoneDao {

    /**
     * 查询区域信息
     *
     * @param id 区域ID
     * @return
     */
    Zone findZone(String id);

    /**
     * 查询区域名称/文物名称
     *
     * @param systemFlag
     * @param zoneId
     * @return
     */
    String findZoneNameById(int systemFlag, String zoneId);
}
