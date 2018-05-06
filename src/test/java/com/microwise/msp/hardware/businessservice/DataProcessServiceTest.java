package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.util.DateUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/appContext.xml"})
public class DataProcessServiceTest {
	
	@Autowired
	private DataProcessService dataProcessService;

    @Ignore
	@Test
	public void testGetAvgPeakInfo(){
        Date endTime = DateUtils.getDate("2012-02-16 00:00:00");
        Date currentTime = DateUtils.getDate("2013-09-29 00:00:00");
        while(currentTime.after(endTime)){
            dataProcessService.mathAvgPeak(currentTime);
            currentTime = new DateTime(currentTime).minusDays(1).toDate();
        }
		dataProcessService.mathAvgPeak(DateUtils.getDate("2012-09-09 02:00:00"));
	}
	@Ignore
	@Test
	public void testMathHourLux(){
		dataProcessService.mathHourLux("6101150110200",  DateUtils.getDate("2013-08-15 09:00:00"));
	}
	
	@Ignore
	@Test
	public void testMathHourRb(){
		dataProcessService.mathHourRb("6101150110200",  DateUtils.getDate("2013-08-15 00:00:00"));
	}
	
	@Ignore
	@Test
	public void testMathDayRb(){
		dataProcessService.mathDayRb("6101150110200",  DateUtils.getDate("2013-07-29 03:00:00"));
	}
	@Ignore
	@Test
	public void testMathWindRose(){
		dataProcessService.mathWindRose("1000000157920",  DateUtils.getDate("2013-11-20 03:00:00"));
	}

}
