package com.microwise.msp.hardware.handler.formula;

import java.util.Map;

/**
 * 一次三次方程
 *
 * @author gaohui
 * @date 14-1-2 15:59
 */
public class CubicFunction implements Function {
    @Override
    public Double compute(Double x, Map<String, Double> params) {
        if (params.size() < 4) {
            // TODO 参数不满足
            return null;
        }

        if (!params.containsKey("a") || !params.containsKey("b") || !params.containsKey("c") || !params.containsKey("d")) {
            // TODO 参数不满足
            return null;
        }

        Double a = params.get("a");
        Double b = params.get("b");
        Double c = params.get("c");
        Double d = params.get("d");

        return a * Math.pow(x, 3) + b * Math.pow(x, 2) + c * x + d;
    }
}
