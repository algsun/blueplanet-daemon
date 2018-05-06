package com.microwise.msp.util;

import com.google.common.primitives.Bytes;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 13-8-6 16:52
 */
public class CRCUtilTest {
    @Test
    public void testGenCRC16(){
        int crc = CRCUtil.generateCRC(Bytes.asList( new byte[]{0x01, 0x02, 0x00, (byte) 0xC4, 0x00, 0x16}));
        Assert.assertEquals(0x39B8, crc);
    }

    @Test
    public void testGetCRC16(){
        int crc = CRCUtil.getCRC(Bytes.asList( new byte[]{0x01, 0x02, 0x00, (byte) 0xC4, 0x00, 0x16, 0x00, 0x00}));
        Assert.assertEquals(0x39B8, crc);
    }
}
