package com.microwise.msp.hardware.handler.formula;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 14-1-6 20:25
 */
public class TwoLinearFunctionTest {

    @Test
    public void test(){
        Function function = new TwoLinearFunction();

        Map<String, Double> params = new HashMap<String, Double>();
        params.put("a", 2D);
        params.put("b", 3.4D);
        params.put("c", 5.6D);
        params.put("d", 7.8D);
        params.put("e", 200D);


        Double result = function.compute(100D, params);
        Assert.assertEquals(203.4D, result, 0);

        result = function.compute(300D, params);
        Assert.assertEquals(1687.8, result, 0);
    }

}
