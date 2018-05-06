/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;

/**
 * <pre>
 * 区域_接入点 对应表 - Mapping_area_site
 * </pre>
 * 
 * @author heming
 * @since 2011-11-02
 */
public class Mapping_area_site implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int id;

	/**
	 * 1：国家监测中心 2：区域监测中心 3：文博监测站
	 */
	public int levels;
	/**
	 * 站点唯一标识 （接入点号）
	 */
	public String siteid;
	/**
	 * 父站点唯一标识 （父站点的接入点号）
	 */
	public String p_siteid;
	/**
	 * 站点名，比如：西安文物保护修复中心(为区域监测中心)
	 */
	public String siteName;
	/**
	 * 站点ip地址
	 */
	public String siteip;
	/**
	 * 备注
	 */
	public String remark;
	/**
	 * 数据 版本号
	 */
	public long dataVersion;

}
