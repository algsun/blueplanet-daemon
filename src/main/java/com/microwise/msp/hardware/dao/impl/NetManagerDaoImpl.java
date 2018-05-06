package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.dao.NetManagerDao;
import com.microwise.msp.hardware.vo.NetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网络管理daoImpl
 * 
 * @author xuexu
 * 
 */
public class NetManagerDaoImpl extends BaseDaoImpl implements NetManagerDao {
	private static Logger log = LoggerFactory.getLogger(NetManagerDaoImpl.class);

    public void addNetInfo(int port, int model, String siteId){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("lport", port);
        paraMap.put("model", model);
        paraMap.put("siteId", siteId);
        getSqlSession().insert("NetManager.addNetInfo", paraMap);
    }

    @Override
	public boolean hasSameLocalPort(int lport) {
		int count = 0;
		try {
			count = (Integer) getSqlSession().selectOne(
					"NetManager.hasSameLocalPort", lport);
		} catch (DataAccessException e) {
			log.error("\n\n 检查系统是否已经配置了相同的本地监听端口(hasSameLocalPort)出错 \n\n", e);
		}
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteRecord(NetInfo netInfo) {
		int i = 0;
		try {
			i = getSqlSession().delete("NetManager.deleteRecord",
					netInfo);
		} catch (DataAccessException e) {
			log.error("\n\n 删除一条接口记录(deleteRecord)出错 \n\n", e);
		}
		if (i > 0) {
			return true;
		}
		return false;
	}

    @Override
    public void deleteById(int id){
        getSqlSession().delete("NetManager.deleteById", id);
    }

	@Override
	public boolean updateCommState(String id, int state) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("state", state);
		paraMap.put("id", id);
		int i = 0;
		try {
			i = getSqlSession().update("NetManager.updateCommState",
					paraMap);
		} catch (DataAccessException e) {
			log.error("\n\n 更新通讯状态(updateCommState)出错 \n\n", e);
		}
		if (i > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NetInfo> getCommunications() {
		List<NetInfo> list = new ArrayList<NetInfo>();
		try {
			list = getSqlSession().selectList("NetManager.getCommunications");
		} catch (DataAccessException e) {
			log.error("\n\n 获取所有通讯接口(getCommunications)出错 \n\n", e);
		}
		return list;
	}

    @Override
    public List<NetInfo> findByType(int type){
        return getSqlSession().selectList("NetManager.findByType", type);
    }

	@Override
	public NetInfo getCommunicationByID(int id) {
		NetInfo net = null;
		try {
			net = getSqlSession().selectOne("NetManager.getCommunicationByID", id);
		} catch (DataAccessException e) {
			log.error("\n\n 通过id获取通讯接口(getCommunicationByID)出错 \n\n", e);
		}
		return net;
	}

	@Override
	public int getConnState(String id) {
		int a = 0;
		try {
			a = (Integer) getSqlSession().selectOne(
					"NetManager.getConnState", id);
		} catch (DataAccessException e) {
			log.error("\n\n 获取通讯状态(getConnState)出错 \n\n", e);
		}
		return a;
	}

}
