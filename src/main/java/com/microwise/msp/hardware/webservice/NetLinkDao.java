package com.microwise.msp.hardware.webservice;

/**
 * 网络管理WebService接口
 *
 * TODO 与 NetManageDao 冗余 @gaohui 2013-08-13 -->
 * @author he.ming
 * @since May 13, 2013
 * 
 */
public interface NetLinkDao {

	/**
	 * 添加通讯端口并启动监听
	 * 
	 * @param model
	 *            通讯模式
	 * @param lport
	 *            本地端口号
	 * @return
	 */
	public boolean addCommunicationInterface(int model, int lport);

	/**
	 * 删除通讯端口并关闭监听
	 * 
	 * @param port
	 *            端口号
	 * @param model
	 *            通讯模式
	 * @return
	 */
	public boolean deleteCommunicationInterface(int port, int model);

	/**
	 * 是否有相同的通讯端口
	 * 
	 * @param port
	 * @param model
	 * @return
	 */
	public boolean hasSamePort(int port, int model);

	/**
	 * 获取所有通讯端口
	 */
	public String getCommunications();

}
