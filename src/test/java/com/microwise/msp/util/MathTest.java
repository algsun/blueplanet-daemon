package com.microwise.msp.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 13-8-2 10:38
 */
public class MathTest {

    @Test
    public void testRounding(){
        double a = 12.345;

        double b = Maths.roundToDouble(a, 2);
        Assert.assertEquals(12.35, b, 0);

        a = 12.344;
        b = Maths.roundToDouble(a, 2);
        Assert.assertEquals(12.34, b, 0);


        a = -12.345;
        b = Maths.roundToDouble(a, 2);
        Assert.assertEquals(-12.35, b, 0);

        a = -12.344;
        b = Maths.roundToDouble(a, 2);
        Assert.assertEquals(-12.34, b, 0);
    }
}
