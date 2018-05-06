package com.microwise.msp.util;

import java.math.BigInteger;

/**
 * <pre>
 * 字节合并工具类
 * 
 * ①【merge2】：2个字节合并,最大【65535】
 * ②【merge3】：3个字节合并,最大【16777215】
 * ③【merge4】：4个字节合并,十进制int最大处理【16777215】→十六进制【0x00FFFFFF】
 * </pre>
 * 
 * @author heming
 * @since 2012年4月1日
 */
public class MergeUtil {

	/**
	 * <pre>
	 * 数据包中取出的两个字节【高位+低位】 
	 * 组合成为一个数值
	 * </pre>
	 * 
	 * @param high
	 *            接收数值的高位
	 * @param low
	 *            接收数值的低位
	 * @return
	 */
	public static int merge2(char high, char low) {
		return ((((0x000000ff & high) << 8) & 0x0000ff00) | (0x000000ff & low));
	}

	/**
	 * <pre>
	 * 数据包中的3个字节进行合并
	 * 组合成为一个数值
	 * </pre>
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * 
	 * @return
	 */
	public static int merge3(int a, int b, int c) {
		return (((0x000000ff & a) << 16) & 0x00ff0000)
				| (((0x000000ff & b) << 8) & 0x0000ff00) | (0x000000ff & c);
	}

	/**
	 * <pre>
	 * 数据包中的4个字节进行合并
	 * 组合成为一个数值
	 * 【特别注意】： merge4将4个字节中的最高位限制【7F】
	 *  【0x7FFFFFFF】
	 * </pre>
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * 
	 * @return
	 */
	public static int merge4(byte[] byteArray) {
		BigInteger bigNum = new BigInteger(byteArray);
		int intNum = bigNum.intValue();
		return intNum;
	}

	public static void main(String[] args) {
		// System.out.println(merge4(255, 255, 255, 255));

		byte[] b = { (byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff }; // big-endian

		System.out.println(merge4(b));

	}

}
