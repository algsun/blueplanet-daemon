package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.microwise.msp.util.LogQueue;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * 日志
 *
 * @author gaohui
 * @date 13-11-14 21:27
 */
@Component
@Scope("prototype")
@Route("/struts")
public class LogsController {
    // input
    private Set<String> deviceIds;
    private Set<String> gateways;

    @Route("/logs")
    public String index() {
        gateways = LogQueue.getInstance().getGateways();
        deviceIds = LogQueue.getInstance().getDeviceIds();
        return Results.ftl("/pages/logs");
    }

    @Route("/logs.json")
    public String logs() {
        ActionContext.getContext().put("logs", LogQueue.getInstance().logs());
        return Results.json().root("logs").done();
    }

    // input
    // 当前目录， 相对于日志目录
    private String dir = "";
    //output
    private List<File> files = new ArrayList<File>();

    @Route("/logs/files")
    public String files() {
        if (Strings.isNullOrEmpty(dir)) {
            dir = "";
        }
        File pwd = new File(logDir(), dir);
        File[] filesArray = pwd.listFiles();
        if (files != null) {
            files = Lists.newArrayList(filesArray);
            // 时间倒序
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o2.lastModified() > o1.lastModified()) return 1;
                    else if (o2.lastModified() == o1.lastModified()) return 0;
                    else return -1;
                }
            });
        }
        return Results.ftl("/pages/logs-files");
    }

    //input
    private String file;
    //output
    private InputStream inputStream;
    private String fileName;

    // 日志下载
    public String download() throws FileNotFoundException {
        File f = new File(logDir(), file);
        inputStream = new FileInputStream(f);
        fileName = f.getName();
        return Action.SUCCESS;
    }

    /**
     * 返回 blueplanet-daemon 文件目录
     *
     * @return
     */
    private String logDir() {
        return System.getProperty("catalina.home") + "/logs/blueplanet-daemon";
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Set<String> getDeviceIds() {
        return deviceIds;
    }

    public Set<String> getGateways() {
        return gateways;
    }
}
