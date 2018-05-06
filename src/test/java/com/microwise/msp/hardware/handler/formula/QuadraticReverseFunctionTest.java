package com.microwise.msp.hardware.handler.formula;

import com.microwise.msp.hardware.businessbean.Formula;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by basten on 2/27/14.
 */
public class QuadraticReverseFunctionTest {

    @Test
    public void computeAis0() {
        ReverseFunction function = new QuadraticReverseFunction();

        Map<String, Double> params = new HashMap<String, Double>();
        params.put("a", 0D);
        params.put("b", 1.2D);
        params.put("c", 2.1D);


        double origin = function.compute(3.3D, new Formula(), params);

        Assert.assertEquals(1, (int) origin);
    }

    @Test
    public void compute() {
        ReverseFunction function = new QuadraticReverseFunction();

        Map<String, Double> params = new HashMap<String, Double>();
        params.put("a", 1D);
        params.put("b", 1.2D);
        params.put("c", 2.1D);


        double origin = function.compute(4.3D, new Formula(), params);

        Assert.assertEquals(1, (int) origin);
    }
}
