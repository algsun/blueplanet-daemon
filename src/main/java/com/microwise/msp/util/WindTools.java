package com.microwise.msp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 环境监测系统工具类
 * 1.风向转换(由度数转为风向英文标识) updateWindDirection()
 * </pre>
 * 
 * @author he.ming
 * @date Sep 14, 2012
 */
public class WindTools {

	/** 监测指标风向类型标识 */
	public static Integer SENSORINFO_SHOWTYPE_IS_WIND_DIRECTION = 1;

	/**
	 * 所有风向标识
	 */
	private static List<String> wDirList = new ArrayList<String>();

	static {
		// N NNE NE ENE E ESE SE SSE S SSW SW WSW W WNW NW NNW
		wDirList.add("N");
		wDirList.add("NNE");
		wDirList.add("NE");
		wDirList.add("ENE");
		wDirList.add("E");
		wDirList.add("ESE");
		wDirList.add("SE");
		wDirList.add("SSE");
		wDirList.add("S");
		wDirList.add("SSW");
		wDirList.add("SW");
		wDirList.add("WSW");
		wDirList.add("W");
		wDirList.add("WNW");
		wDirList.add("NW");
		wDirList.add("NNW");
	}

	private WindTools() {
	}

	/**
	 * 风向转换,度数>>风向标识
	 * 
	 * @param windDirection
	 *            风向(度数) 范围：0~337.5，具体可查看公式脚本
	 * @return 风向标识
	 * @author he.ming
	 * @date Sep 14, 2012
	 */
	public static String updateWindDirection(final String windDirection) {
		if (windDirection == null || "".equals(windDirection)) {
			return "";
		}
		String windDir = "";
		final double radiusRound = 360.0;
		final int wDirs = 16;
		final double lip = radiusRound / wDirs; // 一个坐标区共有多少度
		double wDirection;
		try {
			wDirection = Double.parseDouble(windDirection);
			Double section = wDirection / lip; //
			int index = section.intValue(); // 取整数,判断是在哪个坐标内
			if (index > -1 && index < wDirs) { // 验证序号有效性
				windDir = wDirList.get(index);
			}
		} catch (Exception e) {
			System.err.println("/n/n EmTools>>updateWindDirection() /n/n");
		}
		return windDir;

	}

}
