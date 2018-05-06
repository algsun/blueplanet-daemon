package com.microwise.msp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数据工具类
 *
 * @author gaohui
 * @date 13-8-2 13:19
 */
public class Maths {

    /**
     * 四舍五入
     *
     * @param value 值
     * @param precision 保留几位小数点
     * @return
     */
    public static BigDecimal round(double value, int precision){
        return BigDecimal.valueOf(value).setScale(precision, RoundingMode.HALF_UP);
    }

    /**
     * 四舍五入
     *
     * @param value 值
     * @param precision 保留几位小数点
     * @return
     */
    public static double roundToDouble(double value, int precision){
        return round(value, precision).doubleValue();
    }
}
