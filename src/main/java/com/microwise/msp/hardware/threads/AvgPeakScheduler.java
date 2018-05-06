package com.microwise.msp.hardware.threads;

import com.microwise.msp.hardware.businessservice.DataProcessService;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.vo.LocationVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 数据统计定时任务
 *
 * @author li.jianfei
 * @since 17/4/7
 */
@Component
@Configurable
@EnableScheduling
public class AvgPeakScheduler {

    @Autowired
    private DataProcessService dataProcessService;


    @Scheduled(cron = "0 10 * * * ?")
    public void mathHourLux() {
        dataProcessService.mathHourLux();
    }

    @Scheduled(cron = "0 10 * * * ?")
    public void mathHourRb() {
        dataProcessService.mathHourRb();
    }

    @Scheduled(cron = "0 10 * * * ?")
    public void mathHourEvap() {
        dataProcessService.mathHourEvap();
    }


    @Scheduled(cron = "0 0 1 * * ?")
    public void mathWindRoseb() {
        dataProcessService.mathWindRose();
    }


    @Scheduled(cron = "0 0 2 * * ?")
    public void mathAvgPeak() {
        dataProcessService.mathAvgPeak();
    }

    @Scheduled(cron = "0 30 2 * * ?")
    public void mathZoneAvgPeak(){
        // 获取当前时间的前一天
        Date yesterday = new DateTime().minusDays(1).toDate();
        dataProcessService.mathZoneAvgPeak(yesterday);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void mathComplianceRate() {
        dataProcessService.mathComplianceRate();
    }


    @Scheduled(cron = "0 0 9 * * ?")
    public void mathDayRb() {
        dataProcessService.mathDayRb();
    }

}
