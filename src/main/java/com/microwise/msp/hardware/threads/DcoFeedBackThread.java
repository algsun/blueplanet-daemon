package com.microwise.msp.hardware.threads;

import com.google.common.primitives.Bytes;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.handler.codec.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>
 * 下行命令包0x09--命令响应包0x0A 
 * </pre>
 * 
 * @author he.ming
 * @since Jan 31, 2013
 * @deprecated 废弃 @gaohui 2013-12-27
 */
public class DcoFeedBackThread implements Runnable {
	private static Logger log = LoggerFactory.getLogger(DcoFeedBackThread.class);

	/** 命令响应包[0x0A] */
	private List<Byte> datagram = new ArrayList<Byte>();
	/** 反控service */
	private DcoOperateService dcoService;

    private AtomicBoolean closed = new AtomicBoolean();

    private Queue<Packet> feedbackQueue = SysConfig.getInstance().getFeedbackQueue();

	@Override
	public void run() {
		while (!isClosed()) {
            try {
                while(!feedbackQueue.isEmpty()){
                    Packet packet = feedbackQueue.poll();
                    process(packet);
                }
                Thread.sleep(1);
            } catch (Exception e) {
                log.error("\n\n DcoFeedBackThread→run() \n\n", e);
            }
		}

        log.info("线程关闭");
	}

    private void process(Packet packet){
        // 解析命令响应包
        if (!dcoService.analysisFeedBackPackage(Bytes.asList(packet.getPacket()))) {
            return;
        }
        // 处理命令响应
        dcoService.operateFeedBackForOrder();
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

	public DcoOperateService getDcoService() {
		return dcoService;
	}

	public void setDcoService(DcoOperateService dcoService) {
		this.dcoService = dcoService;
	}

}
