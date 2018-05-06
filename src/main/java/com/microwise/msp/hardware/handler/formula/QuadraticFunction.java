package com.microwise.msp.hardware.handler.formula;

import java.util.Map;

/**
 * 一次两次方程
 *
 * @author gaohui
 * @date 14-1-2 15:56
 */
public class QuadraticFunction implements Function {
    @Override
    public Double compute(Double x, Map<String, Double> params) {
        if (params.size() < 3) {
            // TODO 参数不满足
            return null;
        }

        if (!params.containsKey("a") || !params.containsKey("b") || !params.containsKey("c")) {
            // TODO 参数不满足
            return null;
        }
        Double a = params.get("a");
        Double b = params.get("b");
        Double c = params.get("c");

        return a * Math.pow(x, 2) + b * x + c;
    }
}
