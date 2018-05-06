package com.microwise.msp.hardware.handler.formula;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 14-1-3 10:40
 */
public class DegreeFunctionTest {
    @Test
    public void test() {
        Function function = new DegreeFunction();

        Map<String, Double> params = new HashMap<String, Double>();
        params.put("a", 2D);
        params.put("b", 3.5D);


        Double result = function.compute(15.5D, params);
        Assert.assertEquals(45, result, 0);

        result = function.compute(-2D, params);
        Assert.assertEquals(0, result, 0);
    }
}
