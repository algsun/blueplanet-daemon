package com.microwise.msp.hardware.handler.formula;

import com.microwise.msp.hardware.businessbean.Formula;

import java.util.Map;

/**
 * 反函数. 通过结果计算原始值
 *
 * @author gaohui
 * @date 14-2-26 17:25
 */
public interface ReverseFunction {

    /**
     * 通过结果值计算原始值，如果无法计算原始值，抛出异常
     *
     * @param target        结果值
     * @param formula
     * @param formulaParams
     * @return
     * @throws java.lang.IllegalStateException
     */
    double compute(double target, Formula formula, Map<String, Double> formulaParams);
}
