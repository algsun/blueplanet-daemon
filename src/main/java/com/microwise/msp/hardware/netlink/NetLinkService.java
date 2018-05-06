package com.microwise.msp.hardware.netlink;

import com.microwise.msp.hardware.dao.NetManagerDao;
import com.microwise.msp.hardware.vo.NetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通讯模块
 * 目前只提供 UDP 服务
 *
 * @author heming
 * @author gaohui
 * @date 2013-09-03
 * @since 2011-10-12
 */
@Transactional
public class NetLinkService {
    public static final Logger log = LoggerFactory.getLogger(NetLinkService.class);

    /**
     * 网络管理dao
     */
    private NetManagerDao dao;

    @Autowired
    private UDPServer udpServer;
    @Autowired
    private TCPServer tcpServer;

    /**
     *
     * @param port
     * @param model
     * @param siteId 可选
     */
    public void addNetInfo(int port, int model, String siteId) {
        dao.addNetInfo(port, model, siteId);
        if (model == NetInfo.MODE_UDP) {
            udpServer.bind(port);
        } else if (model == NetInfo.MODE_TCP) {
            tcpServer.bind(port);
        }
    }

    /**
     * 删除监听端口
     *
     * @param id
     */
    public void delete(int id) {
        NetInfo netinfo = dao.getCommunicationByID(id);
        dao.deleteById(id);
        if (netinfo.getModel() == NetInfo.MODE_UDP) {
            udpServer.unbind(netinfo.getLport());
        } else if (netinfo.getModel() == NetInfo.MODE_TCP) {
            tcpServer.unbind(netinfo.getLport());
        }
    }

    /**
     * 返回UDP监测端口
     *
     * @return
     */
    public List<NetInfo> getUdpPorts() {
        return dao.findByType(NetInfo.MODE_UDP);
    }

    public void setDao(NetManagerDao dao) {
        this.dao = dao;
    }

}
