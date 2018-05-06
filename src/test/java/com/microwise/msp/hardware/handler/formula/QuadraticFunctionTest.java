package com.microwise.msp.hardware.handler.formula;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 14-1-3 10:40
 */
public class QuadraticFunctionTest {
    @Test
    public void test() {
        Function function = new QuadraticFunction();

        Map<String, Double> params = new HashMap<String, Double>();
        params.put("a", 1.2);
        params.put("b", 2.1);
        params.put("c", 3.3);

        double result = function.compute(2D, params);
        Assert.assertEquals(12.3, result, 0);

        result = function.compute(-2D, params);
        Assert.assertEquals(3.9, result, 0.1);
    }
}
