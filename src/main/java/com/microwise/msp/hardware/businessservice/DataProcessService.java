/**
 * Jan 24, 2013
 */
package com.microwise.msp.hardware.businessservice;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.dao.DataProcessDao;
import com.microwise.msp.hardware.dao.LocationDao;
import com.microwise.msp.hardware.dao.ThresholdDao;
import com.microwise.msp.hardware.service.ThresholdService;
import com.microwise.msp.hardware.vo.LocationVo;
import com.microwise.msp.hardware.vo.NodeSensor;
import com.microwise.msp.util.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 数据统计业务类
 *
 * @author he.ming
 * @since Jan 24, 2013
 */
//TODO 数据统计需要处理位置点信息
@Component
@Scope("prototype")
@Transactional
public class DataProcessService {

    private static final String DATE = "yyyy-MM-dd";

    private static final String MONTH = "MM";

    private static Logger log = LoggerFactory.getLogger(DataProcessService.class);

    @Autowired
    private AppCache appCache;

    @Autowired
    private DataProcessDao dao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private AnalysisDao analysisDao;

    @Autowired
    private ThresholdService thresholdService;

    /**
     * 计算小时光照量
     */
    public void mathHourLux() {
        log.info("小时光照量统计开始");
        // 获取当前时间的前一小时
        Date beforeHour = new DateTime().minusHours(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._LUX); // 设备列表

        // 计算小时照度累积量
        for (LocationVo n : locationIds) {
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathHourLux(locationId, beforeHour);
            }
        }
        log.info("小时光照量统计结束");
    }

    /**
     * 计算小时光照量
     *
     * @param date 开始时间
     */
    public void mathHourLux(Date date) {
        log.info("小时光照量统计开始");
        for (int i = 0; i < 24; i++) {
            // 获取当前时间的前一小时
            Date beforeHour = new DateTime(date).withHourOfDay(i).toDate();
            List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._LUX); // 设备列表

            // 计算小时照度累积量
            for (LocationVo n : locationIds) {
                String locationId = n.getLocationId();
                if (!isIgnored(locationId)) {
                    mathHourLux(locationId, beforeHour);
                }
            }
        }
        log.info("小时光照量统计结束");
    }

    /**
     * 计算小时光照量
     *
     * @param locationId
     * @param datetime
     */
    public void mathHourLux(String locationId, Date datetime) {
        // 计算小时光照总量
        Double sumLux = getSumHourLux(locationId, datetime);
        // 为null不需要计算
        if (sumLux == null) {
            return;
        }
        int season = getSeason(datetime);
        // 判断数据是否存在
        boolean isExist = dao.isExistHourLux(locationId, datetime);
        if (isExist) {
            // 修改小时光照总量
            dao.updateHourLux(locationId, datetime, season, sumLux);
            log.debug("更新小时光照量计算结果" + locationId + "\t" + DateUtils.getDate(datetime));
        } else {
            // 添加小时光照总量
            dao.addHourLux(locationId, datetime, season, sumLux);
            log.debug("添加小时光照量计算结果" + locationId + "\t" + DateUtils.getDate(datetime));
        }
    }

    /**
     * 计算小时光照总量
     *
     * @param locationId
     * @param datetime
     */
    public Double getSumHourLux(String locationId, Date datetime) {
        // 获取光照信息
        List<Lux> luxs = dao.getHourLuxs(locationId, datetime);
        if (luxs == null || luxs.isEmpty()) {
            return null;
        }
        boolean isFirst = true;
        double startValue = 0d;
        double endValue = 0d;
        double currentValue = 0d;
        Date startTime = datetime;
        Date currentTime = new Date();
        Date endTime = new DateTime(datetime).plusHours(1).toDate();
        double sumLux = 0d;
        // 计算小时光照
        for (Lux lux : luxs) {
            currentValue = lux.getLux();
            currentTime = lux.getDatetime();
            if (isFirst) {
                startValue = currentValue;
                isFirst = false;
            }
            sumLux += convertHourLux(startTime, currentTime, startValue,
                    currentValue);
        }
        endValue = currentValue;
        sumLux += convertHourLux(startTime, endTime, startValue, endValue);
        return roundByPrecision(Defines._LUX, sumLux / 3600);
    }

    /**
     * 转换光照量为小时光照量(利用两个时间之间的光照进行转换)
     *
     * @param startTime    开始时间
     * @param currentTime  当前时间
     * @param startValue   开始值
     * @param currentValue 结束
     */
    public double convertHourLux(Date startTime, Date currentTime,
                                 double startValue, double currentValue) {
        long diffSeconds = (currentTime.getTime() / 1000)
                - (startTime.getTime() / 1000);
        return (startValue + currentValue) * diffSeconds / 2;
    }

    /**
     * 计算小时降雨量
     */
    public void mathHourRb() {
        log.info("小时降雨量统计开始");
        // 获取当前时间的前一小时
        Date beforeHour = new DateTime().minusHours(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._Rainfall); // 设备列表
        // 计算小时降水量
        for (LocationVo n : locationIds) {
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathHourRb(locationId, beforeHour);
            }
        }
        log.info("小时降雨量统计结束");
    }

    /**
     * 计算水流量，水速，水位监测指标的小时平均值
     */
    public void mathHourData(int sensorId, String str) {
        log.info("小时" + str + "统计开始");
        Date beforeHour = new DateTime().minusHours(1).toDate();
        List<LocationVo> locations = dao.getDeviceListBySensor(sensorId); // 设备列表
        //计算小时数据
        for (LocationVo location : locations) {
            String locationId = location.getLocationId();
            if (!isIgnored(locationId)) {
                Date startDate = DateUtils.startOfHour(beforeHour);
                Date endDate = DateUtils.endOfHour(beforeHour);
                //获取当前时间的前一小时的数据
                Double data = dao.getAvgByTimeAndSensorId(locationId, startDate, endDate, sensorId);
                //前一小时的值为空，就获取前一小时的前一小时的数据
                if (data == null) {
                    Date before = DateUtils.addHour(beforeHour, -1);
                    Date startDateBefore = DateUtils.startOfHour(before);
                    Date endDateBefore = DateUtils.endOfHour(before);
                    data = dao.getAvgByTimeAndSensorId(locationId, startDateBefore, endDateBefore, sensorId);
                }
                if (data == null) {
                    //没有数据，不做操作
                    log.debug("当前时间前两小时都没有" + str + "数据," + locationId + "\t" + DateUtils.getDate(new Date()));
                } else {
                    Date newDate = new Date();

                    DecimalFormat dg = new DecimalFormat("0.00");
                    data = Double.parseDouble(dg.format(data));
                    dao.addHourAvgValue(locationId, newDate, data, sensorId);
                    log.debug("添加小时" + str + "," + locationId + "\t" + DateUtils.getDate(newDate));
                }
            }
        }
        log.info("小时" + str + "统计结束");
    }

    /**
     * 计算小时有机污染物
     */
    public void mathHourOp() {
        log.info("小时有机污染物统计开始");
        // 获取当前时间的前一小时
        Date beforeHour = new DateTime().minusHours(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._ORGANIC_POL); // 位置点列表
        // 计算小时有机污染物
        for (LocationVo n : locationIds) {
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathHourOp(locationId, beforeHour);
            }
        }
        log.info("小时有机污染物统计结束");
    }


    /**
     * 计算小时无机污染物
     */
    public void mathHourIp() {
        log.info("小时无机污染物统计开始");
        // 获取当前时间的前一小时
        Date beforeHour = new DateTime().minusHours(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._INORGANIC_POL); // 位置点列表
        // 计算小时无机污染物
        for (LocationVo n : locationIds) {
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathHourIp(locationId, beforeHour);
            }
        }
        log.info("小时无机污染物统计结束");
    }


    /**
     * 计算小时含硫污染物
     */
    public void mathHourSp() {
        log.info("小时含硫污染物统计开始");
        // 获取当前时间的前一小时
        Date beforeHour = new DateTime().minusHours(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._SULFUROUS_POL); // 位置点列表
        // 计算小时含硫污染物
        for (LocationVo n : locationIds) {
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathHourSp(locationId, beforeHour);
            }
        }
        log.info("小时含硫污染物统计结束");
    }

    /**
     * 计算小时降雨量
     *
     * @param date 开始时间
     */
    public void mathHourRb(Date date) {
        log.info("小时降雨量统计开始");
        for (int i = 0; i < 24; i++) {
            // 获取当前时间的前一小时
            Date beforeHour = new DateTime(date).withHourOfDay(i).toDate();
            List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._Rainfall); // 设备列表
            // 计算小时降水量
            for (LocationVo n : locationIds) {
                String locationId = n.getLocationId();
                if (!isIgnored(locationId)) {
                    mathHourRb(locationId, beforeHour);
                }
            }
        }
        log.info("小时降雨量统计结束");
    }

    /**
     * 计算小时降雨量
     *
     * @param locationId 监测点编号
     * @param datetime   计算的时间
     */
    public void mathHourRb(String locationId, Date datetime) {
        // 获取小时降雨总量
        Double hourRb = dao.getHourRbSum(locationId, datetime);
        // 值为空不需要进行计算
        if (hourRb == null) {
            return;
        }
        // 根据传感精度四舍五入
        hourRb = roundByPrecision(Defines._Rainfall, hourRb);
        // 判断降雨量是否存在,
        // 降雨量是用当前一包的液位数据减上一包的液位数据
        boolean isExist = dao.isExistHourRB(locationId, datetime);
        if (isExist) {
            // 修改降雨量
            dao.updateHourRb(locationId, datetime, hourRb);
            log.debug("更新小时降雨总量" + locationId + "\t" + DateUtils.getDate(datetime));
        } else {
            // 添加降雨量
            dao.addHourRb(locationId, datetime, hourRb);
            log.debug("添加小时降雨总量" + locationId + "\t" + DateUtils.getDate(datetime));
        }
    }

    /**
     * 计算小时有机污染物
     *
     * @param locationId 监测点编号
     * @param datetime   计算的时间
     */
    public void mathHourOp(String locationId, Date datetime) {
        // 获取小时有机污染物总量
        Double hourOp = dao.getHourOpSum(locationId, datetime);
        // 值为空不需要进行计算
        if (hourOp == null) {
            return;
        }
        // 根据传感精度四舍五入
        hourOp = roundByPrecision(Defines._ORGANIC_POL, hourOp);
        // 判断有机污染物是否存在
        boolean isExist = dao.isExistHourOp(locationId, datetime);
        if (isExist) {
            // 修改有机污染物
            dao.updateHourOp(locationId, datetime, hourOp);
            log.debug("更新有机污染物总量" + locationId + "\t" + DateUtils.getDate(datetime));
        } else {
            // 添加有机污染物
            dao.addHourOp(locationId, datetime, hourOp);
            log.debug("添加小时有机污染物总量" + locationId + "\t" + DateUtils.getDate(datetime));
        }
    }

    /**
     * 计算小时无机污染物
     *
     * @param locationId 监测点编号
     * @param datetime   计算的时间
     */
    public void mathHourIp(String locationId, Date datetime) {
        // 获取小时无机污染物总量
        Double hourIp = dao.getHourIpSum(locationId, datetime);
        // 值为空不需要进行计算
        if (hourIp == null) {
            return;
        }
        // 根据传感精度四舍五入
        hourIp = roundByPrecision(Defines._INORGANIC_POL, hourIp);
        // 判断无机污染物是否存在
        boolean isExist = dao.isExistHourIp(locationId, datetime);
        if (isExist) {
            // 修改无机污染物
            dao.updateHourIp(locationId, datetime, hourIp);
            log.debug("更新有机污染物总量" + locationId + "\t" + DateUtils.getDate(datetime));
        } else {
            // 添加无机污染物
            dao.addHourIp(locationId, datetime, hourIp);
            log.debug("添加小时有机污染物总量" + locationId + "\t" + DateUtils.getDate(datetime));
        }
    }

    /**
     * 计算小时含硫污染物
     *
     * @param locationId 监测点编号
     * @param datetime   计算的时间
     */
    public void mathHourSp(String locationId, Date datetime) {
        // 获取小时含硫污染物总量
        Double hourSp = dao.getHourSpSum(locationId, datetime);
        // 值为空不需要进行计算
        if (hourSp == null) {
            return;
        }
        // 根据传感精度四舍五入
        hourSp = roundByPrecision(Defines._SULFUROUS_POL, hourSp);
        // 判断含硫污染物是否存在
        boolean isExist = dao.isExistHourSp(locationId, datetime);
        if (isExist) {
            // 修改含硫污染物
            dao.updateHourSp(locationId, datetime, hourSp);
            log.debug("更新有机污染物总量" + locationId + "\t" + DateUtils.getDate(datetime));
        } else {
            // 添加含硫污染物
            dao.addHourSp(locationId, datetime, hourSp);
            log.debug("添加小时有机污染物总量" + locationId + "\t" + DateUtils.getDate(datetime));
        }
    }

    /**
     * 计算小时蒸发量
     */
    public void mathHourEvap() {
        log.info("小时蒸发量统计开始");
        // 获取当前时间的前一小时
        Date beforeHour = new DateTime().minusHours(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._EVAP); // 设备列表
        // 计算小时降水量
        for (LocationVo n : locationIds) {
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathHourEvap(locationId, beforeHour);
            }
        }
        log.info("小时蒸发量统计结束");
    }

    /**
     * 计算小时蒸发量
     *
     * @param date 开始时间
     */
    public void mathHourEvap(Date date) {
        log.info("小时蒸发量统计开始");
        // 获取当前时间的前一小时
        Date beforeHour = new DateTime(date).minusHours(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._EVAP); // 设备列表
        // 计算小时降水量
        for (LocationVo n : locationIds) {
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathHourEvap(locationId, beforeHour);
            }
        }
        log.info("小时蒸发量统计结束");
    }

    /**
     * 计算小时蒸发量
     *
     * @param locationId 监测点编号
     * @param datetime   计算的时间
     */

    public void mathHourEvap(String locationId, Date datetime) {
        // 获取小时降雨总量
        Double hourEvap = dao.getHourEvapSum(locationId, datetime);
        // 值为空不需要进行计算
        if (hourEvap == null) {
            return;
        }
        // 根据传感精度四舍五入
        hourEvap = roundByPrecision(Defines._EVAP, hourEvap);
        // 判断降雨量是否存在
        boolean isExist = dao.isExistHourEvap(locationId, datetime);
        if (isExist) {
            // 修改蒸发量
            dao.updateHourEvap(locationId, datetime, hourEvap);
            log.debug("更新小时蒸发总量" + locationId + "\t" + DateUtils.getDate(datetime));
        } else {
            // 添加蒸发量
            dao.addHourEvap(locationId, datetime, hourEvap);
            log.debug("添加小时蒸发总量" + locationId + "\t" + DateUtils.getDate(datetime));
        }
    }


    /**
     * 计算天降水量
     * <p>
     * 计算日降水量 参考《中华人民共和国水利行业标准SL21-2006 降水量观测规范》 降水量的观测时间以北京时间为准。
     * 记起止时间者,观测时间记至分,不及起止时间,记为小时。 每日降水以北京时间8时为日分界,即从昨日8时至今日8时的降水为昨日降水量。
     */
    public void mathDayRb() {
        log.info("天降雨量统计开始");
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._Rainfall); // 设备列表
        // 获取当前时间的前一天
        Date yesterday = new DateTime().minusDays(1).toDate();
        for (LocationVo n : locationIds) {
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathDayRb(locationId, yesterday);
            }
        }
        log.info("天降雨量统计结束");
    }


    /**
     * 天降雨量(批量统计过去未统计成功的数据)
     */
    public void mathDayRb(Date date) {
        log.info("天降雨量统计开始");
        List<LocationVo> deviceList = dao.getDeviceListBySensor(Defines._Rainfall);
        for (LocationVo n : deviceList) {
            // 均峰值计算
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathDayRb(locationId, date);
            }
        }
        log.info("天降雨量统计结束");
    }

    /**
     * 计算天降水量
     *
     * @param locationId 监测点编号
     * @param date       时间
     */
    public void mathDayRb(String locationId, Date date) {
        // 开始时间
        Date startTime = new DateTime(DateUtils.convertDate(date, DATE)).plusHours(8).toDate();
        // 结束时间
        Date endTime = new DateTime(startTime).plusDays(1).minusSeconds(1).toDate();
        Double dayRbSum = dao.getDayRbSum(locationId, startTime, endTime);
        //为null表示没有数据，不需要计算
        if (dayRbSum == null) {
            return;
        }
        // 根据传感精度四舍五入
        dayRbSum = roundByPrecision(Defines._Rainfall, dayRbSum);
        // 判断天降雨量是否存在
        boolean isExist = dao.isExistDayRB(locationId, date);
        if (isExist) {
            // 修改降雨量
            dao.updateDayRb(locationId, date, dayRbSum);
            log.debug("更新天降雨总量" + locationId + "\t" + DateUtils.getDate(date));
        } else {
            // 添加降雨量
            dao.addDayRb(locationId, date, dayRbSum);
            log.debug("添加天降雨总量" + locationId + "\t" + DateUtils.getDate(date));
        }
    }

    /**
     * 均峰值计算(批量统计过去未统计成功的数据)
     */
    public void mathAvgPeak(Date date) {
        log.info("均峰值统计开始");
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._NoneSensor);
        for (LocationVo n : locationIds) {
            // 均峰值计算
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathAvgPeak(locationId, date);
            }
        }
        log.info("均峰值统计结束");
    }

    /**
     * 位置点均峰值计算
     */
    public void mathAvgPeak() {
        log.info("均峰值统计开始");
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._NoneSensor);
        // 获取当前时间的前一天
        Date yesterday = new DateTime().minusDays(1).toDate();
        for (LocationVo n : locationIds) {
            // 均峰值计算
            String locationId = n.getLocationId();
            if (!isIgnored(locationId)) {
                mathAvgPeak(locationId, yesterday);
            }
        }
        log.info("均峰值统计结束");
    }

    /**
     * 计算各区域各监测指标均值
     */
    public void mathZoneAvgPeak(Date date) {
        log.info(date + "区域均峰值统计开始");
        try {

            dao.deleteZoneAvgPeak(date);
            dao.addZoneAvgPeak(date);
        } catch (Exception e) {
            log.error("添加区域均峰值信息(addZoneAvgPeak)出错", e);
        }
        log.info(date + "区域均峰值统计结束");
    }

    public void mathKDJ() {
        log.info("kdj统计开始");
        //查询所有位置点
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._NoneSensor);

        for (LocationVo locationVo : locationIds) {
            String locationId = locationVo.getLocationId();
            if (!Strings.isNullOrEmpty(locationId)) {
                //计算kdj
                mathKDJ(locationId, new Date());
            }
        }
        log.info("kdj统计结束");
    }

    public void deleteLogAnalysisFile() {
        log.info("删除日志分析包开始");
        String sourcePath = System.getProperty("catalina.home") + File.separator + "logs" + File.separator + "blueplanet-daemon" + File.separator + "logminer" + File.separator;
        File f = new File(sourcePath);
        new DeleteDirectory().deleteDir(f);
        log.info("删除日志分析包结束");
    }

    public void handMathKDJ(Date startDate, Date endDate) {
        LocalDate start = new LocalDate(startDate);
        LocalDate end = new LocalDate(endDate);
        int days = Days.daysBetween(start, end).getDays();
        log.info("kdj手动统计开始");
        //查询所有位置点
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._NoneSensor);
        for (int i = 0; i <= days; i++) {
            for (LocationVo locationVo : locationIds) {
                String locationId = locationVo.getLocationId();
                if (!Strings.isNullOrEmpty(locationId)) {
                    //计算kdj
                    mathKDJ(locationId, startDate);
                }
            }
            startDate = new DateTime(startDate).plusDays(1).toDate();
        }
        log.info("kdj手动统计结束");
    }

    /**
     * 计算kdj日均值
     *
     * @param locationId 位置点id
     */
    public void mathKDJ(String locationId, Date date) {

        Date yesterday = new DateTime(date).minusDays(1).toDate();
        List<Stock> stocks = dao.findStocks(locationId, DateUtils.startOfDay(yesterday), DateUtils.endOfDay(yesterday));
        Date beforeYesterday = new DateTime(yesterday).minusDays(1).toDate();

        for (Stock stock : stocks) {
            //计算rsv
            Stock stockKJ = dao.findStockKD(locationId, stock.getSensorId(), DateUtils.startOfDay(beforeYesterday));
            double rsv;
            if (stock.getMaxValue() - stock.getMinValue() != 0) {
                rsv = (stock.getEndValue() - stock.getMinValue()) / (stock.getMaxValue() - stock.getMinValue()) * 100;
            } else {
                rsv = (stock.getEndValue() - stock.getMinValue()) * 100;
            }

            double k;
            double d;
            double j;
            //当日的kd (如果无前一日的kd，则用50代替)
            if (stockKJ != null) {
                k = 2d / 3d * stockKJ.getK() + 1d / 3d * rsv;
                d = 2d / 3d * stockKJ.getD() + 1d / 3d * k;
            } else {
                k = 2d / 3d * 50 + 1d / 3d * rsv;
                d = 2d / 3d * 50 + 1d / 3d * k;
            }

            //当日的j
            j = 3 * k - 2 * d;

            //保留两位小数
            DecimalFormat df = new DecimalFormat("#.00");
            df.format(stock.getD());
            try {
                stock.setK(Double.parseDouble(df.format(k)));
                stock.setD(Double.parseDouble(df.format(d)));
                stock.setJ(Double.parseDouble(df.format(j)));
            } catch (Exception e) {
                log.info("数字转换异常", e);
            }

            // 参数设置
            stock.setId(StringUtil.uuid());
            stock.setLocationId(locationId);
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime yesterdayTime = DateTime.parse(DateUtils.getDate(yesterday, "yyyy-MM-dd"), format);
            Date tempYesterday = yesterdayTime.toDate();
            stock.setStamp(tempYesterday);

            //日均值计算
            dayAvgValueData(stock);

            boolean isExistAvg = dao.isExistDateKDJ(locationId, tempYesterday, stock.getSensorId());

            if (isExistAvg) {
                dao.updateKDJ(stock);
            } else {
                dao.insertStock(stock);
            }
        }
    }

    /**
     * 计算日均值并组织数据
     *
     * @param stock stock对象
     */
    private void dayAvgValueData(Stock stock) {

        //计算5日均线
        stock.setAvgValue5(dao.findStockEndValue(stock.getLocationId(), stock.getSensorId(), 5, stock.getStamp()));

        //计算10日均线
        stock.setAvgValue10(dao.findStockEndValue(stock.getLocationId(), stock.getSensorId(), 10, stock.getStamp()));

        //计算30日均线
        stock.setAvgValue30(dao.findStockEndValue(stock.getLocationId(), stock.getSensorId(), 30, stock.getStamp()));
    }

    /**
     * 均峰值计算
     *
     * @param locationId 监测点编号
     * @param date       时间
     */
    public void mathAvgPeak(String locationId, Date date) {
        List<AvgPeak> avgPeakInfo = dao.getAvgPeaks(locationId, date);
        for (AvgPeak avg : avgPeakInfo) {
            // 查询最大值和最小值的时间
            avg.setMaxTime(dao.getCreateTimeByData(locationId, avg.getMaxValue(), avg.getSensorPhysicalid(), date));
            avg.setMinTime(dao.getCreateTimeByData(locationId, avg.getMinValue(), avg.getSensorPhysicalid(), date));
            avg.setAvgTime(date);
            Sensor sensor = appCache.loadSensor(avg.getSensorPhysicalid());
            double maxValue = avg.getMaxValue();
            double minValue = avg.getMinValue();
            double avgValue = avg.getAvgValue();
            double waveValue = maxValue - minValue;
            avg.setMaxValue(Maths.roundToDouble(maxValue, sensor.getPrecision()));
            avg.setMinValue(Maths.roundToDouble(minValue, sensor.getPrecision()));
            avg.setAvgValue(Maths.roundToDouble(avgValue, sensor.getPrecision()));
            avg.setWaveValue(Maths.roundToDouble(waveValue, sensor.getPrecision()));
            // 判断均峰值是否存在
            boolean isExistAvg = dao.isExistDatePeakAvg(locationId, date,
                    avg.getSensorPhysicalid());
            if (isExistAvg) {
                dao.updateAvgPeak(locationId, avg);
                log.debug("更新均峰值计算结果" + locationId + "\t" + DateUtils.getDate(date));
            } else {
                dao.addAvgPeak(locationId, avg);
                log.debug("添加均峰值计算结果" + locationId + "\t" + DateUtils.getDate(date));
            }

        }
    }

    /**
     * 计算达标率
     */
    public void mathComplianceRate() {
        log.info("监测指标达标率统计开始");
        //日期
        Date date = new DateTime().minusDays(1).toDate();
        Collection<DeviceBean> devices = appCache.getDeviceCache().asMap().values();

        for (DeviceBean device : devices) {
            mathComplianceRate(device, date);
        }
        log.info("监测指标达标率统计结束");
    }

    public void mathComplianceRate(Date date) {
        log.info("监测指标达标率统计开始");
        Collection<DeviceBean> devices = appCache.getDeviceCache().asMap().values();
        for (DeviceBean device : devices) {
            mathComplianceRate(device, date);
        }
        log.info("监测指标达标率统计结束");
    }

    public void mathComplianceRate(DeviceBean device, Date date) {
        // 查询位置点报警条件
        List<Threshold> thresholds = thresholdService.findThresholds(device.deviceid);
        if (thresholds == null) return;

        // 根据报警条件计算达标率
        for (Threshold threshold : thresholds) {
            if (threshold.getConditionType() == 0) continue;
            if (device.getNodeSensors().containsKey(threshold.getSensorId())) {
                dao.updateComplianceRate(threshold, date, device.locationId);
            }
        }
    }

    /**
     * 计算玫瑰图
     */
    public void mathWindRose() {
        log.info("玫瑰图统计开始");
        // 获取当前时间的前一天
        Date yesterday = new DateTime().minusDays(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._Wind_Velocity);
        for (LocationVo no : locationIds) {
            String locationId = no.getLocationId();
            if (!isIgnored(locationId)) {
                mathWindRose(locationId, yesterday);
            }
        }
        log.info("玫瑰图统计结束");
    }

    /**
     * 计算玫瑰图
     */
    public void mathWindRose(Date date) {
        log.info("玫瑰图统计开始");
        // 获取当前时间的前一天
        Date yesterday = new DateTime(date).minusDays(1).toDate();
        List<LocationVo> locationIds = dao.getDeviceListBySensor(Defines._Wind_Velocity);
        for (LocationVo no : locationIds) {
            String locationId = no.getLocationId();
            if (!isIgnored(locationId)) {
                mathWindRose(locationId, yesterday);
            }
        }
        log.info("玫瑰图统计结束");
    }

    /**
     * 计算玫瑰图
     *
     * @param locationId 监测点编号
     * @param date       时间
     */
    public void mathWindRose(String locationId, Date date) {
        // 获取玫瑰图数据（静风除外 ,风速小于等于0.2的为静风）
        List<WindRose> windRoseList = dao.getWindRoses(locationId, date);
        //数据为空不计算
        if (windRoseList.isEmpty()) {
            return;
        }
        // 获取所有玫瑰的数据条数（包含静风值）
        int windRoseCount = dao.getWindRoseCount(locationId, date, false);
        WindMark windMark = new WindMark();
        for (WindRose windRose : windRoseList) {
            mathWindMarkValue(windRose, windMark, windRoseCount);
        }
        // 获取静风值
        int windCalm = dao.getWindRoseCount(locationId, date, true);
        // 算出静风率
        float windCalmRate = getDirectionRate(windCalm, windRoseCount);
        windMark.setWindCalmRate(windCalmRate);
        windMark.setWindRoseCount(windRoseCount);
        // 计算时间所属季度
        int season = getSeason(date);
        // 判断玫瑰图是否存在
        boolean isExist = dao.isExistDateRose(locationId, date);
        if (isExist) {
            // 修改玫瑰图信息
            dao.updateWindRose(windMark, locationId, date, season);
            log.debug("更新玫瑰图计算结果" + locationId + "\t" + DateUtils.getDate(date));
        } else {
            // 添加玫瑰图信息
            dao.addWindRose(windMark, locationId, date, season);
            log.debug("添加玫瑰图计算结果" + locationId + "\t" + DateUtils.getDate(date));
        }

    }

    public void mathPmSensor() {
        log.info("pm传感计算开始");
        //获取上一个小时数据
        Date startDate = new DateTime().minusHours(SysConfig.countPmSensorTime).toDate();
        Date endDate = new Date();
        List<NodeData> pmSensorData = dao.getPmSensorData(startDate, endDate);
        for (NodeData nodeData : pmSensorData) {
            DeviceBean device = new DeviceBean();
            device.deviceid = nodeData.getNodeId();
            device.timeStamp = nodeData.getCreateTime();
            SensorPhysicalBean sensor = new SensorPhysicalBean();
            sensor.setSensorPhysical_id(nodeData.getSensorPhysicalId());
            sensor.setSensor_Value(nodeData.getSensorPhysicalValue());
            sensor.setSensor_State(nodeData.getState());
            analysisDao.updateSensorMemory(device, sensor);
            analysisDao.addNodeData(nodeData.getNodeId(), nodeData.getCreateTime(), nodeData.getVoltage(), sensor, nodeData.getAnomaly());
            //算完以后删除数据
            dao.deleteSensors(device.deviceid, sensor.getSensorPhysical_id());
        }
        log.info("pm传感计算结束");
    }

    /**
     * 计算风向值和风速值
     *
     * @param windRose      玫瑰图信息
     * @param wm            存储风向和风速信息
     * @param windRoseCount 玫瑰图总条数
     */
    private void mathWindMarkValue(WindRose windRose, WindMark wm,
                                   int windRoseCount) {
        // 获取风向标
        float windMark = windRose.getWindmark();
        // 计算风向率
        float directionRate = getDirectionRate(
                windRose.getWindDirectionCount(), windRoseCount);
        // 获取风向值
        float windSpeed = windRose.getWindSpeedAvg();
        if (windMark == 0) {
            wm.setDirectionN(directionRate);
            wm.setSpeedN(windSpeed);
        } else if (windMark == 22.5) {
            wm.setDirectionNNE(directionRate);
            wm.setSpeedNNE(windSpeed);
        } else if (windMark == 45) {
            wm.setDirectionNE(directionRate);
            wm.setSpeedNE(windSpeed);
        } else if (windMark == 67.5) {
            wm.setDirectionENE(directionRate);
            wm.setSpeedENE(windSpeed);
        } else if (windMark == 90) {
            wm.setDirectionE(directionRate);
            wm.setSpeedE(windSpeed);
        } else if (windMark == 112.5) {
            wm.setDirectionESE(directionRate);
            wm.setSpeedESE(windSpeed);
        } else if (windMark == 135) {
            wm.setDirectionSE(directionRate);
            wm.setSpeedSE(windSpeed);
        } else if (windMark == 157.5) {
            wm.setDirectionSSE(directionRate);
            wm.setSpeedSSE(windSpeed);
        } else if (windMark == 180) {
            wm.setDirectionS(directionRate);
            wm.setSpeedS(windSpeed);
        } else if (windMark == 202.5) {
            wm.setDirectionSSW(directionRate);
            wm.setSpeedSSW(windSpeed);
        } else if (windMark == 225) {
            wm.setDirectionSW(directionRate);
            wm.setSpeedSW(windSpeed);
        } else if (windMark == 247.5) {
            wm.setDirectionWSW(directionRate);
            wm.setSpeedWSW(windSpeed);
        } else if (windMark == 270) {
            wm.setDirectionW(directionRate);
            wm.setSpeedW(windSpeed);
        } else if (windMark == 292.5) {
            wm.setDirectionWNW(directionRate);
            wm.setSpeedWNW(windSpeed);
        } else if (windMark == 315) {
            wm.setDirectionNW(directionRate);
            wm.setSpeedNW(windSpeed);
        } else if (windMark == 337.5) {
            wm.setDirectionNNW(directionRate);
            wm.setSpeedNNW(windSpeed);
        }
    }

    /**
     * 获取传感精度四舍五入
     *
     * @return 计算结果
     */
    private double roundByPrecision(int sensorId, double value) {
        Sensor sensor = appCache.loadSensor(sensorId);
        int precision = 0;
        if (sensor != null && sensor.getPrecision() >= 0) {
            precision = sensor.getPrecision();
        }
        return Maths.roundToDouble(value, precision);
    }

    /**
     * 统计风向率
     *
     * @param directionCount 风向总数据量
     * @param sum            玫瑰图总数据量
     */
    private float getDirectionRate(float directionCount, float sum) {
        if (sum == 0) {
            return 0;
        }
        return Maths.round(directionCount / sum * 100, 1).floatValue();
    }

    /**
     * 根据时间获取季度
     *
     * @param date 时间
     * @return 季度
     */
    private int getSeason(Date date) {
        int season = 1;
        int month = Integer.valueOf(DateUtils.getDate(date, MONTH));
        if (month >= 1 && month <= 3) {
            season = 1;
        } else if (month >= 4 && month <= 6) {
            season = 2;
        } else if (month >= 7 && month <= 9) {
            season = 3;
        } else {
            season = 4;
        }
        return season;
    }

    /**
     * 是否忽略统计
     *
     * @param deviceId 设备编号
     * @return true 忽略  false 不忽略
     */
    private boolean isIgnored(String deviceId) {
        for (String siteId : SysConfig.getInstance().getSitesWithoutTimeOutCheck()) {
            if (deviceId.startsWith(siteId)) {
                return true;
            }
        }
        return false;
    }
}
