package com.microwise.msp.hardware.businessbean;

import java.util.Date;
import java.util.List;

/**
 * 区域阈值
 *
 * @author bastengao
 * @date 14-3-19 下午2:24
 */
public class ThresholdAlarm {
    // 短信
    public static final int NOTIFICATION_SMS = 1;
    // 邮件
    public static final int NOTIFICATION_EMAIL = 2;

    // 可打扰
    public static final int DISTURB_YES = 0;

    // 免打扰
    public static final int DISTURB_NO = 1;

    // ID
    private String id;

    // 区域ID
    private String zoneId;

    // 是否免打扰
    private int isNoDisturb;

    // 免打扰时段
    private Date afterTime;
    private Date beforeTime;

    // 通知类型
    private int notificationType;

    // 我的任务发布人
    private int taskReleaser;

    // 通知人
    private List<User> notificationUsers;

    private List<Threshold> thresholds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public int getIsNoDisturb() {
        return isNoDisturb;
    }

    public void setIsNoDisturb(int isNoDisturb) {
        this.isNoDisturb = isNoDisturb;
    }

    public Date getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(Date beforeTime) {
        this.beforeTime = beforeTime;
    }

    public Date getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(Date afterTime) {
        this.afterTime = afterTime;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public int getTaskReleaser() {
        return taskReleaser;
    }

    public void setTaskReleaser(int taskReleaser) {
        this.taskReleaser = taskReleaser;
    }

    public List<User> getNotificationUsers() {
        return notificationUsers;
    }

    public void setNotificationUsers(List<User> notificationUsers) {
        this.notificationUsers = notificationUsers;
    }

    public List<Threshold> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<Threshold> thresholds) {
        this.thresholds = thresholds;
    }
}
