package com.microwise.msp.hardware.handler.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 这是一个特殊的线程池, 他将以固定的线程执行相同 workId 的任务
 *
 * TODO 考虑每个设备线程的权重不同，比如控制模块的数据包相对多一些。如果权重一样则可能有失平衡。 @gaohui 2014-03-20
 *
 * @author gaohui
 * @date 13-8-10 17:10
 */
public class ThreadWorkers {
    public static final Logger log = LoggerFactory.getLogger(ThreadWorkers.class);

    private Lock lock = new ReentrantLock();

    /**
     * 线程大小
     */
    private int threadSize;

    /**
     * 工号序列
     * <p/>
     * 此字段的线程安全由 lock 保证
     */
    private int workIdSequence = 1;

    /**
     * 线程序列
     * <p/>
     * 此字段的线程安全由 lock 保证
     */
    private int threadIdSequence = 0;

    /**
     * 工号 => 线程号
     *
     * TODO 可以通过 workId 直接推算出 thread索引，因为大家都是自增的 @gaohui 2013-08-12
     */
    private ConcurrentMap<Integer, Integer> workIds = new ConcurrentHashMap<Integer, Integer>();


    /**
     * 线程号 => 线程
     * <p/>
     * 每个 ExecutorService 都是一个单线程的线程池
     */
    private ExecutorService[] threads;

    /**
     * 线程任务特殊集合
     */
    private List<Collection> workQueues = new ArrayList<Collection>();

    /**
     * 自动根据 cpu 的核数分配线程数量
     */
    public ThreadWorkers(){
        this(Runtime.getRuntime().availableProcessors() + 1);
    }

    public ThreadWorkers(int threadSize) {
        this.threadSize = threadSize;

        this.threads = new ExecutorService[threadSize];
        for (int i = 0; i < threadSize; i++) {
            threads[i] = newSingleThreadExecutor();
        }
    }

    /**
     * 申请工号, 有工号才能执行操作
     *
     * @return
     */
    public int newWorkId() {
        // 此操作原子性 @gaohui 2013-08-10
        lock.lock();
        try {
            int threadId = threadIdSequence++;
            threadId = threadId % threadSize;
            int workId = workIdSequence++;
            workIds.put(workId, threadId);
            return workId;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 干活
     *
     * @param workId
     * @param task
     */
    public void execute(int workId, Runnable task) {
        ExecutorService executorService = getThreadByWorkId(workId);
        executorService.execute(task);
    }

    /**
     * 干活
     *
     * @param workId
     * @param task
     * @return
     */
    public Future<?> submit(int workId, Runnable task) {
        ExecutorService executorService = getThreadByWorkId(workId);
        return executorService.submit(task);
    }

    /**
     * 返回线程个数
     *
     * @return
     */
    public int threadSize(){
        return threadSize;
    }

    /**
     * 返回所有线程的任务队列大小
     *
     * @return
     */
    public int workQueueSize(){
        int size = 0;
        for(Collection workQueue: workQueues){
            size += workQueue.size();
        }
        return size;
    }

    /**
     * 按线程返回队列大小
     *
     * @return
     */
    public List<Integer> workQueueSizes(){
        List<Integer> workQueueSizes = new ArrayList<Integer>();
        for(int i = 0; i< threadSize; i++){
            workQueueSizes.add(workQueues.get(i).size());
        }

        return workQueueSizes;
    }

    /**
     * 停止接受新的任务，直至现存的任务完成后，关闭整个线程池
     */
    public void shutdown() {
        log.info("shutdown ...");
        for (ExecutorService executorService : threads) {
            executorService.shutdown();
        }
    }

    /**
     * 停止正在执行的任务，关闭整个线程池
     */
    public void shutdownNow() {
        log.info("shutdown ...");
        for (ExecutorService executorService : threads) {
            executorService.shutdownNow();
        }
    }


    /**
     * 根据工号返回对应线程
     *
     * @param workId
     * @return
     */
    private ExecutorService getThreadByWorkId(int workId) {
        int threadId = workIds.get(workId);
        if (threadId >= threads.length) {
            throw new IllegalArgumentException("错误的工号");
        }

        return threads[threadId];
    }

    private ThreadPoolExecutor newSingleThreadExecutor(){
        BlockingQueue<Runnable> workQueue =  new LinkedBlockingQueue<Runnable>();
        workQueues.add(workQueue);
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, workQueue);
    }
}
