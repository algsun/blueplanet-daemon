package com.microwise.msp.hardware.businessbean;

import java.util.Date;

/**
 * Created by liuzhu on 15-2-26.
 */
public class Stock {

    /**
     * 自增id
     */
    private String id;

    /**
     * 位置点id
     */
    private String locationId;

    /**
     * 监测指标id
     */
    private int sensorId;

    /**
     * 开始值
     */
    private double beginValue;

    /**
     * 结束值
     */
    private double endValue;

    /**
     * 最大值
     */
    private double maxValue;

    /**
     * 最小值
     */
    private double minValue;

    /**
     * k
     */
    private double k;

    /**
     * d
     */
    private double d;

    /**
     * j
     */
    private double j;

    /**
     * 5日均线
     */
    private float avgValue5;


    /**
     * 10日均线
     */
    private float avgValue10;


    /**
     * 30日均线
     */
    private float avgValue30;

    /**
     * 时间
     */
    private Date stamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public double getBeginValue() {
        return beginValue;
    }

    public void setBeginValue(double beginValue) {
        this.beginValue = beginValue;
    }

    public double getEndValue() {
        return endValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public float getAvgValue5() {
        return avgValue5;
    }

    public void setAvgValue5(float avgValue5) {
        this.avgValue5 = avgValue5;
    }

    public float getAvgValue10() {
        return avgValue10;
    }

    public void setAvgValue10(float avgValue10) {
        this.avgValue10 = avgValue10;
    }

    public float getAvgValue30() {
        return avgValue30;
    }

    public void setAvgValue30(float avgValue30) {
        this.avgValue30 = avgValue30;
    }

    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getJ() {
        return j;
    }

    public void setJ(double j) {
        this.j = j;
    }
}
