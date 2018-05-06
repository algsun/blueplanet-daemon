package com.microwise.msp.hardware.businessservice;

import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 13-8-9 09:41
 */
public class ScriptServiceTest {
    @Test
    public void testGetInstance() {
        ScriptService scriptService = ScriptService.getScriptService();
        Assert.assertNotNull(scriptService);
    }

    @Test
    public void testCallFuncGeneralCompute() throws ScriptException, NoSuchMethodException {
        ScriptService scriptService = ScriptService.getScriptService();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("a", 1D);
        params.put("b", 2D);
        params.put("c", 3D);
        Object result = scriptService.callFunc("generalCompute", 4, params);
        Assert.assertNotNull(result);
        Assert.assertSame(Double.class, result.getClass());

        Double value = (Double) result;
        Assert.assertEquals(27, value, 0);
    }

    // 方法不存在
    @Test(expected = NoSuchMethodException.class)
    public void testCallFunc() throws ScriptException, NoSuchMethodException {
        ScriptService scriptService = ScriptService.getScriptService();
        scriptService.callFunc("i_am_gone");
    }

    @Test
    public void testGet32() throws ScriptException, NoSuchMethodException {
        ScriptService scriptService = ScriptService.getScriptService();

        Map<String, Double> params = new HashMap<String, Double>();
        params.put("a", -0.0000028D);
        params.put("b", 0.0405D);
        params.put("c", -4D);

        Double result = scriptService.doMath(32, 0D, params);
        Assert.assertEquals(-4D, result, 0);
    }

    @Test
    public void testGet43() {
        ScriptService scriptService = ScriptService.getScriptService();

        Map<String, Double> params = new HashMap<String, Double>();
        params.put("a", -0.0000028D);
        params.put("b", 0.0405D);
        params.put("c", -4D);

        Double result = scriptService.doMath(43, 1D, params);
        Assert.assertEquals(1D, result, 0);
    }

    @Test
    public void isLowVoltage() {
        ScriptService scriptService = ScriptService.getScriptService();
        Assert.assertFalse(scriptService.isLowVoltage(1, 10.5f));

        Assert.assertTrue(scriptService.isLowVoltage(2, 10.4f));
        Assert.assertTrue(scriptService.isLowVoltage(3, 8f));

        Assert.assertFalse(scriptService.isLowVoltage(4, 7.9f));
        Assert.assertFalse(scriptService.isLowVoltage(1, 3.5f));


        Assert.assertTrue(scriptService.isLowVoltage(7, 3.5f));
        Assert.assertFalse(scriptService.isLowVoltage(7, 3.6f));
    }
}
