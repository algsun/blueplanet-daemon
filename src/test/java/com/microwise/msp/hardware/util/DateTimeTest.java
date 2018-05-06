package com.microwise.msp.hardware.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 13-9-13 15:47
 */
public class DateTimeTest {
    @Test
    public void testDuration(){
        DateTime dateTime = DateTime.now();
        DateTime dateTime2 = dateTime.plusHours(1);

        Duration duration = new Duration(dateTime, dateTime2);
        Assert.assertEquals(1, duration.getStandardHours());

        Duration duration2 = new Duration(dateTime2, dateTime);
        Assert.assertEquals(-1, duration2.getStandardHours());
    }
}
