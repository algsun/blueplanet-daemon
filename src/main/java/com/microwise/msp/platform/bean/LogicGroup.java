package com.microwise.msp.platform.bean;

import java.io.Serializable;

public class LogicGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * logicGroupId编号
	 */
	public Integer id;

	/**
	 * logicGroup名称
	 */
	public String logicGroupName;

	/**
	 * 站点唯一标识
	 */
	public String siteId;

	/**
	 * 父级logicGroup
	 */
	public Integer parentLogicGroupId;

	/**
	 * 机构代码
	 */
	public String orgCode;

	/**
	 * 地址
	 */
	public String orgAddress;

	/**
	 * 邮编
	 */
	public String orgZipcode;

	/**
	 * 网址
	 */
	public String orgWebsite;

	/**
	 * 联系电话
	 */
	public String orgTel;

	/**
	 * 传真
	 */
	public String orgFax;

	/**
	 * 1-未激活；2-待激活；3-已激活。
	 */
	public Integer activeState;

	/**
	 * 1本实例创建；2别的tomcat实例同步上来的。
	 */
	public Integer logicGroupType;

	/**
	 * 数据同步版本
	 */
	public long dataVersion;

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getLogicGroupName() {
		return logicGroupName;
	}

	public void setLogicGroupName(String logicGroupName) {
		this.logicGroupName = logicGroupName;
	}
}
