package com.microwise.msp.util;


import com.google.gson.Gson;

/**
 * @author xiedeng
 * @date 14-12-8
 */
public class JsonUtil {

    public static String toJson(Object value) {
        Gson gson = new Gson();
        return gson.toJson(value);
    }

}
