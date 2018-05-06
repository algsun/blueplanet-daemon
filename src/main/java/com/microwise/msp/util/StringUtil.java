/**
 *
 */
package com.microwise.msp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * <pre>
 *  工具
 * ①fillZero(),    针对整型数值转一定长度的字符串前补零
 * ②genSelfIP(),   根据子网+节点ID生成5位IP号
 * ③uuid(),        生成uuid
 * ④nowTimestamp() 获取当前时间戳
 * ⑤toHex()        将一个字节转为16进制字符
 *
 * </pre>
 *
 * @author heming
 * @since 2011-12-20
 */
public class StringUtil {

    private static Logger log = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 针对整型数值【srcValue】
     * 根据长度【length】前补零为十进制的字符串
     * TODO 数值超过长度会溢出 @gaohui 2014-04-04
     * <p/>
     * 2012年4月1日 何明明
     *
     * @param srcValue 原始值
     * @param length   长度(十进制数值转字符串)
     * @return
     */
    public static String fillZero(int srcValue, int length) {
        return String.format("%0" + length + "d", srcValue);
    }

    /**
     * <pre>
     * 根据子网netid+终端节点号selfid,生成v3协议中的IP号
     * </pre>
     *
     * @param netid  子网
     * @param selfid 终端节点ID
     * @return 生成5位的IP号
     * @author heming 2012年3月31日
     */
    public static String genSelfIP(byte netid, byte selfid) {
        int tempI = MergeUtil.merge2((char) netid, (char) selfid); // 子网+节点ID
        return fillZero(tempI, 5);
    }

    /**
     * 1.3子网转为十进制2位字符串
     *
     * @param netid netid<100
     * @return
     * @author he.ming
     * @since Mar 12, 2013
     */
    public static String netIdTo2(byte netid) {
        return fillZero(netid & 0xFF, 2);
    }

    public static String netIdTo2(int netid) {
        return fillZero(netid, 2);
    }

    /**
     * 1.3节点号转为十进制3位字符串
     *
     * @param selfid selfid<256
     * @return
     * @author he.ming
     * @since Mar 12, 2013
     */
    public static String selfIdTo3(byte selfid) {
        return fillZero(selfid & 0xFF, 3);
    }

    public static String selfIdTo3(int selfid) {
        return fillZero(selfid, 3);
    }

    /**
     * <pre>
     * 获取当前时间戳
     * </pre>
     *
     * @return 当前时间戳
     * @author heming
     * @since 2012-03-29
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 字节List转为String
     *
     * @param list
     * @return
     * @author he.ming
     * @since Feb 26, 2013
     */
    public static String bytesToString(List<Byte> list) {
        int size = list.size();
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = list.get(i);
        }
        return new String(byteArray);
    }

    /**
     * 字节List转为int
     *
     * @param list
     * @return
     * @author he.ming
     * @since Feb 26, 2013
     */
    public static int bytesToInt(List<Byte> list) {
        int size = list.size();
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = list.get(i);
        }
        return Integer.parseInt(new String(byteArray));
    }

    /**
     * String to byteList
     *
     * @param strs 字符串
     * @return
     * @author he.ming
     * @since Feb 26, 2013
     */
    public static List<Byte> stringToBytes(String strs) {
        List<Byte> rlist = new ArrayList<Byte>();
        byte[] byteArray = strs.getBytes();
        for (byte b : byteArray) {
            rlist.add(b);
        }
        return rlist;
    }

    /**
     * int to byteList
     *
     * @param value 整形数值
     * @return
     * @author he.ming
     * @since Feb 26, 2013
     */
    public static List<Byte> intToBytes(int value) {
        List<Byte> rlist = new ArrayList<Byte>();
        byte[] byteArray = (Integer.toString(value)).getBytes();
        for (byte b : byteArray) {
            rlist.add(b);
        }
        return rlist;
    }

    /**
     * <pre>
     * 生成UUID
     * UUID(Universally Unique Identifier)全局唯一标识符
     * </pre>
     *
     * @return
     * @author heming 2012年3月31日
     */
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        // 去掉中间的 "-", 节省四个字符
        return uuid.toString().replaceAll("-", "");
    }

    /**
     * <p>
     * 将一字节转换为无符号整形 <br>
     * 比如：12 => 12, -86 => 171
     * </p>
     *
     * @param b
     * @return
     */
    public static int toUnsignedInt(byte b) {
        return 0xFF & b;
    }

    /**
     * <p>
     * 将两位16进制字符转换为一个字节<br>
     * 比如 16进制"55" 转换为 "85"
     * </p>
     *
     * @param s
     * @return
     */
    public static final byte fromHex(String s) {
        return (byte) Integer.parseInt(s.substring(0, 2), 16);
    }

    /**
     * 将16进制字符串转换为字节数组
     *
     * @param s
     * @return
     */
    public static final byte[] fromHexs(String s) {
        int length = s.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("十六进制字符串长度不是偶数");
        }
        int byteLength = length / 2;
        byte[] bytes = new byte[byteLength];
        for (int i = 0; i < byteLength; i++) {
            bytes[i] = fromHex(s.substring(i * 2, i * 2 + 2));
        }
        return bytes;
    }

    /**
     * <pre>
     * 将1个字节转为16进制的字符
     * </pre>
     *
     * @param b 16进制的1个字节内容
     * @return
     */
    public static final String toHex(byte b) {
        return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF"
                .charAt(b & 0xf));
    }

    /**
     * 将字节序列格式化为16进制字符串,比如"55AA0100"
     *
     * @param bytes
     * @return
     * @since 2012-05-17
     */
    public static final String toHex(Iterable<Byte> bytes) {
        StringBuilder sb = new StringBuilder();
        for (Byte b : bytes) {
            sb.append(StringUtil.toHex(b));
        }
        return sb.toString();
    }

    /**
     * 将字节序列格式化为16进制字符串,比如"55AA0100"
     *
     * @param bytes
     * @return
     * @since 2012-05-17
     */
    public static final String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (Byte b : bytes) {
            sb.append(StringUtil.toHex(b));
        }
        return sb.toString();
    }

    /**
     * 将字节序列格式化为16进制字符串，字节间空格隔开,比如"55 AA 01 00"
     *
     * @param bytes
     * @return
     * @since 2012-05-17
     */
    public static String toHexWithBlank(Collection<Byte> bytes) {
        StringBuilder sb = new StringBuilder();
        for (Byte b : bytes) {
            sb.append(StringUtil.toHex(b) + " ");
        }
        return sb.toString();
    }

    /**
     * 字符转为double
     *
     * @param str 字符
     * @return
     * @author he.ming
     * @since Feb 26, 2013
     */
    public static double stringToDouble(String str) {
        double rParam = 0.0d;
        try {
            rParam = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            rParam = 0.0d;
            log.error("\n\n" + e.getMessage() + "\n\n");
        }
        return rParam;
    }

    /**
     * 根据精度四舍五入
     *
     * @param value 传入的数值
     * @param scale 保留的精度
     * @return
     */
    public static String round(double value, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        BigDecimal b = BigDecimal.valueOf(value);
        BigDecimal one = new BigDecimal("1");
        String strvalue = b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
        return subZeroAndDot(strvalue);
    }

    public static String integerList2String(List<Integer> list) {
        if (list == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : list) {
            if (i != null) {
                sb.append(",").append(i);
            }
        }
        return sb.toString().substring(1);
    }

}
