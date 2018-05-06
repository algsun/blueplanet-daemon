package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.handler.codec.Versions;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author bastengao
 * @date 14-4-4 上午9:50
 */
public class DcoOperateServiceTest {

    @Test
    public void testGetParentId(){

        // v1 协议 netId 77
        String deviceId = "6101150177315";
        String parentId = DcoOperateService.getParentIp(deviceId, 10, Versions.V_1);
        Assert.assertEquals("6101150177010", parentId);


        // v3 协议
        deviceId = "6101150100315";
        parentId = DcoOperateService.getParentIp(deviceId, 10, Versions.V_3);
        Assert.assertEquals("6101150100010", parentId);
    }
}
