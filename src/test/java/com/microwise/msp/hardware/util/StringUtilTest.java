package com.microwise.msp.hardware.util;

import com.microwise.msp.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 13-8-17 15:08
 */
public class StringUtilTest {
    @Test
    public void testUUID(){
        Assert.assertNotNull(StringUtil.uuid());
        Assert.assertEquals(32, StringUtil.uuid().length());
    }
}
