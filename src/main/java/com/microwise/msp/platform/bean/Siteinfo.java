/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;

/**
 * <pre>
 * 当前运行站点的信息
 * 
 * 何明明 2012-3-29 
 * 删除了m_systeminfoconfig表,
 * 修改为从mapping_area_site获取：
 * isCurrentSite=1为当前运行站点||isActive=1为加入网络
 * </pre>
 * 
 * @author heming
 * @since 2011-11-30
 */
public class Siteinfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 站点 唯一标识 （接入点号）
	 */
	public String siteid;
	/**
	 * <pre>
	 * 站点 or 机构名称==orgName
	 * 比如：
	 * 陕西历史博物馆(为文博监测站)
	 * 西安文物保护修复中心(为区域监测中心)
	 * </pre>
	 */
	public String siteName;

	/**
	 * 目标 ip地址
	 */
	public String targetHost;

	/**
	 * <pre>
	 * 当前站点是否加入网络
	 * 1加入网络(默认值)
	 * 0未加入网络
	 * </pre>
	 */
	public int isActive = 1;

	/**
	 * <pre>
	 * 是否当前运行站点
	 * </pre>
	 */
	public int isCurrentSite = 0;
}
