/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;

/**
 * <pre>
 * 接入点_节点 对应表 - Mapping_site_node
 * </pre>
 * 
 * @author heming
 * @since 2011-11-02
 */
public class Mapping_site_node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int id;
	/**
	 * 站点唯一标识（接入点唯一标识）
	 */
	public String siteid;
	/**
	 * 产品入网唯一标识
	 */
	public String nodeid;
	/**
	 * 备注
	 */
	public String remark;
	/**
	 * 数据 版本号
	 */
	public long dataVersion;

}
