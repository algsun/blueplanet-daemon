package com.microwise.msp.hardware.handler.formula;

import com.google.common.base.Preconditions;

import java.util.Map;

/**
 * 一元一次分组
 *
 * @author gaohui
 * @date 14-1-2 15:56
 */
public class TwoLinearFunction implements Function {

    @Override
    public Double compute(Double x, Map<String, Double> params) {
        Preconditions.checkNotNull(params);

        if (params.size() < 5) {
            // TODO 参数不满足
            return null;
        }

        if (!params.containsKey("a")
                || !params.containsKey("b")
                || !params.containsKey("c")
                || !params.containsKey("d")
                || !params.containsKey("e")) {
            // TODO 参数不满足
            return null;
        }

        Double a = params.get("a");
        Double b = params.get("b");
        Double c = params.get("c");
        Double d = params.get("d");
        Double e = params.get("e");

        if (x <= e) {
            return a * x + b;
        } else {
            return c * x + d;
        }
    }
}
