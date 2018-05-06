package com.microwise.msp.hardware.businessbean;

/**
 * 区域对应的阈值对象
 *
 * @author: wanggeng
 * @date: 13-8-26 下午3:05
 */
public class ThresholdCacheBean {

    // 可打扰
    public static final int DISTURB_YES = 0;

    // 免打扰
    public static final int DISTURB_NO = 1;

    //-------m_threshold_user,t_users
    /**
     * 用户ID
     */
    private int userId;

    /**
     * 用户手机
     */
    private String mobile;

    //-------m_threshold,t_zone
    /**
     * 区域id
     */
    private String zoneId;

    /**
     * 区域名称
     */
    private String zoneName;

    /**
     * 传感量标识
     */
    private int sensorPhysicalid;

    /**
     * 传感中文名称
     */
    private String sensorName;

    /**
     * 传感单位
     */
    private String units;
    /**
     * 阈值上限
     */
    private float maxValue;
    /**
     * 阈值下限
     */
    private float minValue;

    //-------m_threshold_option
    /**
     * 免打扰时段：时间点之前
     */
    private String beforeTime;
    /**
     * 免打扰时段：时间点之后
     */
    private String afterTime;
    /**
     * 是否设置免打扰时段：0 不设置 ， 1 设置
     */
    private int isNoDisturb;
    /**
     * 通知类型：1 短信，2 邮件
     */
    private int notificationType;

    public String getZoneId() {
        return zoneId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public int getSensorPhysicalid() {
        return sensorPhysicalid;
    }

    public void setSensorPhysicalid(int sensorPhysicalid) {
        this.sensorPhysicalid = sensorPhysicalid;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public String getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(String beforeTime) {
        this.beforeTime = beforeTime;
    }

    public String getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(String afterTime) {
        this.afterTime = afterTime;
    }

    public int getNoDisturb() {
        return isNoDisturb;
    }

    public void setNoDisturb(int noDisturb) {
        isNoDisturb = noDisturb;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }
}
