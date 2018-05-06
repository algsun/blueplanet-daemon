package com.microwise.msp.hardware.netlink;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.hardware.handler.channel.Channel;
import com.microwise.msp.hardware.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 通过兄弟节点推断最可能的 udp ip:port.
 * <p/>
 * 如何推断节点是自己的兄弟节点：
 * 一个网关下的网络是棵树，他们都有共同的根节点。
 * <p/>
 * 每个节点都都跟新根节点对应的通道
 *
 * @author bastengao
 * @date 14-4-3 下午5:34
 */
@Component
@Scope("singleton")
public class DeviceChannelSuggestor {

    // 一个网络下的最新通道  rootDeviceId => channel
    private Cache<String, SuggestChannel> root2channelCache;

    // 当前节点的根节点,  deviceId => rootDeviceId
    private LoadingCache<String, String> rootsCache;

    // 当前节点的父节点,  deviceId => parentParentId
    private LoadingCache<String, String> myParentsCache;

    private DeviceService deviceService;

    @Autowired
    private void initCaches(final DeviceService deviceService) {
        this.deviceService = deviceService;
        root2channelCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();

        myParentsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String deviceId) throws Exception {
                        DeviceBean device = deviceService.findDeviceById(deviceId);
                        if (device == null) {
                            // throw exception
                            throw new Exception("device not found");
                        }
                        return DcoOperateService.getParentIp(deviceId, device.parentid, device.version);
                    }
                });

        initRootsCache();
    }

    private void initRootsCache() {
        rootsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String deviceId) throws Exception {
                        String currentDeviceId = null;
                        String parentDeviceId = deviceId;
                        Set<String> parentDeviceIds = new HashSet<String>();
                        do {
                            //防止环形网络，导致死循环
                            if (parentDeviceIds.contains(currentDeviceId)){
                                break;
                            }
                            try {
                                currentDeviceId = parentDeviceId;
                                parentDeviceId = myParentsCache.get(currentDeviceId);
                                parentDeviceIds.add(parentDeviceId);
                            } catch (Exception e) {
                                throw new Exception("query parent id failed");
                            }
                        }
                        // 根节点的父节点通常是自己
                        while (!parentDeviceId.equals(currentDeviceId));

                        return currentDeviceId;
                    }
                });

    }

    /**
     * 返回建议的通道, 如果无返回 null
     *
     * @param deviceId
     * @return
     */
    public SuggestChannel querySuggestChannel(String deviceId) {
        // 先尝试通过共享的 rootChannel 获取, 如果不行通过数据库获取设备最后的通道
        String rootDeviceId = queryRootDeviceId(deviceId);
        if (rootDeviceId != null) {
            try {
                return root2channelCache.get(rootDeviceId, new Callable<SuggestChannel>() {
                    @Override
                    public SuggestChannel call() throws Exception {
                        throw new Exception("not found");
                    }
                });
            } catch (ExecutionException e) {
                DeviceBean device = deviceService.findDeviceById(deviceId);
                if (device != null) {
                    // TODO 不是所有的通信都是 UDP, 也可能是 TCP @gaohui 2014-04-15
                    return new SuggestChannel(SuggestChannel.TYPE_UDP, new InetSocketAddress(device.remoteAddress, device.remotePort));
                }
            }
        }


        return null;
    }

    /**
     * 获取某个设备的根节点
     *
     * @param deviceId
     * @return
     */
    public String queryRootDeviceId(String deviceId) {
        try {
            return rootsCache.get(deviceId);
        } catch (ExecutionException e) {
            return null;
        }
    }

    /**
     * 跟新一个网络下的通道
     *
     * @param rootDeviceId
     * @param channel
     */
    public void putRootChannel(String rootDeviceId, SuggestChannel channel) {
        root2channelCache.put(rootDeviceId, channel);
    }


    /**
     * 建议的通道
     */
    public static class SuggestChannel {
        public static final int TYPE_UDP = 2;
        public static final int TYPE_TCP = 3;

        // 通讯类型
        private int type = 1;

        // 远端地址
        private InetSocketAddress remoteAddress;

        private Channel channel;

        public SuggestChannel(int type, InetSocketAddress remoteAddress) {
            this.type = type;
            this.remoteAddress = remoteAddress;
        }

        public SuggestChannel(int type, InetSocketAddress remoteAddress, Channel channel) {
            this.type = type;
            this.remoteAddress = remoteAddress;
            this.channel = channel;
        }

        public int getType() {
            return type;
        }

        public InetSocketAddress getRemoteAddress() {
            return remoteAddress;
        }

        public Channel getChannel() {
            return channel;
        }
    }

}
