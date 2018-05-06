package com.microwise.msp.hardware.webservice;

import com.google.gson.Gson;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.dao.SqlMapClient2SqlSessionAdapter;
import com.microwise.msp.hardware.vo.NetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络管理webService
 * 
 * @author xuexu
 * 
 */
@WebService
public class NetLinkDaoImpl extends SqlMapClient2SqlSessionAdapter implements
		NetLinkDao {

	private static Logger log = LoggerFactory.getLogger(NetLinkDaoImpl.class);

	@Override
	public boolean addCommunicationInterface(@WebParam(name = "model") int model, @WebParam(name = "lport") int lport) {
		boolean bool = true;
		NetInfo net = new NetInfo();
		net.setLport(lport);
		net.setModel(model);
		try {
			// 如果当前端口不存在，则创建
			if (!hasSamePort(net.getLport(), net.getModel())) {
				net.setState(2);
				getSqlSession().insert("netLink.addCommunicationInterface", net);
				switch (net.getModel()) {
				case Defines._WS_Udp: // 创建UDP监听
//					udpLinker.createConnection(net.getLport());
					break;
				case Defines._WS_Tcp_Server: // 创建TCP监听
                    // 暂时无 TCP 业务 @gaohui 2013-09-12
					break;
				}
			}
		} catch (DataAccessException e) {
			log.error("\n\n 添加通讯端口(addCommunicationInterface)出错 \n\n",e);
			bool = false;
		}
		return bool;
	}

	@Override
	public boolean deleteCommunicationInterface(@WebParam(name = "port")
	int port, @WebParam(name = "model")
	int model) {
		boolean bool = true;
		try {
			NetInfo netInfo = new NetInfo();
			netInfo.setLport(port);
			netInfo.setModel(model);
			getSqlSession().delete("netLink.deleteCommunicationInterface", netInfo);
			switch (model) {
			case Defines._WS_Udp: // 中断UDP监听
//				udpLinker.disConnection(port);
				break;
			case Defines._WS_Tcp_Server: // 中断TCP监听
                // 暂时无 TCP 业务 @gaohui 2013-09-12
				break;
			}
		} catch (DataAccessException e) {
			log.error("\n\n 删除通讯端口(deleteCommunicationInterface)出错 \n\n",e);
			bool = false;
		}
		return bool;
	}

	@Override
	public boolean hasSamePort(@WebParam(name = "port")
	int port, @WebParam(name = "model")
	int model) {
		int count = 0;
		try {
			NetInfo netInfo = new NetInfo();
			netInfo.setLport(port);
			netInfo.setModel(model);
			count = (Integer) getSqlSession().selectOne("netLink.hasSamePort", netInfo);
		} catch (DataAccessException e) {
			log.error("\n\n 是否有相同的通讯端口(hasSamePort)出错 \n\n",e);
		}
		if (count > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getCommunications() {
		Gson gson = new Gson();
		List<NetInfo> list = new ArrayList<NetInfo>();
		try {
			list = getSqlSession().selectList("netLink.getCommunications");
		} catch (DataAccessException e) {
			log.error("\n\n 获取所有通讯接口(getCommunications)出错 \n\n",e);
		}
		return gson.toJson(list);
	}

}
