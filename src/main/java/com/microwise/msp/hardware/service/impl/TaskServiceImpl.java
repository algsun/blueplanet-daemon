package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.businessbean.Task;
import com.microwise.msp.hardware.businessbean.User;
import com.microwise.msp.hardware.dao.TaskDao;
import com.microwise.msp.hardware.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author bastengao
 * @date 14-3-20 下午6:20
 */
@Component
@Scope("prototype")
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Override
    public void addTask(Task task, List<User> assignees) {
        taskDao.addTask(task);
        for(User user: assignees){
            taskDao.addTaskDesignee(task, user);
        }
    }
}
