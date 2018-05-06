package com.microwise.msp.hardware.handler.formula;

import com.microwise.msp.hardware.businessbean.Formula;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 14-1-2 16:35
 */
public class FormulasTest {
    @Test
    public void testToDouble() {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("a", "1234");
        params.put("b", "0.1234");
        params.put("c", "4321.1234");

        Map<String, Double> doubleParams = Formulas.toDouble(params);
        Assert.assertNotNull(doubleParams);
        Assert.assertEquals(3, doubleParams.size());

        Assert.assertEquals(params.keySet(), doubleParams.keySet());
        Assert.assertEquals(1234D, doubleParams.get("a"), 0);
        Assert.assertEquals(0.1234D, doubleParams.get("b"), 0);
        Assert.assertEquals(4321.1234D, doubleParams.get("c"), 0);
    }

    @Test
    public void testCompute(){
        Formula formula = new Formula();
        formula.setId(1);
        formula.setParamCount(2);

        Map<String, String> params = new HashMap<String, String>();
        params.put("a", "1.2");
        params.put("b", "2.1");
        formula.setFormulaParams(params);

        double result = Formulas.compute(1, formula, formula.getFormulaParams());
        Assert.assertEquals(3.3, result, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeException(){
        Formula formula = new Formula();
        formula.setId(100);

        Formulas.compute(1D, formula, null);
    }

    @Test
    public void reverseCompute(){
        Formula formula = new Formula();
        formula.setId(1);
        formula.setParamCount(2);

        Map<String, String> params = new HashMap<String, String>();
        params.put("a", "1.2");
        params.put("b", "2.1");
        formula.setFormulaParams(params);

        double origin = Formulas.reverseCompute(3.3, formula, formula.getFormulaParams());
        Assert.assertEquals(1, origin, 0);
    }
}
