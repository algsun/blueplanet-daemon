package com.microwise.msp.hardware.handler.formula;

import com.microwise.msp.hardware.businessbean.Formula;

import java.util.ArrayList;
import java.util.List;

/**
 * 原始值过滤器
 *
 * @author gaohui
 * @daet 2014-02-27
 */
public class OriginValueFilter {

    /**
     * 根据原始值的范围和公式的约束从候选值中挑选一个最适合的结果
     * <p/>
     * 如果没有合适的结果抛出异常
     *
     * @param candidates
     * @param formula
     * @return
     */
    public static double finalize(double[] candidates, Formula formula) {

        if (candidates == null) {
            throw new IllegalArgumentException();
        }

        List<Double> availableOrigins = new ArrayList<Double>();
        for (double originValue : candidates) {
            if (isMatchRange(originValue, formula)) {
                availableOrigins.add(originValue);
            }
        }

        if (availableOrigins.isEmpty()) {
            throw new IllegalStateException("解不满足范围");
        }

        int suiteTimes = 0;
        double suiteOriginValue = 0;
        for (double originValue : availableOrigins) {
            // x 超范围
            if (formula.isXMinEnable() && originValue < formula.getMinX()) {
                continue;
            }
            if (formula.isXMaxEnable() && originValue > formula.getMaxX()) {
                continue;
            }

            suiteOriginValue = originValue;
            suiteTimes++;
        }

        if (suiteTimes != 1) {
            throw new IllegalStateException("无解或者超过一个解");
        }


        return suiteOriginValue;
    }

    /**
     * 是否匹配原始值范围
     *
     * @param originValue
     * @param formula
     * @return
     */
    public static boolean isMatchRange(double originValue, Formula formula) {
        if (formula.getSignType() == Formula.SIGN_TYPE_NO) {
            if (originValue < 0 || originValue > 65535) {
                return false;
            }
        } else if (formula.getSignType() == Formula.SIGN_TYPE_YES) {
            if (originValue < -32768 || originValue > 32767) {
                return false;
            }
        }

        return true;
    }
}
