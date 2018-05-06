package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.vo.NetInfo;

import java.util.List;

/**
 * 网络管理dao
 * TODO 与 NetLink 冗余 @gaohui 2013-08-13 -->
 *
 * @author heming
 * 
 */
public interface NetManagerDao extends BaseDao {

    /**
     * 添加监听
     *
     * @param port
     * @param model
     * @param siteId
     */
    void addNetInfo(int port, int model, String siteId);

    /**
	 * 检查系统是否已经配置了相同的本地监听端口
	 * 
	 * @param lport
	 *            本地端口
	 * @return
	 * 
	 * @author xuexu
	 */
	boolean hasSameLocalPort(int lport);

	/**
	 * 删除一条接口记录
	 * 
	 * @param netInfo
	 *            当前model索引
	 * @author xuexu
	 */
	boolean deleteRecord(NetInfo netInfo);

	/**
	 * 更新通讯状态
	 * 
	 * @param id
	 *            主键
	 * @param state
	 *            状态
	 * @author xuexu
	 */
	boolean updateCommState(String id, int state);

	/**
	 * 获取所有通讯接口
	 * 
	 * @author xuexu
	 */
	List<NetInfo> getCommunications();

    List<NetInfo> findByType(int type);

    /**
	 * 通过id获取通讯接口
	 * 
	 *
     * @param id
     *            主键
     * @author xuexu
	 */
	NetInfo getCommunicationByID(int id);

	/**
	 * 获取通讯状态
	 * 
	 * @param id
	 *            主键
	 * @return
	 * 
	 * @author xuexu
     * @deprecated
	 */
	int getConnState(String id);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void deleteById(int id);
}
