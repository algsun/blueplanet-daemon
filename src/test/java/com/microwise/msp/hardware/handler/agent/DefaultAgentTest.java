package com.microwise.msp.hardware.handler.agent;

import com.microwise.msp.hardware.handler.agent.v30.DefaultAgent;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

/**
 * @author gaohui
 * @date 13-8-16 12:13
 */
public class DefaultAgentTest {
    @Test
    public void test(){
        Timestamp timestamp = DefaultAgent.DARK_END;

        DateTime time = new DateTime(timestamp);
        Assert.assertEquals(2007, time.getYear());
        Assert.assertEquals(1, time.getMonthOfYear());
        Assert.assertEquals(1, time.getDayOfMonth());
        Assert.assertEquals(0, time.getHourOfDay());
        Assert.assertEquals(0, time.getMinuteOfHour());
        Assert.assertEquals(0, time.getSecondOfDay());
    }
}
