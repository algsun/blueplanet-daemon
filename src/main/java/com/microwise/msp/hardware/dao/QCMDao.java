package com.microwise.msp.hardware.dao;

import java.util.Date;

/**
 * qcm
 *
 * @author liuzhu
 * @date 2016-1-6
 */
public interface QCMDao {

    /**
     * 插入更换时间
     *
     * @param locationId  位置点id
     * @param replaceDate 更换时间
     * @author liuzhu
     * @date 2016-1-6
     */
    public void insertReplaceSensor(String locationId, Date replaceDate);

    /**
     * 是否存在
     *
     * @param locationId 位置点id
     * @param replaceDate 更换时间
     * @return
     */
    public boolean isExist(String locationId,Date replaceDate);
}
