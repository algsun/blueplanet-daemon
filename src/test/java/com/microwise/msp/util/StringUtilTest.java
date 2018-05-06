package com.microwise.msp.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bastengao
 * @date 14-4-4 上午10:06
 */
public class StringUtilTest {
    @Test
    public void testFillZero() {
        String result = StringUtil.fillZero(120, 5);
        Assert.assertEquals("00120", result);

        result = StringUtil.fillZero(120, 3);
        Assert.assertEquals("120", result);

        result = StringUtil.fillZero(120, 2);
        Assert.assertEquals("120", result);
    }
}
