package com.microwise.msp.hardware.handler.agent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.microwise.msp.hardware.handler.codec.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 负责多个协议版本的代理
 *
 * 注意：此类单例
 *
 * <p/>
 * 后记：warden 一词来自于魔兽争霸中暗夜精灵的英雄中其中一位的名字
 *
 * @author gaohui
 * @date 13-8-10 14:36
 */
@Component("facadeAgentWarden")
@Scope("singleton")
public class FacadeAgentWarden implements AgentWarden {
    public static final Logger log = LoggerFactory.getLogger(FacadeAgentWarden.class);

    @Autowired
    private ApplicationContext appContext;

    // 访问一个小时后过期
    private Cache<String, AgentWarden> gatewayAgentWardens = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();


    @Override
    public void onPacketReceived(Packet packet) {
        String gatewayKey = gatewayKey(packet);
        AgentWarden agentWarden = gatewayAgentWardens.getIfPresent(gatewayKey);
        if(agentWarden == null){
            // 新的网关守望者
            gatewayAgentWardens.put(gatewayKey, appContext.getBean(GatewayAgentWarden.class));
            agentWarden = gatewayAgentWardens.getIfPresent(gatewayKey);
        }

        agentWarden.onPacketReceived(packet);
    }

    private static String gatewayKey(Packet packet) {
        return packet.getRemoteHost() + ":" + packet.getRemotePort();
    }
}
