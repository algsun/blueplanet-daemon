package com.microwise.msp.hardware.cache;

import com.google.common.cache.CacheLoader;
import com.microwise.msp.hardware.businessservice.NetService;
import com.microwise.msp.hardware.vo.NetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaohui
 * @date 13-8-12 17:15
 */
@Component
@Scope("prototype")
public class NetInfoCacheLoader extends CacheLoader<Integer, NetInfo> {

    @Autowired
    private NetService netService;

    @Override
    public NetInfo load(Integer port) throws Exception {
        List<NetInfo> netInfos = netService.findAllNetInfo();
        for(NetInfo netInfo: netInfos){
            if(netInfo.getLport() == port){
                return netInfo;
            }
        }

        throw new Exception("对应端口未找到: " + port);
    }
}
