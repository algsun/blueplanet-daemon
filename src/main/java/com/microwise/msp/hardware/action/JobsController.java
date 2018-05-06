package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author gaohui
 * @date 13-11-22 17:18
 */
@Component
@Scope("prototype")
@Route("/struts")
public class JobsController {
    @Autowired
    private Scheduler scheduler;

    @Route("/jobs")
    public String index(){
        return Results.ftl("/pages/jobs");
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
