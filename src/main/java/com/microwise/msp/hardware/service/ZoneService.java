package com.microwise.msp.hardware.service;

/**
 * Created by Administrator on 2017/11/15.
 */
public interface ZoneService {
    /**
     * 通过id查找区域或者文物名称
     *
     * @param systemFlag
     * @param zoneId
     * @return
     */
    String findZoneNameById(int systemFlag, String zoneId);
}
