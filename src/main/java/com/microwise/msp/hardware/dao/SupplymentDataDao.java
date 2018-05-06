package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.EmptyDataBean;

import java.util.List;

public interface SupplymentDataDao extends BaseDao {

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	List<EmptyDataBean> getDataCacheEmptyInfo(int maxSupplyCount);

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	List<EmptyDataBean> getGatewayEmptyInfo(int maxSupplyCount);

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	public EmptyDataBean getEmptyInfo(EmptyDataBean emptyDataBean);

	/**
	 * 判断空数据记录是否存在
	 * 
	 * @param emptyDataBean
	 *            空数据记录
	 * @return 操作结果 true 存在, false 不存在
	 */
	Boolean isExistEmptyData(EmptyDataBean emptyDataBean);

	/**
	 * 修改网关空数据记录
	 * 
	 * @param emptyDataBean
	 *            修改空数据记录
	 * @return 操作结果 true 成功 , false 失败
	 */
	Boolean updateGatewaySign(EmptyDataBean emptyDataBean);

	/**
	 * 修改缓存中心空数据记录
	 * 
	 * @param emptyDataBean
	 *            修改空数据记录
	 * @return 操作结果 true 成功 , false 失败
	 */
	Boolean updateCacheSign(EmptyDataBean emptyDataBean);

	/**
	 * 删除空数据记录
	 * 
	 * @param id
	 *            空数据记录编号
	 * @return 操作结果 true 成功 , false 失败
     * @deprecated 方法未实现 @gaohui 2013-12-13
	 */
	Boolean deleteEmptyInfo(int id);

	/**
	 * 添加空数据记录
	 * 
	 * @param emptyDataBean
	 *            空数据记录
	 * @return 操作结果 true 成功 , false 失败
	 */
	Boolean insertIntoEmptyInfo(EmptyDataBean emptyDataBean);
}
