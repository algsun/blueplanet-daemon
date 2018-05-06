package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.Task;
import com.microwise.msp.hardware.businessbean.User;
import com.microwise.msp.hardware.dao.TaskDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bastengao
 * @date 14-3-20 下午6:05
 */
@Component
@Scope("prototype")
public class TaskDaoImpl extends BaseDaoImpl implements TaskDao {

    @Override
    public void addTask(Task task){
        getSqlSession().insert("Task.addTask", task);
    }

    @Override
    public void addTaskDesignee(Task task, User user){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("taskId", task.getId());
        params.put("designee", user.getId());
        getSqlSession().insert("Task.addTaskDesignee", params);
    }
}
