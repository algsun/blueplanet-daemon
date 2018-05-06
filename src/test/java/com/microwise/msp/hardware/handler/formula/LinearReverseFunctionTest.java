package com.microwise.msp.hardware.handler.formula;

import com.microwise.msp.hardware.businessbean.Formula;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 14-2-26 17:35
 */
public class LinearReverseFunctionTest {

    @Test
    public void compute() {
        ReverseFunction function = new LinearReverseFunction();

        Map<String, Double> params = new HashMap<String, Double>();
        params.put("a", 1.2D);
        params.put("b", 2.1D);

        double origin = function.compute(3.3D, new Formula(), params);

        Assert.assertEquals(1, (int) origin);
    }
}
