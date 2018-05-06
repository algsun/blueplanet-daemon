package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessservice.DataProcessService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author liuzhu
 * @date 2015-3-10
 */
@Component
@Scope("prototype")
@Route("/struts")
public class KDJController {

    @Autowired
    private DataProcessService dataProcessService;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    @Route("/kdj")
    public String index() {
        Date date = new Date();
        startDate = DateTime.now().minusDays(1).toDate();
        endDate = date;
        return Results.ftl("/pages/kdj");
    }

    @Route("/mathKDJ")
    public String handMathKDJ() {
        dataProcessService.handMathKDJ(startDate, endDate);
        return Results.ftl("/pages/kdj");
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
