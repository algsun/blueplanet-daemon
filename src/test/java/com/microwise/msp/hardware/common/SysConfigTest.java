package com.microwise.msp.hardware.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * @author bastengao
 * @date 14-3-10 下午1:33
 */
public class SysConfigTest {
    @Test
    public void testLoadSystemConfig(){
        SysConfig.getInstance();

        Assert.assertEquals(10, SysConfig.getInstance().getSynchronizeInterval());
        Assert.assertEquals(9912, SysConfig.getInstance().getSynchronizePort());
    }

    @Test
    public void initConfigFromYaml() {
        Set<String> sites = SysConfig.getInstance().getSitesWithoutTimeOutCheck();

        Assert.assertEquals(2, sites.size());
        Assert.assertTrue(sites.contains("61011501"));
        Assert.assertTrue(sites.contains("31010101"));
    }
}
