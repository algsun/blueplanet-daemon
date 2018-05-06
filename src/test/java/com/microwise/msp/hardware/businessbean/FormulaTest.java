package com.microwise.msp.hardware.businessbean;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author gaohui
 * @date 14-1-2 17:38
 */
public class FormulaTest {
    @Test
    public void testRangeType() {
        Formula formula = new Formula();
        Assert.assertFalse(formula.isXMinEnable());
        Assert.assertFalse(formula.isXMaxEnable());

        formula.setxRangeType(Formula.RANG_TYPE_MIN);
        Assert.assertTrue(formula.isXMinEnable());

        formula.setxRangeType(Formula.RANG_TYPE_MAX);
        Assert.assertTrue(formula.isXMaxEnable());

        formula.setxRangeType(Formula.RANG_TYPE_BOTH);
        Assert.assertTrue(formula.isXMinEnable());
        Assert.assertTrue(formula.isXMaxEnable());

        formula.setyRangeType(Formula.RANG_TYPE_MIN);
        Assert.assertTrue(formula.isYMinEnable());

        formula.setyRangeType(Formula.RANG_TYPE_MAX);
        Assert.assertTrue(formula.isYMaxEnable());

        formula.setyRangeType(Formula.RANG_TYPE_BOTH);
        Assert.assertTrue(formula.isYMinEnable());
        Assert.assertTrue(formula.isYMaxEnable());
    }
}
