package com.microwise.msp.hardware.dao;

/**
 * Created by sun.cong on 2017/11/15.
 */
public interface SourceDao {
    /**
     * 查找sourceName
     *
     * @param systemFlag
     * @param sourceId
     * @return
     */
    String findSourceNameById(int systemFlag, String sourceId);
}
