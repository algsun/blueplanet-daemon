package com.microwise.msp.hardware.netlink;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author bastengao
 * @date 14-4-16 下午2:56
 */
public class ChannelAttributeCache {
    public static final Logger log = LoggerFactory.getLogger(ChannelAttributeCache.class);

    /**
     * channelKey(remoteAddress) => channelAttribute
     * <p/>
     * 写后 1 分钟超时
     */
    private final Cache<String, ChannelAttribute> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    /**
     * channel 对应额外的属性, channelKey(remoteAddress) => ChannelAttribute
     *
     * @return
     */
    public Cache<String, ChannelAttribute> getCache() {
        return cache;
    }

    public void put(InetSocketAddress address, ChannelAttribute channelAttribute){
        cache.put(channelKey(address), channelAttribute);
    }

    /**
     * 获取远端地址对应的属性, 如果没有创建
     *
     * @param address
     * @return
     */
    public ChannelAttribute requireChannelAttribute(final InetSocketAddress address) {
        try {
            return cache.get(channelKey(address), new Callable<ChannelAttribute>() {
                @Override
                public ChannelAttribute call() throws Exception {
                    ChannelAttribute channelAttribute = new ChannelAttribute();

                    channelAttribute.setRemoteAddress(address);

                    return channelAttribute;
                }
            });
        } catch (ExecutionException e) {
            log.error("", e);
        }

        return null;
    }

    public static String channelKey(String host, int port) {
        return host + ":" + port;
    }

    public static String channelKey(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
}
