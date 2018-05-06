package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.Task;
import com.microwise.msp.hardware.businessbean.User;

/**
 * @author bastengao
 * @date 14-3-20 下午6:05
 */
public interface TaskDao {
    void addTask(Task task);

    void addTaskDesignee(Task task, User user);
}
