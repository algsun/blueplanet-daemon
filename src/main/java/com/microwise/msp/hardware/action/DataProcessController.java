package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessservice.DataProcessService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by lijianfei on 15/4/29.
 */
@Component
@Scope("prototype")
@Route("/struts")
public class DataProcessController {
    public static final Logger log = LoggerFactory.getLogger(DataProcessController.class);


    @Autowired
    private DataProcessService dataProcessService;

    private Date begin;
    private Date end;

    @Route("/statistics/view")
    public String view() {
        begin = DateTime.now().minusDays(1).toDate();
        end = new Date();
        return Results.ftl("/pages/statistics");
    }

    @Route("/statistics")
    public String process() {
        if (begin != null && end != null) {
            while (end.after(begin)) {
                dataProcessService.mathAvgPeak(begin);
                dataProcessService.mathHourLux(begin);
                dataProcessService.mathHourRb(begin);
                dataProcessService.mathDayRb(begin);
                dataProcessService.mathWindRose(begin);
                dataProcessService.mathComplianceRate(begin);
                dataProcessService.mathZoneAvgPeak(begin);
                begin = new DateTime(begin).plusDays(1).toDate();
            }
        }
        return Results.ftl("/pages/statistics");
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
