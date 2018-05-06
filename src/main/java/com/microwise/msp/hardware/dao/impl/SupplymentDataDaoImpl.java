package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.EmptyDataBean;
import com.microwise.msp.hardware.dao.SupplymentDataDao;
import com.microwise.msp.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplymentDataDaoImpl extends BaseDaoImpl implements
		SupplymentDataDao {

	private static Logger log = LoggerFactory.getLogger(SupplymentDataDaoImpl.class);

	/**
	 * 删除空数据记录
	 * 
	 * @param id
	 *            空数据记录编号
	 * @return 操作结果 true 成功 , false 失败
	 */
	@Override
	public Boolean deleteEmptyInfo(int id) {
        throw new UnsupportedOperationException();
	}

	/**
	 * 判断空数据记录是否存在
	 * 
	 * @param emptyDataBean
	 *            空数据记录
	 * @return 操作结果 true 存在, false 不存在
	 */
	@Override
	public Boolean isExistEmptyData(EmptyDataBean emptyDataBean) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("nodeid", emptyDataBean.getNodeid());
		String stamp = DateUtils.getDate(emptyDataBean.getStamp(),
				"yyyy-MM-dd HH");
		parms.put("stamp", stamp.concat("%"));
		int count = 0;
		try {
			count = (Integer) getSqlSession().selectOne(
					"SupplymentData.isExistEmptyData", parms);
		} catch (DataAccessException e) {
			log.error("查询空数据记录是否存在(isExistEmptyData)出错", e);
		}
		return count > 0;
	}

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmptyDataBean> getDataCacheEmptyInfo(int maxSupplyCount) {
		List<EmptyDataBean> list = new ArrayList<EmptyDataBean>();
		try {
			list = getSqlSession().selectList("SupplymentData.getDataCacheEmptyInfo", maxSupplyCount);
		} catch (DataAccessException e) {
			log.error("获取空数据记录(getDataCacheEmptyInfo)出错", e);
		}
		return list;
	}

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmptyDataBean> getGatewayEmptyInfo(int maxSupplyCount) {
		List<EmptyDataBean> list = new ArrayList<EmptyDataBean>();
		try {
			list = getSqlSession().selectList("SupplymentData.getGatewayEmptyInfo", maxSupplyCount);
		} catch (DataAccessException e) {
			log.error("获取空数据记录(getEmptyInfo)出错", e);
		}
		return list;
	}

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EmptyDataBean getEmptyInfo(EmptyDataBean emptyDataBean) {
		EmptyDataBean ebn = new EmptyDataBean();
		try {
			ebn = (EmptyDataBean) getSqlSession().selectOne(
					"SupplymentData.getEmptyInfoByNodeid", emptyDataBean);
		} catch (DataAccessException e) {
			log.error("获取空数据记录(getEmptyInfo)出错", e);
		}
		return ebn;
	}

	/**
	 * 修改网关空数据记录
	 * 
	 * @param emptyDataBean
	 *            修改空数据记录
	 * @return 操作结果 true 成功 , false 失败
	 */
	@Override
	public Boolean updateGatewaySign(EmptyDataBean emptyDataBean) {
		int i = 0;
		try {
			i = (Integer) getSqlSession().update(
					"SupplymentData.updateGatewaySign", emptyDataBean);
		} catch (DataAccessException e) {
			log.error("修改网关空数据记录(updateGatewaySign)出错", e);
		}
		if (i > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 修改缓存中心空数据记录
	 * 
	 * @param emptyDataBean
	 *            修改空数据记录
	 * @return 操作结果 true 成功 , false 失败
	 */
	@Override
	public Boolean updateCacheSign(EmptyDataBean emptyDataBean) {
		int i = 0;
		try {
			i = (Integer) getSqlSession().update(
					"SupplymentData.updateCacheSign", emptyDataBean);
		} catch (DataAccessException e) {
			log.error("修改缓存中心空数据记录(updateCacheSign)出错", e);
		}
		if (i > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 添加空数据记录
	 * 
	 * @param emptyDataBean
	 *            空数据记录
	 * @return 操作结果 true 成功 , false 失败
	 */
	public Boolean insertIntoEmptyInfo(EmptyDataBean emptyDataBean) {
		int i = 0;
		try {
			i = (Integer) getSqlSession().update(
					"SupplymentData.insertEmptyInfo", emptyDataBean);
		} catch (DataAccessException e) {
			log.error("添加空数据记录(inserIntoEmptyInfo)出错", e);
		}
		if (i > 0) {
			return true;
		}
		return false;
	}
}
