package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.businessbean.DeviceBean;

/**
 * 当收到数据时，且是实时数据时的监听器
 *
 * @author gaohui
 * @date 13-8-26 11:04
 */
public interface DataReceivedListener {

    /**
     * 当收到数据时
     *
     * @param deviceBean
     */
    public void onDataReceived(DeviceBean deviceBean);
}
