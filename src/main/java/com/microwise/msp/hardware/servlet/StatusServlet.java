package com.microwise.msp.hardware.servlet;

import com.google.common.base.Strings;
import com.jcabi.manifests.Manifests;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 返回 status
 *
 * @author gaohui
 * @date 13-12-9 14:29
 */
public class StatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");

        Date startTime = (Date) req.getSession().getServletContext().getAttribute("app.startTime");
        JSONObject json = new JSONObject();
        try {
            json.put("name", "blueplanet-daemon");
            json.put("uptime", startTime.getTime());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            json.put("uptime_str", dateFormat.format(startTime));
            String svnRevision = readManifest("App-Svn-Revision");
            if (!Strings.isNullOrEmpty(svnRevision)) {
                json.put("svn_revision", Integer.parseInt(svnRevision));
            }
            String appVersion = readManifest("App-Version");
            if(!Strings.isNullOrEmpty(appVersion)){
                json.put("version", appVersion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resp.getWriter().append(json.toString());
        resp.getWriter().flush();
    }

    private static String readManifest(String key) {
        try {
            return Manifests.read(key);
        } catch(IllegalArgumentException e) { // do nothing
        }

        return null;
    }
}
