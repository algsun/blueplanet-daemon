package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.handler.agent.ThreadWorkers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 显示线程队列情况
 *
 * @author gaohui
 * @date 2014-03-04
 */
@Route("/struts")
@Component
@Scope("prototype")
public class ThreadWorkerController {

    @Autowired
    private ThreadWorkers threadWorkers;

    //output
    // 数据包任务队列
    private int workQueueSize;
    private List<Integer> workQueueSizes;

    @Route("threads")
    public String show(){
        workQueueSize = threadWorkers.workQueueSize();
        workQueueSizes = threadWorkers.workQueueSizes();
        return Results.ftl("/pages/thread-workers");
    }

    public List<Integer> getWorkQueueSizes() {
        return workQueueSizes;
    }

    public int getWorkQueueSize() {
        return workQueueSize;
    }
}
