package com.microwise.msp.hardware.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 13-8-6 09:50
 */
public class StatsTest {
    @Test
    public void testIncrWrites() {
        Stats stats = Stats.getInst();

        Assert.assertEquals(0, stats.packetWrites());
        stats.incrPacketWrites(1);
        Assert.assertEquals(1, stats.packetWrites());
        Assert.assertEquals(2, stats.incrPacketWrites(1));
        stats.resetPacketWrites();
        Assert.assertEquals(0, stats.packetWrites());
    }
}
