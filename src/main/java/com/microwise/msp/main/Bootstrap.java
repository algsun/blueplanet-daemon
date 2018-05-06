package com.microwise.msp.main;

import com.microwise.msp.hardware.businessservice.SystemInitService;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.netlink.UDPServer;
import com.microwise.msp.hardware.threads.DataPersistenceThread;
import com.microwise.msp.hardware.threads.SupplymentCacheDataThread;
import com.microwise.msp.hardware.threads.SupplymentGatewayDataThread;
import com.microwise.msp.hardware.threads.TimeoutCheckingThread;
import com.microwise.msp.platform.service.DataTransferService;
import com.microwise.msp.platform.thread.TcpServerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 系统初始化启动类
 *
 * @author heming
 * @since 2011-12-06
 */
@Component
public class Bootstrap {
    private static Logger log = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private SystemInitService systemInit;

    @Autowired
    private UDPServer udpServer;

    @Autowired
    private DataPersistenceThread persistenceTh;
    /**
     * @deprecated 废弃 @gaohui 2014-04-29
     */
    @Autowired
    private TimeoutCheckingThread emptyTh;
    @Autowired
    private SupplymentCacheDataThread supplyth;
    @Autowired
    private SupplymentGatewayDataThread sgdthread;
    @Autowired
    private TcpServerThread tcpServer;
    @Autowired
    private DataTransferService transferInit;

    /**
     * 系统初始化
     *
     * @author he.ming
     * @since 2013-1-8
     */
    public void startInit() {
        systemInit.loadDevicesInfo(); // 加载设备信息
    }

    /**
     * 启动硬件接入子系统
     *
     * @author he.ming
     * @since 2013-1-8
     */
    public void startHardware() {
        // 开启udp监听
        udpServer.bindAll();

        // 数据持久化
        Thread persistence = new Thread(persistenceTh, "DataPersistence");
        persistence.start();

        if (SysConfig.isEmptyCheck) { // 设备状态检查
            // 空数据检查线程（只做对实时数据状态的更改）
            Thread empty = new Thread(emptyTh, "TimeoutChecking");
            empty.start();
        }

        if (SysConfig.isCacheDatabackup) { // 缓存中心数据回补线程
            Thread slyth = new Thread(supplyth, "缓存中心数据回补线程");
            slyth.start();
        }


        /**
         * 已经转移为 quartz 任务 @gaohui 2013-08-07
         if (SysConfig.isSdDatabackup) { // 回补
         // 设置回补状态为未开始回补
         int state = 0;
         SysConfig.sd_state.put("state", state);
         SysConfig.sd_state.put("stamp", DateUtils.getSystemCurrentTime());

         // 网关数据回补线程
         Thread slygd = new Thread(sgdthread);
         slygd.setName("网关数据回补线程");
         slygd.start();
         }
         **/
    }

    /**
     * 硬件接入、网络管理、统计分析
     *
     * @author he.ming
     * @since 2013-1-8
     */
    public void startThread() {
        startInit();
        startHardware();
//		startPlatform();
        startHistoryEmptyData();
        systemInit.initObeliskStatus();
    }

    /**
     * 启动添加历史空数据
     */
    public void startHistoryEmptyData() {
        if (SysConfig.getInstance().isSdDataBackup()) { // 回补
            systemInit.initEmptyData(); // 初始化空数据
        }
    }

}
