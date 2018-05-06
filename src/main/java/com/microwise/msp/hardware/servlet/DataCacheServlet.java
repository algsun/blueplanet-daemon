package com.microwise.msp.hardware.servlet;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.microwise.msp.hardware.businessbean.DataPacket;
import com.microwise.msp.hardware.dao.DataPacketDao;
import com.microwise.msp.util.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据回补--接口
 * 
 * @author heming
 * @date 2012-5-29
 */
public class DataCacheServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(DataCacheServlet.class);

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd_HH:mm:ss");

    // TODO 没有事务 @gaohui 2013-08-07
    DataPacketDao dataPacketDao = AppContext.getInstance().getAppContext().getBean(DataPacketDao.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String nodeIdStr = req.getParameter("nodeid");
		String timestampStr = req.getParameter("timestamp"); // "2012-05-18_12:34:56"

		// 如果参数为null
		if (Strings.isNullOrEmpty(nodeIdStr)
				|| Strings.isNullOrEmpty(timestampStr)) {
			respondError(resp, "parameters ara invalid");
			return;
		}

		// 解析 timestamp
		Date timestamp = null;
		try {
			timestamp = SIMPLE_DATE_FORMAT.parse(timestampStr);
		} catch (ParseException e) {
			log.debug("parameter", e);
			respondError(resp, "timestamp is invalid");
			return;
		}

		respondWithoutPaging(resp, nodeIdStr, timestamp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	/**
	 * 提供无分页的接口
	 * 
	 * @param resp
	 * @param subnetId
	 * @param deviceId
	 * @param timestamp
	 * @throws IOException
	 */
	private void respondWithoutPaging(HttpServletResponse resp, String nodeId,
			Date timestamp) throws IOException {
		try {
			nodeId = "packet_" + nodeId;
			List<DataPacket> packets = new ArrayList<DataPacket>();
			packets = dataPacketDao.findPackets(nodeId, timestamp);

			Gson gson = new Gson();

			jsonResponse(resp);

			if (packets == null || packets.isEmpty()) {
				resp.getWriter().append("nodata");
			} else {
				resp.getWriter().append(gson.toJson(packets));
			}

			resp.getWriter().flush();
			resp.getWriter().close();

			return;
		} catch (Exception e) {
			log.error("query", e);
			respondError(resp, "query error");
			return;
		}
	}

	/**
	 * 返回错误信息
	 * 
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	private void respondError(HttpServletResponse response, String msg)
			throws IOException {
		jsonResponse(response);
		response.getWriter().append(
				String.format("{\"msg\":\"%s\",\"error\":true}", msg));
		response.getWriter().flush();
	}

	/**
	 * 设置 json response 编码和类型
	 * 
	 * @param response
	 */
	private void jsonResponse(HttpServletResponse response) {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
	}

}
