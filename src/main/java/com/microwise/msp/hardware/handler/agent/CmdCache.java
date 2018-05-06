package com.microwise.msp.hardware.handler.agent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.microwise.msp.hardware.handler.agent.command.Command;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 命令缓存
 *
 * 单例
 * @author bastengao
 * @date 14-4-29 下午4:10
 */
public class CmdCache {

    private static final CmdCache instance = new CmdCache();

    public static final CmdCache getInstance() {
        return instance;
    }

    // 命令缓存, (deviceId + sequence) => command
    private Cache<String, Command> commandCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();


    private CmdCache() {
    }

    /**
     * 获取并且清除
     *
     * @param deviceId
     * @param sequence
     * @return
     */
    public Command getAndEvict(String deviceId, int sequence){
        Command command = get(deviceId, sequence);
        evict(deviceId, sequence);
        return command;
    }

    public Command get(String deviceId, int sequence){
        try {
            return commandCache.get(deviceId + sequence, new Callable<Command>() {
                @Override
                public Command call() throws Exception {
                    throw new Exception(new NoSuchElementException());
                }
            });
        } catch (ExecutionException e) {
            return null;
        }
    }

    public void put(String deviceId, int sequence, Command command){
        commandCache.put(deviceId + sequence, command);
    }


    public void evict(String deviceId, int sequence){
        commandCache.invalidate(deviceId + sequence);
    }


}
