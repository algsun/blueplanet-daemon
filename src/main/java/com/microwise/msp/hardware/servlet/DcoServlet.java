package com.microwise.msp.hardware.servlet;

import com.google.common.base.Strings;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import com.microwise.msp.util.AppContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 反控Servlet
 *
 * @author he.ming
 * @since Feb 5, 2013
 * @deprecated
 */
public class DcoServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(DcoServlet.class);
    private static final long serialVersionUID = 1L;

    /**
     * 修改工作周期
     */
    static final String MODIFY_INTERVAL = "modifyInterval";
    /**
     * 开启巡检
     */
    static final String POLLING_OPEN = "pollingOpen";
    /**
     * 关闭巡检
     */
    static final String POLLING_CLOSE = "pollingClose";

    DcoOperateService dcoService = AppContext.getInstance().getAppContext().getBean(DcoOperateService.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String method = request.getParameter("method");
        method = Strings.nullToEmpty(method).trim();

        String deviceId = request.getParameter("deviceId");
        deviceId = Strings.nullToEmpty(deviceId).trim();

        String siteId = request.getParameter("siteId");
        siteId = Strings.nullToEmpty(siteId).trim();

        String intervalStr = request.getParameter("interval");
        intervalStr = Strings.nullToEmpty(intervalStr).trim();

        int interval = 600;
        if (!Strings.isNullOrEmpty(intervalStr)) {
            interval = Integer.parseInt(intervalStr);
        }

        try {
            if (method.equals(MODIFY_INTERVAL)) { // 修改工作周期
//                boolean result = dcoService.modifyInterval(deviceId, interval);
//                outBack(out, result, method);
            } else if (method.equals(POLLING_OPEN)) { // 开启巡检
//                boolean result = dcoService.openPolling(siteId, interval);
//                outBack(out, result, method);
            } else if (method.equals(POLLING_CLOSE)) { // 退出巡检
//                boolean result = dcoService.closePolling(siteId);
//                outBack(out, result, method);
            }
        } catch (Exception e) {
            log.error("", e);
            outBack(out, false, method);
        }
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void outBack(PrintWriter out, boolean success, String operate) {
        JSONObject json = new JSONObject();
        try {
            json.put("success", success);
            json.put("operate", operate);
            out.write(json.toString());
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
