package com.microwise.msp.hardware.handler.formula;

import com.microwise.msp.hardware.businessbean.Formula;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by basten on 2/27/14.
 */
public class OriginValueFilterTest {

    @Test
    public void isMatchRange(){
        Formula formula = new Formula();
        formula.setSignType(Formula.SIGN_TYPE_NO);

        Assert.assertTrue(OriginValueFilter.isMatchRange(65535, formula));
        Assert.assertTrue(OriginValueFilter.isMatchRange(0, formula));
        Assert.assertFalse(OriginValueFilter.isMatchRange(65536, formula));
        Assert.assertFalse(OriginValueFilter.isMatchRange(-1, formula));


        formula = new Formula();
        formula.setSignType(Formula.SIGN_TYPE_YES);

        Assert.assertTrue(OriginValueFilter.isMatchRange(32767, formula));
        Assert.assertTrue(OriginValueFilter.isMatchRange(-32768, formula));
        Assert.assertFalse(OriginValueFilter.isMatchRange(32768, formula));
        Assert.assertFalse(OriginValueFilter.isMatchRange(-32769, formula));
    }

}
