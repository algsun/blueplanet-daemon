package com.microwise.msp.hardware.threads;

import com.microwise.msp.hardware.businessbean.EmptyDataBean;
import com.microwise.msp.hardware.businessservice.SupplymentDataService;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 数据回补
 *
 * TODO 同只能有一个运行 @gaohui 2013-08-07
 *
 */
public class SupplymentGatewayDataThread implements Runnable {
    public static final Logger log = LoggerFactory.getLogger(SupplymentGatewayDataThread.class);

	/**
	 * 数据回补服务层
	 */
	private SupplymentDataService supplymentDataService;
	/**
	 * 空数据队列
	 */
	Queue<EmptyDataBean> emptyDataQueue = new LinkedList<EmptyDataBean>();

    // 是否关闭
    private AtomicBoolean closed = new AtomicBoolean(false);

	@Override
	public void run() {
		while (!isClosed()) {
			try {
				threadWork();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        log.info("线程关闭");
	}

	private void threadWork() {
        // TODO 可改为局部变量 @gaohui 2013-09-03
		// 获取空数据集合
		emptyDataQueue.addAll(supplymentDataService
				.getGatewayEmptyInfo(SysConfig.databackupNum));
		supplySdData();
	}

	private void supplySdData() {
		while (!emptyDataQueue.isEmpty()) {
			int state = Integer.valueOf(SysConfig.sd_state.get("state")
					.toString());
			Date stamp = (Date) SysConfig.sd_state.get("stamp");
			if (state == 0
					|| (new Date().getTime()
							- stamp.getTime() > SysConfig.sd_timeout)) {
				EmptyDataBean emptyDataBean = emptyDataQueue.poll();
				if (emptyDataBean != null
						&& emptyDataBean.getGatewaySuccess() == 0
						&& !isLargerThanCurrentHour(emptyDataBean.getStamp())) {
					// 从SD卡获取数据
					supplymentDataService.getGatewayPacket(emptyDataBean
							.getStamp(), emptyDataBean.getNodeid());
					// 设置回补状态为正在回补
					SysConfig.sd_state.put("state", 1);
					SysConfig.sd_state.put("stamp", new Date());
				}
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断回补时间是否大于当前时间
	 * 
	 * @param timestamp
	 */
	private boolean isLargerThanCurrentHour(Timestamp timestamp) {
		long curtime = DateUtils.getDate(DateUtils.getDate(new Date(), "yyyy-MM-dd HH:00:00")).getTime();
		long memtime = DateUtils.getDate(
				DateUtils.getDate(timestamp, "yyyy-MM-dd HH:00:00")).getTime();
		if (memtime >= curtime) {
			return true;
		}
		return false;
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
