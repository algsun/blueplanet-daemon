package com.microwise.msp.hardware.servlet;

import com.jcabi.manifests.Manifests;
import com.microwise.msp.hardware.netlink.UDPServer;
import com.microwise.msp.main.Bootstrap;
import com.microwise.msp.util.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Date;

/**
 * 整个应用的监听器, 负责整个应用的启动与关闭
 *
 * @author gaohui
 * @date 13-7-17 16:53
 */
public class MicrowiseListener implements ServletContextListener {
    public static final Logger log = LoggerFactory.getLogger(MicrowiseListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("==============项目启动 start===========");
        sce.getServletContext().setAttribute("app.startTime", new Date());

        // 设置 appContext
        AppContext.getInstance().setAppContext(WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()));
        try {
            Manifests.append(sce.getServletContext());
        } catch (IOException e) {
            log.error("", e);
        }

        try{
        	Bootstrap bootstrap = AppContext.getInstance().getAppContext().getBean(Bootstrap.class);
        	bootstrap.startThread();
        }catch (Exception e){
            log.error("应用启动", e);
        }
        log.info("==============项目启动 end=======");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("==============项目关闭 start==============");
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        UDPServer udpServer = appContext.getBean(UDPServer.class);
        udpServer.shutdown();

        log.info("=============项目关闭 end============");
    }
}
