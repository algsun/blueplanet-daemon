package com.microwise.msp.hardware.handler.formula;

import com.microwise.msp.hardware.businessbean.Formula;
import com.microwise.msp.util.Maths;

import java.util.HashMap;
import java.util.Map;

/**
 * 一元二次方程的反函数
 *
 * @author gaohui
 * @date 2014-02-27
 */
public class QuadraticReverseFunction implements ReverseFunction {

    @Override
    public double compute(double target, Formula formula, Map<String, Double> params) {
        if (params.size() < 3) {
            // 参数不满足
            throw new IllegalArgumentException("参数不足");
        }

        if (!params.containsKey("a") || !params.containsKey("b") || !params.containsKey("c")) {
            throw new IllegalArgumentException("参数不满足");
        }


        Double a = params.get("a");
        Double b = params.get("b");
        Double c = params.get("c");


        // 如果 a 为 0，则转化为一元一次方程
        if (a == 0) {
            Map<String, Double> newParams = new HashMap<String, Double>();
            newParams.put("a", b);
            newParams.put("b", c);
            return new LinearReverseFunction().compute(target, formula, newParams);
        }


        double delta = Math.sqrt((b * b) - (4 * a * (c - target)));

        double origin1 = (int) Maths.roundToDouble((-b + delta) / (2 * a), 1);
        double origin2 = (int) Maths.roundToDouble((-b - delta) / (2 * a), 1);


        return OriginValueFilter.finalize(new double[]{origin1, origin2}, formula);
    }

}
