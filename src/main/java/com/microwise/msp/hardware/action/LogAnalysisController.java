package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessservice.SiteService;
import com.microwise.msp.hardware.service.DeviceService;
import com.microwise.msp.hardware.service.LogAnalysisService;
import com.microwise.msp.platform.bean.LogicGroup;
import com.microwise.msp.platform.bean.NodeInfo;
import com.microwise.msp.util.ZipCompressor;
import com.opensymphony.xwork2.Action;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * author: chenyaofei
 * date :2016-07-04
 */
@Component
@Scope("prototype")
@Route("/struts")
public class LogAnalysisController {
    @Autowired
    private LogAnalysisService logAnalysisService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SiteService siteService;

    /**
     * 起始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 站点唯一标识
     */
    private String siteId;

    /**
     * 选中的设备
     */
    private String deviceIds;

    /**
     * 设备录入方式
     *
     */
    private String chooseType;
    /**
     * 起始设备号
     */
    private int deviceStart;
    /**
     * 结束设备号
     */
    private int deviceEnd;
    /**
     * 站点组
     */
    private List<LogicGroup> logicGroupList;

    /**
     * 站点下所有设备
     */
    private List<NodeInfo> nodeInfoList;

    /**
     * 下载文件名
     */
    private String fileName;

    /**
     * 下载文件
     */
    private InputStream downloadFile;

    /**
     * 访问页面
     */
    @Route("/logAnalysis")
    public String view() {
        //获取当前时间
        try {
            //获取时间
            startTime = DateTime.now().minusDays(1).toDate();
            endTime = new Date();
            //获取所有站点
            logicGroupList = siteService.findAllLogicGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Results.ftl("/pages/logAnalysis");
    }

    /**
     * 加载设备
     */
    @Route("/allDevice")
    public String allDevice() {
        nodeInfoList = deviceService.findBySiteId2(siteId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("nodeInfoList", nodeInfoList);
        return Results.json().asRoot(data).done();
    }

    /**
     * 执行脚本并下载
     */
    public String doLogAnalysis() {
        String tempDir = UUID.randomUUID().toString();
        String devices;
        if ("interval".equals(chooseType)) {
            devices = deviceIds.replace(" ", "");
        } else {
            devices = makeDeviceIdsArr();
        }
        String shellPath = getClass().getResource("/").getPath() + "logminer" + File.separator + "userReport.sh";
        String sourcePath = System.getProperty("catalina.home") + File.separator + "logs" + File.separator + "blueplanet-daemon" + File.separator;
        String targetPath = sourcePath + "logminer" + File.separator + tempDir + File.separator;
        String beginTimeStr = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startTime) + "'";
        String endTimeStr = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endTime) + "'";
        String command = shellPath + " " + siteId + " " + devices + " " + beginTimeStr + " " + endTimeStr + " " + sourcePath + " " + targetPath;
        try {
            String[] cmdArray = {"chmod +x " + shellPath, command};
             boolean flag = logAnalysisService.doLogAnalysis(cmdArray);
            if(flag){
                downLoad(startTime,endTime,targetPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Action.SUCCESS;
    }

    private String makeDeviceIdsArr() {
        String deviceIdsArr = "";
        for (int i = deviceStart; i <= deviceEnd; i++) {
            deviceIdsArr = deviceIdsArr + String.format("%05d", i) + ",";
        }
        return deviceIdsArr.substring(0, deviceIdsArr.length() - 1);
    }

    public void downLoad(Date startTime, Date endTime, String targetPath) throws FileNotFoundException,UnsupportedEncodingException {
        String start = new SimpleDateFormat("yyMMddHHmm").format(startTime);
        String end = new SimpleDateFormat("yyMMddHHmm").format(endTime);
        String zipFileName = "logAnalysis.zip";
        //压缩
        ZipCompressor zc = new ZipCompressor(targetPath + zipFileName);
        String file1 = targetPath + "logs" + File.separator;
        String file2 = targetPath + "reports" + File.separator;
        String[] files = {file1, file2};
        zc.compress(files);
        //下载
        fileName = "("+start + "-" + end + ")日志分析.zip";
        fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        File f = new File(targetPath, zipFileName);
        downloadFile = new FileInputStream(f);
    }

    public List<LogicGroup> getLogicGroupList() {
        return logicGroupList;
    }

    public void setLogicGroupList(List<LogicGroup> logicGroupList) {
        this.logicGroupList = logicGroupList;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public List<NodeInfo> getNodeInfoList() {
        return nodeInfoList;
    }

    public void setNodeInfoList(List<NodeInfo> nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
    }

    public String getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(InputStream downloadFile) {
        this.downloadFile = downloadFile;
    }

    public String getChooseType() {
        return chooseType;
    }

    public void setChooseType(String chooseType) {
        this.chooseType = chooseType;
    }

    public int getDeviceStart() {
        return deviceStart;
    }

    public void setDeviceStart(int deviceStart) {
        this.deviceStart = deviceStart;
    }

    public int getDeviceEnd() {
        return deviceEnd;
    }

    public void setDeviceEnd(int deviceEnd) {
        this.deviceEnd = deviceEnd;
    }
}
