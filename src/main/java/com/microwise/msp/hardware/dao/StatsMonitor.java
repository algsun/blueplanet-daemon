package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.common.Stats;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 统计信息监视器
 *
 * @author gaohui
 * @date 13-7-18 10:28
 */
@Component
@Aspect
public class StatsMonitor {
    public static final Logger log = LoggerFactory.getLogger(StatsMonitor.class);

    // @Around("execution (* com.microwise.msp.hardware.businessservice.DataPersistenceService.saveBatch(..))")
    public Object aroundSaveBatch(ProceedingJoinPoint point) throws Throwable {
        List deviceBeans = (List) point.getArgs()[0];
        Object o = point.proceed();
        Stats.getInst().incrPacketWrites(deviceBeans.size());
        return o;
    }

    // @Around("execution (* com.microwise.msp.hardware.businessservice.DataPersistenceService.persistence(..))")
    public Object aroundPersistence(ProceedingJoinPoint point) throws Throwable {
        Object o = point.proceed();
        Stats.getInst().incrPacketWrites(1);
        return o;
    }

    @Around("execution (* com.microwise.msp.hardware.handler.agent.v13.AgentV13Warden.onPacketReceived(..))")
    public Object aroundPacketReceived(ProceedingJoinPoint point) throws Throwable {
        Object o = point.proceed();
        Stats.getInst().incrPacketWrites(1);
        return o;
    }

    @Around("execution (* com.microwise.msp.hardware.handler.agent.v30.AgentV30Warden.onPacketReceived(..))")
    public Object aroundPacketReceived3(ProceedingJoinPoint point) throws Throwable {
        Object o = point.proceed();
        Stats.getInst().incrPacketWrites(1);
        return o;
    }


    public void print(){
        log.debug("数据包入库次数: {}", Stats.getInst().packetWrites());
    }

}
