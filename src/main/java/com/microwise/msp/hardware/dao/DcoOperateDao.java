package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.vo.Order;

import java.util.List;
import java.util.Map;

/**
 * 反控dao
 * 
 * @author he.ming
 * @since 2013-1-10
 */
public interface DcoOperateDao extends BaseDao {

    /**
     * 查询站点下所有网络下的根节点
     *
     * @param siteId
     * @return
     */
    List<String> findRootNodeIdsBySiteId(String siteId);

	/**
	 * 获取网关ID
	 * 
	 * @param deviceBean
	 * @return
	 * @author he.ming
	 * @since Feb 21, 2013
	 */
	int getGateWayByDeviceBean(DeviceBean deviceBean);


	/**
	 * 根据childId获取父节点Id和节点类型
	 * 
	 * @param childId
	 *            自身IP
	 * @param siteId
	 *            接入点
	 * @return
	 */
	boolean getParent(int childId, String siteId, Map<String,Integer> devInfo);

	/**
	 * 是否存在相同的命令
	 * 
	 * @param order
	 *            指令包信息
	 * @return
	 */
	boolean isSameOrder(Order order);

	/**
	 * 存储指令包
	 * 
	 * @param order
	 *            指令包信息
	 * @return
	 */
	boolean storageOrder(Order order);

	/**
	 * 更新指令包
	 * 
	 * @param order
	 * @return
	 */
	boolean updateOrder(Order order);

	/**
	 * 获取异常命令列表
	 * 
	 * @return
	 */
	List<Order> getExceptionOrders();

	/**
	 * 获取命令属性
	 * 
	 * @param orderId
	 * @return
	 */
	Order getOrderPropertyFromDB(int orderId);

	/**
	 * 是否存在有效命令
	 * 
	 * @param orderSerial
	 *            命令序列号
	 * @return
	 */
	int isActiveOrder(String orderSerial);

	/**
	 * 修改命令执行的反馈码[01成功/02失败/03送达成功/FF路径不通]
	 * 
	 * @param orderId
	 *            命令Id
	 * @param currentState
	 *            命令反馈码
	 */
	void updateState(int orderId, int currentState);

	/**
	 * 设置命令失效
	 * 
	 * @param orderId
	 */
	boolean setOrderInvalid(int orderId);

	/**
	 * 获取下行命令的目标ID
	 * 
	 * @param orderId
	 *            命令Id
	 * @return
	 */
	String getSelfIdByOrderId(int orderId);

	/**
	 * 更新接收数据次数
	 * 
	 * @param order
	 */
	void updateDataReciveCount(Order order);
}
