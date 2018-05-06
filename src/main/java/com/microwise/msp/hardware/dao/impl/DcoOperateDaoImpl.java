package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.dao.DcoOperateDao;
import com.microwise.msp.hardware.vo.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据控制Dao
 * 
 * @author xuexu
 * 
 */
public class DcoOperateDaoImpl extends BaseDaoImpl implements DcoOperateDao {

	private static Logger log = LoggerFactory.getLogger(DcoOperateDaoImpl.class);

	/** 父节点IP */
	private int parentid;
	/** 节点类型 */
	private int type;

    @Override
    public List<String> findRootNodeIdsBySiteId(String siteId) {
        return getSqlSession().selectList("Dco.findRootNodeIdBySiteId", siteId);
    }

    @Override
	public int getGateWayByDeviceBean(DeviceBean deviceBean) {
		int gateWayid = 1;
		try {
			DeviceBean tempBean = deviceBean;
			int count = 0;
			do {
				tempBean = (DeviceBean) getSqlSession()
						.selectOne("Dco.getGateWayByDeviceBean", tempBean);
				count++;
			} while (null != tempBean
					&& count < 20
					&& (tempBean.selfid != tempBean.parentid || tempBean.deviceType != Defines.DEVICE_GATEWAY));

			if (tempBean != null) {
				gateWayid = tempBean.selfid;
			}
		} catch (Exception e) {
			log.error("\n\n 获取网关Id(getGateWayByDeviceBean)出错 \n\n", e);
		}
		return gateWayid;
	}

	

	@Override
	public boolean getParent(int childId, String siteId ,Map<String,Integer> devInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("childIP", childId);
		map.put("siteId", siteId);
		boolean success = true;
		DeviceBean dev = new DeviceBean();
		try {
			dev = (DeviceBean) getSqlSession().selectOne(
					"Dco.getParent", map);
			if (null != dev) {
				devInfo.put("parentid", dev.parentid);
				devInfo.put("type", dev.deviceType);
			} else {
				success = false;
			}
		} catch (DataAccessException e) {
			log.error("\n\n 根据childId+siteId获取父节点Id和节点类型(getParent)出错 \n\n", e);
			success = false;
		}
		return success;
	}

	@Override
	public boolean isSameOrder(Order order) {
		int count = 0;
		boolean bool = true;
		try {
			count = (Integer) getSqlSession().selectOne(
					"Dco.isSameOrder", order);
		} catch (DataAccessException e) {
			log.error("\n\n 是否存在相同的命令(isSameOrder)出错 \n\n", e);
			bool = false;
		}
		if (count == 0) {
			bool = false;
		}
		return bool;
	}

	@Override
	public boolean storageOrder(Order order) {
		boolean bool = true;
		try {
			getSqlSession().insert("Dco.storageOrder", order);
		} catch (DataAccessException e) {
			bool = false;
			log.error("\n\n 存储下行命令包(storageOrder)出错 \n\n", e);
		}
		return bool;
	}

	@Override
	public boolean updateOrder(Order order) {
		boolean bool = true;
		try {
			getSqlSession().insert("Dco.updateOrder", order);
		} catch (DataAccessException e) {
			log.error("\n\n 更新指令包(updateOrder(" + order.getOrderId()
					+ "))出错 \n\n", e);
			bool = false;
		}
		return bool;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getExceptionOrders() {
		List<Order> orderList = new ArrayList<Order>();
		try {
			orderList = getSqlSession().selectList(
					"Dco.getExceptionOrders");
		} catch (DataAccessException e) {
			log.error("\n\n 获取异常命令列表(getExceptionOrders)出错 \n\n", e);
		}
		return orderList;
	}

	@Override
	public boolean setOrderInvalid(int orderId) {
		boolean bool = true;
		try {
			getSqlSession().update("Dco.setOrderInvalid", orderId);
		} catch (DataAccessException e) {
			log.error("\n\n 设置命令失效(setOrderInvalid(" + orderId + "))出错 \n\n", e);
			bool = false;
		}
		return bool;
	}

	@Override
	public Order getOrderPropertyFromDB(int orderId) {
		Order order = new Order();
		try {
			order = (Order) getSqlSession().selectOne(
					"Dco.getOrderPropertyFromDB", orderId);
		} catch (DataAccessException e) {
			log.error("\n\n 获取命令属性(getOrderPropertyFromDB)出错 \n\n", e);
		}
		return order;
	}

	@Override
	public int isActiveOrder(String orderSerial) {
		Order order = new Order();
		try {
			order = (Order) getSqlSession().selectOne(
					"Dco.isActiveOrder", orderSerial);
		} catch (DataAccessException e) {
			log.error("\n\n 是否存在有效命令(isActiveOrder)出错 \n\n", e);
		}
		if (order != null && order.getOrderId() != 0) {
			return order.getOrderId();
		}
		return 0;
	}

	@Override
	public void updateState(int orderId, int currentState) {
		try {
			Order order = new Order();
			order.setOrderId(orderId);
			order.setCurrentState(currentState);
			getSqlSession().update("Dco.updateState", order);
		} catch (DataAccessException e) {
			log.error("\n\n 修改命令执行的反馈码[01成功/02失败/03送达成功/FF路径不通](updateState("
                    + orderId + "," + currentState + "))出错 \n\n", e);
		}
	}

	@Override
	public String getSelfIdByOrderId(int orderId) {
		String deviceId = "";
		try {
			deviceId = (String) getSqlSession().selectOne(
					"Dco.getSelfIdByOrderId", orderId);
		} catch (DataAccessException e) {
			log.error("\n\n 获取下行命令的目标ID(getSelfIdByOrderid)出错 \n\n", e);
		}
		return deviceId;
	}

	@Override
	public void updateDataReciveCount(Order order) {
		try {
			getSqlSession().selectOne(
					"Dco.updateDataReciveCount", order);
		} catch (DataAccessException e) {
			log.error("\n\n 更新接收数据次数(updateDataReciveCount)出错 \n\n", e);
		}
		return;
	}

}
