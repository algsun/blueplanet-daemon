package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.base.Strings;
import com.microwise.msp.hardware.businessservice.NetService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.netlink.NetLinkService;
import com.microwise.msp.hardware.vo.NetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 网络监听
 *
 * @author gaohui
 * @date 13-10-30 13:39
 */
@Component
@Scope("prototype")
@Route("/struts")
public class NetworksController {
    @Autowired
    private NetLinkService netLinkService;
    @Autowired
    private NetService netService;

    @Autowired
    private AppCache appCache;

    //output
    private List<NetInfo> netinfos;

    // 所有
    @Route(value = "/networks", method = MethodType.GET)
    public String index() {
        netinfos = netService.findByType(NetInfo.MODE_UDP);
        netinfos.addAll(netService.findByType(NetInfo.MODE_TCP));
        return Results.ftl("pages/networks");
    }

    //input
    private int port = 10001;
    private int model = 2;
    private String siteId;

    // new
    @Route("/networks/new")
    public String NEW(){
        return Results.ftl("pages/networks-new");
    }

    // 添加
    @Route(value = "/networks", method = MethodType.POST)
    public String create() {
        siteId = Strings.emptyToNull(siteId);

        if(!Strings.isNullOrEmpty(siteId)){
            // 必须 8 位, 且全数字
            if(siteId.length() != 8 || siteId.indexOf('0') == 0){
                return Results.redirect("networks");
            }
        }

        netLinkService.addNetInfo(port, model, siteId);
        return Results.redirect("networks");
    }

    //input
    private int id;

    // 删除
    @Route(value="/networks/{id}", params = {"_method=delete"})
    public String destroy() {
        netLinkService.delete(id);
        // 清缓存
        appCache.evictNetInfos();
        return Results.redirect("../networks");
    }


    public List<NetInfo> getNetinfos() {
        return netinfos;
    }

    public void setNetinfos(List<NetInfo> netinfos) {
        this.netinfos = netinfos;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
