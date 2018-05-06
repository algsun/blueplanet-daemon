package com.microwise.msp.hardware.service;

import com.microwise.msp.hardware.businessbean.Task;
import com.microwise.msp.hardware.businessbean.User;

import java.util.List;

/**
 * @author bastengao
 * @date 14-3-20 下午6:20
 */
public interface TaskService {
    void addTask(Task task, List<User> assignees);
}
