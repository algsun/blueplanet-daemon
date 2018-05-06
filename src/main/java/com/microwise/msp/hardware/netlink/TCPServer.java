package com.microwise.msp.hardware.netlink;

import com.microwise.msp.hardware.businessservice.NetService;
import com.microwise.msp.hardware.handler.FixHeaderAndLengthFiledDecoder;
import com.microwise.msp.hardware.handler.TCPServerHandler;
import com.microwise.msp.hardware.vo.NetInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * tcp server
 *
 * @author bastengao
 * @date 14-4-8 上午9:04
 */
@Component
@Scope("singleton")
public class TCPServer {
    public static final Logger log = LoggerFactory.getLogger(TCPServer.class);

    @Autowired
    ApplicationContext appContext;
    @Autowired
    private NetService netService;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ConcurrentMap<Integer, Channel> channels = new ConcurrentHashMap<Integer, Channel>();
    private ServerBootstrap bootstrap;

    private static ChannelAttributeCache channelAttributeCache = new ChannelAttributeCache();

    /**
     * 初始化 TCP server
     */
    @PostConstruct
    private void init() {
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    log.info("init pipeline");
                    socketChannel.pipeline()
                            .addLast(new FixHeaderAndLengthFiledDecoder())
                            .addLast(appContext.getBean(TCPServerHandler.class));
                }
            });


            // 绑定端口
            List<NetInfo> netInfos = netService.findByType(NetInfo.MODE_TCP);
            for (NetInfo netInfo : netInfos) {
                bind(netInfo.getLport());
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void bind(int port) {
        try {
            log.info("bind TCP : {}", port);
            ChannelFuture f = bootstrap.bind(port).sync();
            channels.putIfAbsent(port, f.channel());
        } catch (InterruptedException e) {
            log.error("bind error", e);
        }
    }

    public void unbind(int port) {
        if (channels.containsKey(port)) {
            log.info("unbind TCP : {}", port);
            Channel channel = channels.get(port);
            channel.close();
            channels.remove(port);
        }
    }

    /**
     * 关闭 TCP server
     */
    @PreDestroy
    public void shutdown() {
        closeAllChannels();
        bossGroup.shutdownGracefully();
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

    public static ChannelAttributeCache getChannelAttributeCache() {
        return channelAttributeCache;
    }

}
