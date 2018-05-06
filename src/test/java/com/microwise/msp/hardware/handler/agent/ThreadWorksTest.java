package com.microwise.msp.hardware.handler.agent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaohui
 * @date 13-8-10 17:38
 */
public class ThreadWorksTest {
    @Test
    public void testAutoThreadSize(){
        ThreadWorkers threadWorkers = new ThreadWorkers();
        Assert.assertEquals(Runtime.getRuntime().availableProcessors() + 1, threadWorkers.threadSize());
        threadWorkers.shutdown();
    }

    @Test
    public void testNewWorkId() throws ExecutionException, InterruptedException {
        ThreadWorkers workers = new ThreadWorkers(5);

        final ListMultimap<String, Integer> threadCount = Multimaps.synchronizedListMultimap(ArrayListMultimap.<String, Integer>create());
        final AtomicInteger count = new AtomicInteger(0);
        int times = 50;
        for (int i = 0; i < times; i++) {
            final int workId = workers.newWorkId();

            Future future = workers.submit(workId, new Runnable() {
                @Override
                public void run() {
                    count.incrementAndGet();
                    threadCount.put(Thread.currentThread().getName(), workId);
                }
            });

            future.get();
        }

        // 判断总共执行的次数
        Assert.assertEquals(times, count.get());
        Map<String, Collection<Integer>> threadCountMap = threadCount.asMap();
        for (Map.Entry<String, Collection<Integer>> entry : threadCountMap.entrySet()) {
            // 每个线程都均匀的执行了 10 次
            Assert.assertEquals(10, entry.getValue().size());
        }

        workers.shutdown();
    }
}
