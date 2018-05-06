package com.microwise.msp.hardware.businessbean;

/**
 * 监测指标
 *
 * @author gaohui
 * @date 13-7-19 10:12
 */
public class Sensor {
    // 有效
    public static final int ACTIVE_TRUE = 1;
    // 无效
    public static final int ACTIVE_FALSE = 0;

    //  默认
    public static final int SHOW_TYPE_DEFAULT = 0;
    // 风向类
    public static final int SHOW_TYPE_WIND = 1;

    // 无范围限制
    public static final int RANG_TYPE_ANY = 0;
    // 只有最小值限制
    public static final int RANG_TYPE_MIN = 1;
    // 只有最大值限制性
    public static final int RANG_TYPE_MAX = 2;
    // 有最小值，最大值限制
    public static final int RANG_TYPE_BOTH = 3;

    /**
     * 无符号
     */
    public static final int SIGN_TYPE_NO = 0;

    /**
     * 有符号
     */
    public static final int SIGN_TYPE_YES = 1;

    /**
     * id
     */
    private int id;

    /**
     * 这才是他妈的 id, 上面的 id 是假的
     */
    private int physicalId;

    /**
     * 需要转义的监测指标编号
     */
    private int escapeSensorId;

    /**
     * 英文名
     */
    private String enName;

    /**
     * 中文名
     */
    private String cnName;

    /**
     * 精度(几位小数点)
     */
    private int precision;

    /**
     * 显示拉(监测指标显示顺序)
     */
    private int position;

    /**
     * 单位
     */
    private String unit;

    /**
     * 是否有效
     */
    private int isActive;

    /**
     * 显示类型
     */
    private int showType;

    /**
     * 最小值
     */
    private double minValue;

    /**
     * 最大值
     */
    private double maxValue;

    /**
     * 无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;
     */
    private int rangeType;

    /**
     * 原始数据符号类型(0 无符号, 1 有符号)
     */
    private int signType;

    /**
     * 条件类型 1-数值范围；2-大于；3-小于；4-大于等于；5-小于等于
     */
    private int conditionType;

    /**
     * 目标值
     */
    private float target;

    /**
     * 浮动值
     */
    private float floating;


    /**
     * 是否有最小值限制
     *
     * @return
     */
    public boolean isMinEnable() {
        return rangeType == RANG_TYPE_MIN || rangeType == RANG_TYPE_BOTH;
    }

    /**
     * 是否有最大值限制
     *
     * @return
     */
    public boolean isMaxEnable() {
        return rangeType == RANG_TYPE_MAX || rangeType == RANG_TYPE_BOTH;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhysicalId() {
        return physicalId;
    }

    public void setPhysicalId(int physicalId) {
        this.physicalId = physicalId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getActive() {
        return isActive;
    }

    public void setActive(int active) {
        isActive = active;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public int getRangeType() {
        return rangeType;
    }

    public void setRangeType(int rangeType) {
        this.rangeType = rangeType;
    }

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }

    public int getEscapeSensorId() {
        return escapeSensorId;
    }

    public void setEscapeSensorId(int escapeSensorId) {
        this.escapeSensorId = escapeSensorId;
    }

    public int getConditionType() {
        return conditionType;
    }

    public void setConditionType(int conditionType) {
        this.conditionType = conditionType;
    }

    public float getTarget() {
        return target;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public float getFloating() {
        return floating;
    }

    public void setFloating(float floating) {
        this.floating = floating;
    }
}
