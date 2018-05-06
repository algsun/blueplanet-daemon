package com.microwise.msp.hardware.handler.agent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 管理命令延迟动作，将异步的转换为同步
 * <p/>
 * 此类为单例
 *
 * @author gaohui
 * @date 14-2-28 11:10
 */
public class CmdDeferredManager {

    private static CmdDeferredManager instance = new CmdDeferredManager();

    private CmdDeferredManager() {
    }

    public static CmdDeferredManager getInstance() {
        return instance;
    }

    private ConcurrentMap<String, Deferred> deferreds = new ConcurrentHashMap<String, Deferred>();

    private Cache<String, CmdDeferred> deferredsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    /**
     * 发起一个新的命令, 同时返回命令对应的 Promise
     *
     * @param deviceId
     * @param sequence
     * @return
     */
    public CmdPromise newCommand(String deviceId, int sequence) {
        Deferred deferred = new DeferredObject();
        deferreds.put(deferredKey(deviceId, sequence), deferred);


        CmdDeferred cmdDeferred = new CmdDeferred(deviceId, sequence);
        deferredsCache.put(deferredKey(deviceId, sequence), cmdDeferred);

        return cmdDeferred.promise();
    }

    /**
     * 命令被网关收到
     *
     * @param deviceId
     * @param sequence
     */
    public void gotCommand(String deviceId, int sequence) {
        CmdDeferred cmdDeferred = deferredsCache.getIfPresent(deferredKey(deviceId, sequence));
        if (cmdDeferred == null) {
            return;
        }

        if(cmdDeferred.getSendDeferred().isPending()){
            cmdDeferred.getSendDeferred().resolve(null);
        }
    }

    /**
     * 命令被成功执行
     *
     * @param deviceId
     * @param sequence
     * @param success 命令是否执行成功
     */
    public void executeCommand(String deviceId, int sequence, boolean success) {
        CmdDeferred cmdDeferred = deferredsCache.getIfPresent(deferredKey(deviceId, sequence));
        if (cmdDeferred == null) {
            return;
        }


        Deferred deferred = cmdDeferred.getDoDeferred();
        if(deferred.isPending()){
            if (success) {
                deferred.resolve(null);
            } else {
                deferred.reject(null);
            }
        }
    }

    private static String deferredKey(String deviceId, int sequence) {
        return deviceId + sequence;
    }

}
