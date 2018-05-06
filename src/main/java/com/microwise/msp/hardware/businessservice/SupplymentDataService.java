package com.microwise.msp.hardware.businessservice;

import com.google.common.primitives.Bytes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microwise.msp.hardware.businessbean.DataPacket;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.EmptyDataBean;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.DeviceDao;
import com.microwise.msp.hardware.dao.SupplymentDataDao;
import com.microwise.msp.hardware.handler.codec.MultiVersionPacketSplitter;
import com.microwise.msp.hardware.handler.codec.Packet;
import com.microwise.msp.hardware.handler.codec.PacketSplitter;
import com.microwise.msp.util.DateUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class SupplymentDataService {

	private static Logger log = LoggerFactory.getLogger(SupplymentDataService.class);

	private SupplymentDataDao supplymentDataDao;

	private DcoOperateService dcoOperateService;

    @Autowired
    private DeviceDao deviceDao;

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 */
	public String doGet(String url, String queryString) {
		String response = null;
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			if (queryString != null && queryString != "") {
				method.setQueryString(URIUtil.encodeQuery(queryString));
			}
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				response = method.getResponseBodyAsString();
			}
		} catch (Exception e) {
			log.error("执行HTTP Get请求" + url + "时，发生异常！", e);
		} finally {
			method.releaseConnection();
		}
		return response;
	}

	/**
	 * 从数据缓存获取数据包
	 * 
	 * @param nodeid
	 *            请求的监测点号
	 * @param timestamp
	 *            请求的时间戳
	 */
	public void getDataCachePacket(String nodeid, String timestamp) {
		log.info("节点" + nodeid + ",时间:" + timestamp + ",开始请求缓存中心获取数据");
		String data = doGet(SysConfig.dataCacheUrl, "?nodeid=" + nodeid
				+ "&timestamp=" + timestamp);
		List<DataPacket> packetList = new ArrayList<DataPacket>();
        // TODO 比较有问题 @gaohui 2013-09-05
		if (StringUtils.isNotBlank(data)) {
			// 修改监测点状态
			EmptyDataBean emptyDataBean = new EmptyDataBean();
			emptyDataBean.setNodeid(nodeid);
			emptyDataBean.setStamp(new Timestamp(DateUtils.getDate(timestamp,
					"yyyy-MM-dd_HH:mm:ss").getTime()));
			emptyDataBean = supplymentDataDao.getEmptyInfo(emptyDataBean);
			emptyDataBean.setDataCacheSuccess(emptyDataBean
					.getDataCacheSuccess() + 1);
			supplymentDataDao.updateCacheSign(emptyDataBean);
			if (!data.trim().equals("nodata")) {
				Gson gson = new Gson();
				packetList = gson.fromJson(data,
						new TypeToken<List<DataPacket>>() {
						}.getType());
			}
		}

		for (DataPacket pet : packetList) {
			String packet = pet.packet;
			List<Byte> datagram = hexStringToBytes(packet);

			// 根据监测点号获取IP和端口信息
			DeviceBean devicebean = deviceDao.findById(pet.nodeid);
			if (devicebean != null) {
				// 协议版本
                int version = Integer.parseInt(packet.substring(8, 10));

                // 将4组数据合成一包 @gaohui 2013-07-18
//                Packet p = new Packet(datagram, version, devicebean.remoteAddress, devicebean.remotePort);

//                SysConfig.getInstance().getDataQueue().offer(p);
                PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
                ByteBuffer buf = ByteBuffer.wrap(Bytes.toArray(datagram));
                while (buf.hasRemaining()) {
                    Packet p = packetSplitter.split(buf);
                    if (packet == null) {
                        break;
                    }
                    p.setRemote(new InetSocketAddress(devicebean.remoteAddress, devicebean.remotePort));
                    p.setRemoteHost(devicebean.remoteAddress);
                    p.setRemotePort(devicebean.remotePort);
                }
			}
		}
	}

	public void getGatewayPacket(Timestamp timestamp, String deviceId) {
		dcoOperateService.coveringDataSingle(timestamp, deviceId);
	}

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	public List<EmptyDataBean> getDataCacheEmptyInfo(int maxSupplyCount) {
		return supplymentDataDao.getDataCacheEmptyInfo(maxSupplyCount);
	}

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	public List<EmptyDataBean> getGatewayEmptyInfo(int maxSupplyCount) {
		return supplymentDataDao.getGatewayEmptyInfo(maxSupplyCount);
	}

	/**
	 * 获取空数据记录
	 * 
	 * @return 空数据记录
	 */
	public EmptyDataBean getEmptyInfo(EmptyDataBean emptyDataBean) {
		return supplymentDataDao.getEmptyInfo(emptyDataBean);
	}

	/**
	 * 修改网关空数据记录
	 * 
	 * @param emptyDataBean
	 *            修改空数据记录
	 * @return 操作结果 true 成功 , false 失败
	 */
	public Boolean updateGatewaySign(EmptyDataBean emptyDataBean) {
		return supplymentDataDao.updateGatewaySign(emptyDataBean);
	}

	/**
	 * 修改缓存中心空数据记录
	 * 
	 * @param emptyDataBean
	 *            修改空数据记录
	 * @return 操作结果 true 成功 , false 失败
	 */
	public Boolean updateCacheSign(EmptyDataBean emptyDataBean) {
		return supplymentDataDao.updateCacheSign(emptyDataBean);
	}

	/**
	 * 16进制字符串转换字节集合
	 * 
	 * @param hexString
	 *            16进制字符串
	 * @return 数据包字节集合
	 */
	public List<Byte> hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		List<Byte> d = new ArrayList<Byte>();
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			byte b = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
			d.add(b);
		}
		return d;
	}

	/**
	 * 转换 char 到 byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static void main(String[] args) {
		new SupplymentDataService().getDataCachePacket("0000131300256",
				"2013-01-30_17:49:31");
	}

	public void setSupplymentDataDao(SupplymentDataDao supplymentDataDao) {
		this.supplymentDataDao = supplymentDataDao;
	}

	public void setDcoOperateService(DcoOperateService dcoOperateService) {
		this.dcoOperateService = dcoOperateService;
	}

}