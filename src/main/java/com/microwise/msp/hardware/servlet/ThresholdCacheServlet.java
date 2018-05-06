package com.microwise.msp.hardware.servlet;

import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.util.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 清除区域阈值缓存
 *
 * @author li.jianfei
 * @date 2013-09-02
 *
 * @check xu.baoji 2013-09-02 #5359
 */
public class ThresholdCacheServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DataCacheServlet.class);

    private AppCache appCache = AppContext.getInstance().getAppContext().getBean(AppCache.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String zoneId = req.getParameter("zoneId");
        // TODO 已经去掉缓存，暂时保留接口 @gaohui 2014-03-21
        // appCache.evictThresholdCache(zoneId);
        resp.getWriter().append("{\"success\":true}");
        resp.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
