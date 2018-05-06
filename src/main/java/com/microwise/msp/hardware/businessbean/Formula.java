package com.microwise.msp.hardware.businessbean;

import java.util.Map;

/**
 * 公式
 * @author gaohui
 * @date 13-12-31 14:16
 */
public class Formula {

    // 无范围限制
    public static final int RANG_TYPE_ANY = 0;
    // 只有最小值限制
    public static final int RANG_TYPE_MIN  = 1;
    // 只有最大值限制性
    public static final int RANG_TYPE_MAX  = 2;
    // 有最小值，最大值限制
    public static final int RANG_TYPE_BOTH  = 3;
    // 超出范围，显示限定值
    public static final int RANG_TYPE_DISPLAY = 4;

    /**
     * 无符号
     */
    public static final int SIGN_TYPE_NO = 0;

    /**
     * 有符号
     */
    public static final int SIGN_TYPE_YES = 1;

    // 公式id
    private int id;

    // 公式名称
    private String name;

    // 公式描述
    private String description;

    // 监测指标标识
    private int sensorId;

    // 设备ID
    private String deviceId;

    // x 最小取值范围
    private int minX;

    // x 最大取值范围
    private int maxX;

    // x 范围类型
    private int xRangeType;

    // y 最小取值范围
    private double minY;

    // y 最小取值范围
    private double maxY;

    // y 范围类型
    private int yRangeType;

    // 原始值符号类型
    private int signType;

    // 公式系数个数
    private int paramCount;

    // 公式系数
    private Map<String, String> formulaParams;

    /**
     * 是否有最小值限制
     *
     * @return
     */
    public boolean isXMinEnable(){
        return xRangeType == RANG_TYPE_MIN || xRangeType == RANG_TYPE_BOTH;
    }

    /**
     * 是否有最大值限制
     *
     * @return
     */
    public boolean isXMaxEnable(){
        return xRangeType == RANG_TYPE_MAX || xRangeType == RANG_TYPE_BOTH;
    }

    /**
     * 是否有最小值限制
     *
     * @return
     */
    public boolean isYMinEnable(){
        return yRangeType == RANG_TYPE_MIN || yRangeType == RANG_TYPE_BOTH;
    }

    /**
     * 是否有最大值限制
     *
     * @return
     */
    public boolean isYMaxEnable(){
        return yRangeType == RANG_TYPE_MAX || yRangeType == RANG_TYPE_BOTH;
    }

    /**
     * 是否显示超出的限定值
     * @return
     */
    public boolean isDisplayYMaxAndMin() {
        return yRangeType == RANG_TYPE_DISPLAY;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getxRangeType() {
        return xRangeType;
    }

    public void setxRangeType(int xRangeType) {
        this.xRangeType = xRangeType;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public int getyRangeType() {
        return yRangeType;
    }

    public void setyRangeType(int yRangeType) {
        this.yRangeType = yRangeType;
    }

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }

    public int getParamCount() {
        return paramCount;
    }

    public void setParamCount(int paramCount) {
        this.paramCount = paramCount;
    }

    public Map<String, String> getFormulaParams() {
        return formulaParams;
    }

    public void setFormulaParams(Map<String, String> formulaParams) {
        this.formulaParams = formulaParams;
    }

    @Override
    public String toString() {
        return "Formula{" +
                "id=" + id +
                ", sensorId=" + sensorId +
                ", deviceId='" + deviceId + '\'' +
                ", minX=" + minX +
                ", maxX=" + maxX +
                ", xRangeType=" + xRangeType +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", yRangeType=" + yRangeType +
                ", signType=" + signType +
                ", paramCount=" + paramCount +
                ", formulaParams=" + formulaParams +
                '}';
    }
}
