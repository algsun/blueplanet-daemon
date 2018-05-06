package com.microwise.msp.hardware.servlet;

import com.google.gson.Gson;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.handler.codec.Versions;
import com.microwise.msp.hardware.service.DeviceService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 网络拓扑图
 *
 * @author gaohui
 * @date 13-9-16 13:04
 */
public class NetworkTopoAjaxServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json ; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");

        String deviceId = req.getParameter("deviceId");
        String siteId = deviceId.substring(0, 8);


        DeviceService deviceService = getAppContext().getBean(DeviceService.class);

        DeviceBean gateway = deviceService.findDeviceById(deviceId);
        Map<String,String> routes = new HashMap<String, String>();
        List<DeviceBean> devices = deviceService.findBySiteId(siteId);
        if (gateway.version == Versions.V_3) {
            filterByVersion(devices, Versions.V_3);
            processRoute(routes, devices, gateway);
        } else if (gateway.version == Versions.V_1) {
            filterByVersion(devices, Versions.V_1);
            // v1.3 协议需要查看子网 处理 v1.3 @gaohui 2013-09-16
            processRouteV13(routes, devices, gateway);
        }

        List<Map<String, String>> routesList = new ArrayList<Map<String, String>>();
        for(Map.Entry<String, String> entry: routes.entrySet() ){
            Map<String, String> route = new HashMap<String, String>();
            route.put("source", entry.getKey());
            route.put("target", entry.getValue());
            routesList.add(route);
        }


        Map<String,Object> data = new HashMap<String, Object>();
        data.put("devices", devices);
        data.put("routes", routesList);

        resp.getWriter().append(new Gson().toJson(data));
    }

    private ApplicationContext getAppContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }

    private static void filterByVersion(Collection<DeviceBean> deviceBeans, int version) {
        for (Iterator<DeviceBean> it = deviceBeans.iterator(); it.hasNext(); ) {
            DeviceBean deviceBean = it.next();
            if (deviceBean.version != version) {
                it.remove();
            }
        }
    }

    /**
     *
     * @param routes srcDeviceId => targetDeviceId
     * @param deviceBeans
     */
    private static void processRoute(Map<String,String> routes, Collection<DeviceBean> deviceBeans,DeviceBean parentDevice){
        int parentIP = parentDevice.selfid;
        String deviceId = parentDevice.deviceid;
        for(DeviceBean deviceBean: deviceBeans){
            if(deviceBean.parentid == parentIP && deviceBean.selfid != deviceBean.parentid){
                routes.put(deviceBean.deviceid, deviceId);
                processRoute(routes, deviceBeans, deviceBean);
            }
        }
    }

    /**
     * v1.3 网络拓扑
     * @param routes srcDeviceId => targetDeviceId
     * @param deviceBeans
     */
    private static void processRouteV13(Map<String,String> routes, Collection<DeviceBean> deviceBeans,DeviceBean parentDevice){
        int parentIP = parentDevice.selfid;
        String deviceId = parentDevice.deviceid;
        String netId = deviceId.substring(8, 10);
        for(DeviceBean deviceBean: deviceBeans){
            String currentNetId = deviceBean.deviceid.substring(8, 10);
            if(deviceBean.parentid == parentIP
                    && deviceBean.selfid != deviceBean.parentid
                    && netId.equals(currentNetId) ){
                routes.put(deviceBean.deviceid, deviceId);
                processRoute(routes, deviceBeans, deviceBean);
            }
        }
    }
}
