package com.microwise.msp.hardware.netlink;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.microwise.msp.hardware.businessservice.NetService;
import com.microwise.msp.hardware.handler.UDPServerHandler2;
import com.microwise.msp.hardware.vo.NetInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author gaohui
 * @date 14-1-10 11:27
 */
@Component
@Scope("singleton")
public class UDPServer {
    public static final Logger log = LoggerFactory.getLogger(UDPServer.class);


    @Autowired
    ApplicationContext appContext;
    @Autowired
    private NetService netService;

    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;
    private ConcurrentMap<Integer, Channel> channels = new ConcurrentHashMap<Integer, Channel>();

    /**
     * channelKey(remoteAddress) => channelHandlerContext
     * <p/>
     * 写后 20 分钟 超时
     */
    private static final Cache<String, ChannelHandlerContext> channelCache = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .build();

    private static ChannelAttributeCache channelAttributeCache = new ChannelAttributeCache();

    @PostConstruct
    public void init() {
        try {
            workerGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {
                @Override
                protected void initChannel(NioDatagramChannel ch) throws Exception {
                    ch.pipeline().addLast(appContext.getBean(UDPServerHandler2.class));
                }
            });
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 绑定端口
     */
    public void bindAll() {
        List<NetInfo> netInfos = netService.findByType(NetInfo.MODE_UDP);
        for (NetInfo netInfo : netInfos) {
            bind(netInfo.getLport());
        }
    }

    /**
     * 解除绑定端口
     */
    public void unbindAll() {
        List<NetInfo> netInfos = netService.findAllNetInfo();
        for (NetInfo netInfo : netInfos) {
            if (netInfo.getModel() == NetInfo.MODE_UDP) {
                unbind(netInfo.getLport());
            }
        }
    }

    public void bind(List<Integer> ports) {
        for (int port : ports) {
            bind(port);
        }
    }

    public void bind(int port) {
        try {
            log.info("bind UDP : {}", port);
            ChannelFuture f = bootstrap.bind(port).sync();
            channels.putIfAbsent(port, f.channel());
        } catch (InterruptedException e) {
            log.error("bind error", e);
        }
    }

    public void unbind(int port) {
        if (channels.containsKey(port)) {
            log.info("unbind UDP : {}", port);
            Channel channel = channels.get(port);
            channel.close();
            channels.remove(port);
        }
    }

    /**
     * 首选使用已存在的连接发送数据, 如果不存在返回 false
     *
     * @param host
     * @param port
     * @param datagram
     * @return
     */
    public static boolean sendByChannel(String host, int port, byte[] datagram) {
        ChannelHandlerContext channelCtx = channelCache.getIfPresent(channelKey(host, port));
        if (channelCtx == null) {
            return false;
        }

        // 如果 channel 不可用，则从缓存清除
        if (!channelCtx.channel().isActive()) {
            channelCache.invalidate(channelKey(host, port));
            return false;
        }

        DatagramPacket datagramPacket = new DatagramPacket( Unpooled.wrappedBuffer(datagram),
                new InetSocketAddress(host, port));
        channelCtx.writeAndFlush(datagramPacket);

        return true;
    }

    /**
     * 关闭 udp 监听及 netty
     */
    public void shutdown() {
        log.info("udp server shutdown");
        closeAllChannels();
        workerGroup.shutdownGracefully();
    }

    /**
     * 关闭所有链接
     */
    private void closeAllChannels(){
        for(Channel channel : channels.values()){
            channel.close();
        }
    }

    public static void putChannel(InetSocketAddress address, ChannelHandlerContext channelCtx) {
        channelCache.put(channelKey(address), channelCtx);
    }

    public static Cache<String, ChannelHandlerContext> getChannelCache() {
        return channelCache;
    }

    public static ChannelAttributeCache getChannelAttributeCache() {
        return channelAttributeCache;
    }

    public static String channelKey(String host, int port) {
        return host + ":" + port;
    }

    public static String channelKey(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
}
