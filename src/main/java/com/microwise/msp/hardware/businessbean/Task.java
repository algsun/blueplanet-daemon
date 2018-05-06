package com.microwise.msp.hardware.businessbean;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.Set;

/**
 * 任务 实体
 *
 * @author xubaoji
 * @date 2013-6-8
 */
public class Task {

    /**
     * 任务id主键
     */
    private int id;

    /**
     * 任务标题
     */
    private String taskTitle;

    /**
     * 任务描述信息
     */
    private String taskInfo;

    /**
     * 发布时间
     */
    private Date releaseDate;

    /**
     * 截至时间
     */
    private Date endDate;

    /**
     * 发布人
     */
    private int releaser;

    /**
     * 接受人 列表
     */
    private Set<User> designees;

    /**
     * 完成进度 此处为int 数字 ，页面需展示 百分比 如 20%
     */
    private int completeStatus;

    /**
     * 任务状态 false：未结束 true ：已结束
     */
    private boolean state;

    /**
     * 站点编号
     */
    private int logicGroupId;

    public Task() {
        super();
    }

    public Task(int logicGroupId, String taskTitle, String taskInfo, int releaser) {
        this.logicGroupId = logicGroupId;
        this.taskTitle = taskTitle;
        this.taskInfo = taskInfo;
        this.releaseDate = new Date();
        this.endDate = DateTime.now().plusDays(5).toDate();
        this.releaser = releaser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getReleaser() {
        return releaser;
    }

    public void setReleaser(int releaser) {
        this.releaser = releaser;
    }

    public Set<User> getDesignees() {
        return designees;
    }

    public void setDesignees(Set<User> designees) {
        this.designees = designees;
    }

    public Integer getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(Integer completeStatus) {
        this.completeStatus = completeStatus;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getLogicGroupId() {
        return logicGroupId;
    }

    public void setLogicGroupId(int logicGroupId) {
        this.logicGroupId = logicGroupId;
    }
}
