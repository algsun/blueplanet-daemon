package com.microwise.msp.hardware.handler.formula;

import com.google.common.base.Preconditions;
import com.microwise.msp.hardware.businessbean.Formula;
import com.microwise.msp.util.Maths;

import java.util.Map;

/**
 * @author gaohui
 * @date 14-2-26 17:29
 */
public class LinearReverseFunction implements ReverseFunction {

    @Override
    public double compute(double target, Formula formula, Map<String, Double> params) {
        Preconditions.checkNotNull(params);
        Preconditions.checkArgument(params.size() >= 2);

        if (!params.containsKey("a") || !params.containsKey("b")) {
            throw new IllegalArgumentException("参数不匹配");
        }

        Double a = params.get("a");
        Double b = params.get("b");

        double candidate =  Maths.roundToDouble((target - b) / a, 1);
        return OriginValueFilter.finalize(new double[]{candidate}, formula);
    }
}
