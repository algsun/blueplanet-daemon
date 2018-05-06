package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.base.Strings;
import com.jcabi.manifests.Manifests;
import com.microwise.msp.hardware.businessbean.Site;
import com.microwise.msp.hardware.businessservice.NetService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Stats;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.handler.agent.ThreadWorkers;
import com.microwise.msp.hardware.netlink.ChannelAttribute;
import com.microwise.msp.hardware.netlink.TCPServer;
import com.microwise.msp.hardware.netlink.UDPServer;
import com.microwise.msp.hardware.vo.NetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author gaohui
 * @date 13-12-13 13:39
 */
@Route("/struts/")
@Component
@Scope("prototype")
public class IndexController {

    @Autowired
    private ThreadWorkers threadWorkers;

    @Autowired
    private NetService netService;

    @Autowired
    private AppCache appCache;

    //output
    // svn 版本号
    private int svnRevision;

    // 数据包任务队列
    private int workQueueSize;
    // 历史数据队列
    private int historyQueueSize = SysConfig.getInstance().getPersistenceQueue().size();

    // 入库包数
    private int packetWrites = Stats.getInst().packetWrites();
    // 入库速度
    private float packetWritesPerSecond = Stats.getInst().getPacketWritesPerSecond().get();
    // 入库包数
    private int packetWrites1Minute = Stats.getInst().packetWrites1Minute();
    private int packetWrites5Minute = Stats.getInst().packetWrites5Minute();
    private int packetWrites15Minute = Stats.getInst().packetWrites15Minute();
    private Queue<Integer> packetWrites15MinuteQueue = Stats.getInst().getPacketWrites15MinuteQueue();

    // 监听端口
    private List<NetInfo> udpNetInfos;
    private List<NetInfo> tcpNetInfos;
    // 站点名称
    private Map<String, String> siteNames = new HashMap<String, String>();

    private Map<String, ChannelAttribute> udpChannelAttributes;
    private Map<String, ChannelAttribute> tcpChannelAttributes;

    /**
     * 操作系统名称
     * @return
     */
    String osName;

    @Route("/index")
    public String index() {
        initSvnRevision();
        workQueueSize = threadWorkers.workQueueSize();

        tcpNetInfos = netService.findByType(NetInfo.MODE_TCP);
        udpNetInfos = netService.findByType(NetInfo.MODE_UDP);
        for (NetInfo netInfo : udpNetInfos) {
            loadSite(netInfo.getSiteId());
        }
        for (NetInfo netInfo : tcpNetInfos) {
            loadSite(netInfo.getSiteId());
        }

        udpChannelAttributes = UDPServer.getChannelAttributeCache().getCache().asMap();
        for(ChannelAttribute channelAttribute : udpChannelAttributes.values()){
            loadSite(channelAttribute.getSiteId());
        }
        tcpChannelAttributes = TCPServer.getChannelAttributeCache().getCache().asMap();
        for(ChannelAttribute channelAttribute : tcpChannelAttributes.values()){
            loadSite(channelAttribute.getSiteId());
        }
        osName = System.getProperties().getProperty("os.name");
        return Results.ftl("/pages/index.html.ftl");
    }

    // 加载版本号
    private void initSvnRevision() {
        try {
            String svnRevisionStr = Manifests.read("App-Svn-Revision");
            if (!Strings.isNullOrEmpty(svnRevisionStr)) {
                svnRevision = Integer.parseInt(svnRevisionStr);
            }
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }

    private void loadSite(String siteId){
        if (!Strings.isNullOrEmpty(siteId)) {
            Site site = appCache.loadSite(siteId);
            if(site == null){
                siteNames.put(siteId, "站点不存在");
            }else{
                siteNames.put(siteId, site.getName());
            }
        }
    }

    public int getSvnRevision() {
        return svnRevision;
    }

    public int getWorkQueueSize() {
        return workQueueSize;
    }

    public int getHistoryQueueSize() {
        return historyQueueSize;
    }

    public int getPacketWrites() {
        return packetWrites;
    }

    public float getPacketWritesPerSecond() {
        return packetWritesPerSecond;
    }

    public List<NetInfo> getUdpNetInfos() {
        return udpNetInfos;
    }

    public Map<String, String> getSiteNames() {
        return siteNames;
    }

    public Map<String, ChannelAttribute> getUdpChannelAttributes() {
        return udpChannelAttributes;
    }

    public int getPacketWrites1Minute() {
        return packetWrites1Minute;
    }

    public int getPacketWrites5Minute() {
        return packetWrites5Minute;
    }

    public int getPacketWrites15Minute() {
        return packetWrites15Minute;
    }

    public Queue<Integer> getPacketWrites15MinuteQueue() {
        return packetWrites15MinuteQueue;
    }

    public List<NetInfo> getTcpNetInfos() {
        return tcpNetInfos;
    }

    public Map<String, ChannelAttribute> getTcpChannelAttributes() {
        return tcpChannelAttributes;
    }

    public void setTcpChannelAttributes(Map<String, ChannelAttribute> tcpChannelAttributes) {
        this.tcpChannelAttributes = tcpChannelAttributes;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
}
