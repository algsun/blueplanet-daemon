package com.microwise.msp.hardware.handler.formula;

import com.google.common.base.Preconditions;

import java.util.Map;

/**
 * 一元一次方程
 *
 * @author gaohui
 * @date 14-1-2 15:56
 */
public class LinearFunction implements Function {

    @Override
    public Double compute(Double x, Map<String, Double> params) {
        Preconditions.checkNotNull(params);

        if (params.size() < 2) {
            // TODO 参数不满足
            return null;
        }

        if (!params.containsKey("a") || !params.containsKey("b")) {
            // TODO 参数不满足
            return null;
        }

        Double a = params.get("a");
        Double b = params.get("b");

        return a * x + b;
    }
}
