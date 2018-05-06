package com.microwise.msp.hardware.threads;

import com.microwise.msp.hardware.businessbean.EmptyDataBean;
import com.microwise.msp.hardware.businessservice.SupplymentDataService;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 数据回补
 *
 * TODO 同只能有一个运行 @gaohui 2013-08-07
 *
 */
public class SupplymentCacheDataThread implements Runnable {
    public static final Logger log = LoggerFactory.getLogger(SupplymentCacheDataThread.class);

	/**
	 * 数据回补服务层
	 */
	private SupplymentDataService supplymentDataService;

	/**
	 * 空数据集合
	 */
	List<EmptyDataBean> emptyBeanList = new ArrayList<EmptyDataBean>();

    // 是否关闭
    private AtomicBoolean closed = new AtomicBoolean(false);

	@Override
	public void run() {
		while (!isClosed()) {
			try {
				threadWork();
                Thread.sleep(60000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        log.info("线程关闭");
	}

	private void threadWork() {
		// 获取空数据集合
		emptyBeanList = supplymentDataService
				.getDataCacheEmptyInfo(SysConfig.databackupNum);
		for (EmptyDataBean eb : emptyBeanList) {
//			if (eb.getDataCacheSuccess() == 0) {
				String stamp = DateUtils.getDate(eb.getStamp(),
						"yyyy-MM-dd_HH:mm:ss");
				// 从缓存中心获取数据
				supplymentDataService.getDataCachePacket(eb.getNodeid(), stamp);
//			}
		}

	}

    /**
     * 判断runnable是否关闭
     *
     * @author gaohui
     *
     * @return
     */
    @SuppressWarnings("unused")
    private boolean isClosed() {
        return closed.get();
    }

    /**
     * 关闭线程
     *
     * @author gaohui
     */
    public void shutdown() {
        closed.set(true);
    }

	public SupplymentDataService getSupplymentDataService() {
		return supplymentDataService;
	}

	public void setSupplymentDataService(
			SupplymentDataService supplymentDataService) {
		this.supplymentDataService = supplymentDataService;
	}

}
