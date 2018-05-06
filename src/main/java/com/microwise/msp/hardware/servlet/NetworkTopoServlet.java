package com.microwise.msp.hardware.servlet;

import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.handler.codec.Devices;
import com.microwise.msp.hardware.service.DeviceService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 网络拓扑图
 *
 * @author gaohui
 * @date 13-9-16 13:04
 */
public class NetworkTopoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DeviceService deviceService = getAppContext().getBean(DeviceService.class);
        List<DeviceBean> gateways = deviceService.findByType(Devices.GATEWAY);
        req.setAttribute("gateways", gateways);

        req.getRequestDispatcher("pages/network-topo.jsp").forward(req, resp);
    }

    private ApplicationContext getAppContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }
}
