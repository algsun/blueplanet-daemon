package com.microwise.msp.hardware.threads;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.DataPersistenceService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.DaoMonitor;
import com.microwise.msp.hardware.dao.StatsMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 数据持久化线程
 *
 * @author he.ming
 * @since Feb 18, 2013
 */
public class DataPersistenceThread implements Runnable {

    private static Logger log = LoggerFactory.getLogger(DataPersistenceThread.class);

    private DataPersistenceService dataPersistenceService;

    private AtomicBoolean closed = new AtomicBoolean(false);

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private DaoMonitor daoMonitor;

    @Autowired
    private StatsMonitor statsMonitor;

    @Autowired
    private AppCache appCache;

    private ExecutorService executor;

    public DataPersistenceThread() {
        this.executor = Executors.newFixedThreadPool(2);
    }

    @Override
    public void run() {
        while (!isClosed()) {
            try {
                threadWork();
                Thread.sleep(1);
            } catch (Exception e) {
                log.error("\n\n DataPersistenceThread:  \n\n", e);
            }
        }

        executor.shutdown();

        log.info("线程关闭");
    }

    void threadWork() {
        Queue<DeviceBean> persistenceQ = SysConfig.getInstance().getPersistenceQueue();

        // 如果不为空
        while (!persistenceQ.isEmpty()) {
            long size = persistenceQ.size();
            log.debug("入库队列大小：" + size);

            if (SysConfig.getInstance().isPacketCacheMode()) {
                dataPersistenceService.saveCacheBatch(pollBatch(persistenceQ));
            }else{
                dataPersistenceService.saveBatch(pollBatch(persistenceQ));
            }

            daoMonitor.print();
            statsMonitor.print();
        }
    }

    /**
     * 批量取出, 没有东西返回空的集合
     *
     * @param persistenceQ
     * @return
     */
    private List<DeviceBean> pollBatch(Queue<DeviceBean> persistenceQ) {
        int batchCont = 10;
        List<DeviceBean> deviceBeans = new ArrayList<DeviceBean>();

        for (int i = 0; i < batchCont; i++) {
            DeviceBean deviceBean = persistenceQ.poll();
            if (deviceBean == null) {
                break;
            }

            deviceBeans.add(deviceBean);
        }

        return deviceBeans;
    }

    /**
     * 判断runnable是否关闭
     *
     * @return
     * @author he.ming
     * @since Jan 29, 2013
     */
    @SuppressWarnings("unused")
    private boolean isClosed() {
        return closed.get();
    }

    /**
     * 关闭线程
     *
     * @author he.ming
     * @since Jan 29, 2013
     */
    public void shutdown() {
        closed.set(true);
    }

    public void setDataPersistenceService(
            DataPersistenceService dataPersistenceService) {
        this.dataPersistenceService = dataPersistenceService;
    }

}
