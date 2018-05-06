package com.microwise.msp.hardware.handler.codec.v30;

/**
 * @author gaohui
 * @date 14-2-28 10:52
 */
public class Requests {

    /**
     * 根据指令编号返回对应名称
     *
     * @param orderId
     * @return
     */
   public static String requestName(int orderId){
       switch (orderId){
           case 1: return "keep alive";
           case 2: return "get time";
           case 3: return "report time";
       }

       return "";
   }
}
