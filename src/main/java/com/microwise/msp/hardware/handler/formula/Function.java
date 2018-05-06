package com.microwise.msp.hardware.handler.formula;

import java.util.Map;

/**
 * 方程
 *
 * @author gaohui
 * @date 14-1-2 16:00
 */
public interface Function {

    /**
     * 通过 x 计算结果
     *
     * @param x
     * @param params
     * @return
     */
    public Double compute(Double x, Map<String, Double> params);
}
