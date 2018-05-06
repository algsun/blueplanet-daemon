package com.microwise.msp.hardware.businessbean;

import java.util.List;

/**
 * 控制模块通知设置
 *
 * @author bastengao
 * @date 14-4-28 上午10:59
 */
public class ControlModuleNotification {
    public static final int SUBSCRIBE_TYPE_ALL_DEVICE = 1;
    public static final int SUBSCRIBE_TYPE_CUSTOM_DEVICE = 2;

    public static final int TRIGGER_EVENT_BATTERY_POWER = 1;
    public static final int TRIGGER_EVENT_SWITCH_CHANGE = 2;

    public static final int NOTIFY_METHOD_SMS = 1;
    public static final int NOTIFY_METHOD_EMAIL = 2;


    /**
     * uuid主键
     */
    private String id;

    /**
     * 站点ID
     */
    private String siteId;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 全部设备或者自定义设备，全部设备存1，自定义设备存2
     */
    private int subscribeType;

    /**
     * 发送事件：1设备电池供电，2开关切换，3全选
     */
    private int triggerEvent;

    /**
     * 通知方式：1短信，2邮件，3全选
     */
    private int notifyMethod;

    /**
     * 选择的控制模块
     */
    private List<String> deviceIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubscribeType() {
        return subscribeType;
    }

    public void setSubscribeType(int subscribeType) {
        this.subscribeType = subscribeType;
    }

    public int getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(int triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public int getNotifyMethod() {
        return notifyMethod;
    }

    public void setNotifyMethod(int notifyMethod) {
        this.notifyMethod = notifyMethod;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }
}
