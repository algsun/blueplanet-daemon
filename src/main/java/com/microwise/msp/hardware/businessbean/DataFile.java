package com.microwise.msp.hardware.businessbean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiedeng
 * @date 14-8-7
 */
public class DataFile implements Serializable {
    /**
     * 自增编号
     */
    private int id;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 上传文件的时间
     */
    private Date uploadTime;

    /**
     * 文件解析的标记
     */
    private int analysisSign;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getAnalysisSign() {
        return analysisSign;
    }

    public void setAnalysisSign(int analysisSign) {
        this.analysisSign = analysisSign;
    }
}
