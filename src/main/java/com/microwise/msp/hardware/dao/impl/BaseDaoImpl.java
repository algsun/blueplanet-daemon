package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.dao.BaseDao;
import com.microwise.msp.hardware.dao.SqlMapClient2SqlSessionAdapter;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.*;

/**
 * <pre>
 * 持久化基础类实现类
 * </pre>
 * 
 * @author heming
 * @since 2011-09-27
 */
public class BaseDaoImpl extends SqlMapClient2SqlSessionAdapter implements BaseDao {

	private static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getDeviceList() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			map = queryForMap("Base.getDeviceList", null, "nodeId", "type");
		} catch (DataAccessException e) {
			log.error("\n\n 获取当前节点列表(getDeviceList)出错 \n\n", e);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeSensor> getDeviceSensor(String deviceid) {
		try {
			return getSqlSession().selectList("Base.getDeviceSensor", deviceid);
		} catch (DataAccessException e) {
			log.error("\n\n 获取节点传感信息(getDeviceSensor)出错，nodesensor \n\n", e);
		}
		return Collections.emptyList();
	}

    @Override
    public List<LocationSensor> getLocationSensor(String locationId) {
        try {
            return getSqlSession().selectList("Base.getLocationSensor", locationId);
        } catch (DataAccessException e) {
            log.error("\n\n 获取节点传感信息(getLocationSensor)出错，locationSensor \n\n", e);
        }
        return Collections.emptyList();
    }

    @Override
	public boolean isExistTable(String tableName) {
		boolean isExist = false;
		try {
			isExist = (Boolean) getSqlSession().selectOne("Base.isExistTable", tableName);
		} catch (DataAccessException e) {
			log.error("\n\n 检查表是否存在 调用function(isExistTable)出错 \n\n", e);
		}
		return isExist;
	}

}
