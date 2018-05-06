package com.microwise.msp.hardware.servlet;

import com.google.common.base.Strings;
import com.jcabi.manifests.Manifests;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.netlink.NetLinkService;
import com.microwise.msp.hardware.vo.NetInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * root serlvet
 *
 * @author gaohui
 * @date 13-8-30 11:08
 * @deprecated 由 struts IndexAction 替代
 */
public class IndexServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Manifests.append(config.getServletContext());
        } catch (IOException e) {
            // do nothing
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());

        NetLinkService netLinkService = appContext.getBean(NetLinkService.class);
        List<NetInfo> netinfos = netLinkService.getUdpPorts();
        req.setAttribute("netinfos", netinfos);
        req.setAttribute("appCache", appContext.getBean(AppCache.class));
        initSvnRevision(req);

        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    // 加载版本号
    private void initSvnRevision(HttpServletRequest request) {
        try {
            String svnRevision = Manifests.read("App-Svn-Revision");
            if (!Strings.isNullOrEmpty(svnRevision)) {
                request.setAttribute("svnRevision", svnRevision);
            }
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }
}
