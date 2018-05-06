package com.microwise.msp.hardware.handler.formula;

import com.google.common.base.Preconditions;

import java.util.Map;

/**
 * 规整角度
 *
 * @author gaohui
 * @date 14-1-2 16:11
 */
public class DegreeFunction implements Function {

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

        //  先计算结果 一元两次方程 @gaohui 2014-01-04
        double d = a * x + b;

        Double degree = d;
        if (degree > 11.25 && degree <= 33.75) {
            degree = 22.5;
        } else if (degree > 33.75 && degree <= 56.25) {
            degree = 45D;
        } else if (degree > 56.25 && degree <= 78.75) {
            degree = 67.5;
        } else if (degree > 78.75 && degree <= 101.25) {
            degree = 90D;
        } else if (degree > 101.25 && degree <= 123.75) {
            degree = 112.5;
        } else if (degree > 123.75 && degree <= 146.25) {
            degree = 135D;
        } else if (degree > 146.25 && degree <= 168.75) {
            degree = 157.5;
        } else if (degree > 168.75 && degree <= 191.25) {
            degree = 180D;
        } else if (degree > 191.25 && degree <= 213.75) {
            degree = 202.5;
        } else if (degree > 213.75 && degree <= 236.25) {
            degree = 225D;
        } else if (degree > 236.25 && degree <= 258.75) {
            degree = 247.5;
        } else if (degree > 258.75 && degree <= 281.25) {
            degree = 270D;
        } else if (degree > 281.25 && degree <= 303.75) {
            degree = 292.5;
        } else if (degree > 303.75 && degree <= 326.25) {
            degree = 315D;
        } else if (degree > 326.25 && degree <= 348.75) {
            degree = 337.5;
        } else {
            degree = 0D;
        }

        return degree;
    }
}
