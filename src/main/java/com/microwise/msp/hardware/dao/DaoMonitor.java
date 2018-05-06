package com.microwise.msp.hardware.dao;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 与业务无关，统计 dao 方法调用次数
 *
 * @author gaohui
 * @date 13-7-18 10:28
 */
@Component
@Aspect
public class DaoMonitor {
    public static final Logger log = LoggerFactory.getLogger(DaoMonitor.class);

    private int count;
    private ConcurrentHashMap<String, AtomicInteger> methodCounter = new ConcurrentHashMap<String, AtomicInteger>();

    @Around("execution (* com.microwise.msp.hardware.dao.impl.*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        count++;
        log.debug("次数：" + count);


        methodCounter.putIfAbsent(point.getSignature().toString(), new AtomicInteger(0));
        AtomicInteger methodCount = methodCounter.get(point.getSignature().toString());
        methodCount.incrementAndGet();

        return point.proceed();
    }

    public void print() {
        for (Map.Entry<String, AtomicInteger> entry : methodCounter.entrySet()) {
            log.debug(entry.getKey() + " : " + entry.getValue());
        }
    }
}
