package com.microwise.msp.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Enumeration 工具类
 *
 * @author bastengao
 * @date 12-11-26 14:07
 * @check @wang.yunlong  & li.jianfei # 502 2012-12-18
 */
public class Enumerations {

    /**
     * 将 Enumeration 转为 list
     *
     * @param enumeration
     * @param <T>
     * @return
     * @gaohui //TODO What does it mean???
     * @date 2012-11-26
     */
    public static <T> List<T> toList(Enumeration<T> enumeration) {
        List<T> list = new ArrayList<T>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }

        return list;
    }
}
