package com.microwise.msp.hardware.common;

import com.google.common.collect.EvictingQueue;
import com.microwise.msp.util.Maths;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 统计信息,方便调试开发
 *
 * @author gaohui
 * @date 13-8-6 09:30
 */
public class Stats {
    public static final Stats  instance = new Stats();

    /**
     * (6 * 10sec) * 15
     */
    private EvictingQueue<Integer> packetWrites15Minute = EvictingQueue.create(90);

    /**
     * (6 * 10sec) * 5
     */
    private EvictingQueue<Integer> packetWrites5Minute = EvictingQueue.create(30);

    /**
     * 6 * 10sec
     */
    private EvictingQueue<Integer> packetWrites1Minute = EvictingQueue.create(6);

    // 总记入库了多少数据包
    private AtomicInteger packetWrites = new AtomicInteger(0);

    // 数据包临时写入次数
    private AtomicInteger packetWritesForSpeed = new AtomicInteger(0);

    // 每秒入库次数
    private AtomicReference<Float> packWritesPerSecond = new AtomicReference<Float>(new Float(0));

    /**
     * 返回实例
     *
     * @return
     */
    public static Stats getInst(){
        return instance;
    }

    /**
     * 返回入库包数
     *
     * @return
     */
    public int packetWrites(){
        return packetWrites.get();
    }

    /**
     * 入库包数加1, 并返回总入库次数
     *
     * @return
     */
    public int incrPacketWrites(int delta){
        packetWritesForSpeed.addAndGet(delta);
        return packetWrites.addAndGet(delta);
    }

    public void compute(int interval){
        int writeTimes = packetWritesForSpeed.getAndSet(0);

        packetWrites1Minute.add(writeTimes);
        packetWrites5Minute.add(writeTimes);
        packetWrites15Minute.add(writeTimes);

        float speedPerSecond2 = Maths.round((double) writeTimes / interval, 1).floatValue();
        packWritesPerSecond.set(speedPerSecond2);
    }

    /**
     * 重置入库包数
     *
     */
    public void resetPacketWrites(){
        packetWrites.set(0);
    }

    public AtomicInteger getPacketWritesForSpeed(){
        return packetWritesForSpeed;
    }

    public AtomicReference<Float> getPacketWritesPerSecond(){
        return packWritesPerSecond;
    }

    public int packetWrites1Minute(){
        return sum(packetWrites1Minute);
    }

    public int packetWrites5Minute(){
        return sum(packetWrites5Minute);
    }

    public int packetWrites15Minute(){
        return sum(packetWrites15Minute);
    }

    public EvictingQueue<Integer> getPacketWrites15MinuteQueue(){
        return packetWrites15Minute;
    }

    private static int sum(EvictingQueue<Integer> queue){
        int sum = 0;
        for(Integer count: queue){
            sum += count;
        }

        return sum;
    }
}
