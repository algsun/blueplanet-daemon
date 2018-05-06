package com.microwise.msp.hardware.service;

/**
 * 站点/机构Service
 * Created by Administrator on 2017/11/15.
 */
public interface SourceService {
    /**
     * 通过sourceId查找sourceName
     *
     * @param systemFlag
     * @param sourceId
     * @return
     */
    String findSourceNameById(int systemFlag, String sourceId);
}
