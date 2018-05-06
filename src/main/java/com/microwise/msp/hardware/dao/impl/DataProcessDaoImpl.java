package com.microwise.msp.hardware.dao.impl;

import com.google.common.collect.Maps;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.dao.DataProcessDao;
import com.microwise.msp.hardware.vo.LocationVo;
import com.microwise.msp.util.DateTimeUtil;
import com.microwise.msp.util.DateUtils;
import com.microwise.msp.util.StringUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 数据持久化数据处理实现类
 *
 * @author xuexu
 */
@Component
@Scope("prototype")
public class DataProcessDaoImpl extends BaseDaoImpl implements DataProcessDao {

    private static Logger log = LoggerFactory
            .getLogger(DataProcessDaoImpl.class);

    private static final String DATE = "yyyy-MM-dd";

    private static final String HOUR = "yyyy-MM-dd HH:00:00";


    @Override
    public List<AvgPeak> getAvgPeaks(String locationId, Date date) {
        //转换时间类型
        List<AvgPeak> avgPeakInfo = new ArrayList<AvgPeak>();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        Date startTime = DateUtils.startOfDay(date);
        Date endTime = DateUtils.endOfDay(date);
        paraMap.put("startTime", startTime);
        paraMap.put("endTime", endTime);
        try {
            avgPeakInfo = getSqlSession().selectList(
                    "DataProcess.getAvgPeaks", paraMap);
        } catch (DataAccessException e) {
            log.error("获取均峰值数据(getAvgPeaks)出错", e);
        }
        return avgPeakInfo;
    }


    @Override
    public List<WindRose> getWindRoses(String locationId, Date date) {
        //转换时间类型
        Date startDate = DateUtils.startOfDay(date);
        Date endDate = DateUtils.endOfDay(date);
        List<WindRose> windRoseList = new ArrayList<WindRose>();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        try {
            windRoseList = getSqlSession().selectList(
                    "DataProcess.getWindRoses", paraMap);
        } catch (DataAccessException e) {
            log.error("获取玫瑰图数据(getWindRoses)出错", e);
        }
        return windRoseList;
    }


    public List<NodeData> getPmSensorData(Date startDate, Date endDate) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        List<NodeData> pmSensorData = new ArrayList<NodeData>();
        try {
            pmSensorData = getSqlSession().selectList("DataProcess.getPmSensorData", paramMap);
        } catch (DataAccessException e) {
            log.error(" 获取pm 数据(getPmSensorData)出错", e);
        }
        return pmSensorData;
    }

    public void deleteSensors(String deviceId, int sensorId) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceId", deviceId);
        paramMap.put("sensorId", sensorId);
        try {
            getSqlSession().delete("DataProcess.deleteSensors", paramMap);
        } catch (DataAccessException e) {
            log.error(" 删除传感数据 (deleteSensors)出错", e);
        }
    }

    @Override
    public void deleteZoneAvgPeak(Date date) {
        getSqlSession().delete("DataProcess.deleteZoneAvgPeak", date);
    }


    @Override
    public Integer getWindRoseCount(String locationId, Date date, boolean isCalmWind) {
        Integer count = null;
        Date startDate = DateUtils.startOfDay(date);
        Date endDate = DateUtils.endOfDay(date);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        paraMap.put("isCalmWind", isCalmWind);
        try {
            count = getSqlSession().selectOne(
                    "DataProcess.getWindRoseCount", paraMap);
        } catch (DataAccessException e) {
            log.error("获取玫瑰图的条数(getWindRoseCount)出错", e);
        }
        return count == null ? 0 : count;
    }


    @Override
    public Double getDayRbSum(String locationId, Date startTime, Date endTime) {
        Double result = 0d;
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startTime", startTime);
        paraMap.put("endTime", endTime);
        try {
            result = getSqlSession().selectOne(
                    "DataProcess.getDayRbSum", paraMap);
        } catch (DataAccessException e) {
            log.error("获取天降雨量总数(getDayRbSum)出错", e);
        }
        return result;
    }


    @Override
    public Double getDayEvapSum(String locationId, Date startTime, Date endTime) {
        Double result = 0d;
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startTime", startTime);
        paraMap.put("endTime", endTime);
        try {
            result = getSqlSession().selectOne(
                    "DataProcess.getDayEvapSum", paraMap);
        } catch (DataAccessException e) {
            log.error("获取天蒸发量总数(getDayEvapSum)出错", e);
        }
        return result;
    }


    @Override
    public Double getHourRbSum(String locationId, Date datetime) {
        Double result = 0d;
        Date startDate = DateUtils.startOfHour(datetime);
        Date endDate = DateUtils.endOfHour(datetime);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        try {
            result = getSqlSession().selectOne(
                    "DataProcess.getHourRbSum", paraMap);
        } catch (DataAccessException e) {
            log.error("获取小时降雨量总数(getHourRbSum)出错", e);
        }
        return result;
    }

    @Override
    public Double getAvgByTimeAndSensorId(String locationId, Date startDate, Date endDate, int sensorId) {
        Double result = 0d;
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        paraMap.put("sensorId", sensorId);
        try {
            result = getSqlSession().selectOne("DataProcess.getAvgByTimeAndSensorId", paraMap);
        } catch (Exception e) {
            log.error("获取水流量失败", e);
        }
        return result;
    }

    @Override
    public Double getHourOpSum(String locationId, Date datetime) {
        Double result = 0d;
        Date startDate = DateUtils.startOfHour(datetime);
        Date endDate = DateUtils.endOfHour(datetime);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        try {
            result = getSqlSession().selectOne(
                    "DataProcess.getHourOpSum", paraMap);
        } catch (DataAccessException e) {
            log.error("获取小时有机污染物总数(getHourOpSum)出错", e);
        }
        return result;
    }

    @Override
    public Double getHourIpSum(String locationId, Date datetime) {
        Double result = 0d;
        Date startDate = DateUtils.startOfHour(datetime);
        Date endDate = DateUtils.endOfHour(datetime);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        try {
            result = getSqlSession().selectOne(
                    "DataProcess.getHourIpSum", paraMap);
        } catch (DataAccessException e) {
            log.error("获取小时无机污染物总数(getHourIpSum)出错", e);
        }
        return result;
    }

    @Override
    public Double getHourSpSum(String locationId, Date datetime) {
        Double result = 0d;
        Date startDate = DateUtils.startOfHour(datetime);
        Date endDate = DateUtils.endOfHour(datetime);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        try {
            result = getSqlSession().selectOne(
                    "DataProcess.getHourSpSum", paraMap);
        } catch (DataAccessException e) {
            log.error("获取小时含硫污染物总数(getHourSpSum)出错", e);
        }
        return result;
    }


    @Override
    public Double getHourEvapSum(String locationId, Date datetime) {
        Double result = 0d;
        Date startDate = DateUtils.startOfHour(datetime);
        Date endDate = DateUtils.endOfHour(datetime);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        try {
            result = getSqlSession().selectOne(
                    "DataProcess.getHourEvapSum", paraMap);
        } catch (DataAccessException e) {
            log.error("获取小时蒸发量总数(getHourEvapSum)出错", e);
        }
        return result;
    }


    @Override
    public List<Lux> getHourLuxs(String locationId, Date datetime) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("startTime", DateTimeUtil.startOfHour(datetime));
        paraMap.put("endTime", DateTimeUtil.endOfHour(datetime));
        List<Lux> luxs = new ArrayList<Lux>();
        try {
            luxs = getSqlSession().selectList("DataProcess.getHourLuxs", paraMap);
        } catch (DataAccessException e) {
            log.error("获取光照量信息(getHourLuxs)出错", e);
        }
        return luxs;
    }


    @Override
    public Date getCreateTimeByData(String locationId, double value, int sensorPhysicalId,
                                    Date date) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", date);
        paramMap.put("sensorId", sensorPhysicalId);
        paramMap.put("value", StringUtil.subZeroAndDot(String.valueOf(value)));
        Date result = new Date();
        try {
            result = getSqlSession().selectOne("DataProcess.getCreateTimeByData",
                    paramMap);
        } catch (DataAccessException e) {
            log.error("根据数据查询数据的时间(getCreateTimeByData)出错", e);
        }
        return result;
    }

    @Override
    public boolean isExistDatePeakAvg(String locationId, Date date,
                                      int sensorPhysicalId) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("date", DateUtils.getDate(date));
        paraMap.put("sensorPhysicalId", sensorPhysicalId);
        boolean bool = false;
        try {
            bool = (Boolean) getSqlSession().selectOne(
                    "DataProcess.isExistDatePeakAvg", paraMap);
        } catch (DataAccessException e) {
            log.error("是否存在均峰值数据(isExistDatePeakAvg)出错", e);
        }
        return bool;
    }

    @Override
    public boolean isExistDateKDJ(String locationId, Date date,
                                  int sensorPhysicalId) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("date", DateUtils.getDate(date));
        paraMap.put("sensorId", sensorPhysicalId);
        boolean bool = false;
        try {
            Integer count = getSqlSession().selectOne(
                    "DataProcess.isExistDataKDJ", paraMap);
            if (count >= 1) {
                bool = true;
            }
        } catch (DataAccessException e) {
            log.error("是否存在kdj数据(isExistDateKDJ)出错", e);
        }
        return bool;
    }

    @Override
    public boolean isExistDateRose(String locationId, Date date) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("date", date);
        boolean bool = false;
        try {
            bool = (Boolean) getSqlSession().selectOne(
                    "DataProcess.isExistDateRose", paraMap);
        } catch (DataAccessException e) {
            log.error("是否存在风向玫瑰图数据(isExistDateRose)出错", e);
        }
        return bool;
    }

    @Override
    public boolean isExistHourLux(String locationId, Date datetime) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("datetime", datetime);
        boolean bool = false;
        try {
            bool = (Boolean) getSqlSession().selectOne(
                    "DataProcess.isExistHourLux", paraMap);
        } catch (DataAccessException e) {
            log.error("是否存在小时照度累计数据(isExistHourLux)出错", e);
        }
        return bool;
    }

    @Override
    public boolean isExistHourRB(String locationId, Date datetime) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("datetime", datetime);
        boolean bool = false;
        try {
            bool = (Boolean) getSqlSession().selectOne(
                    "DataProcess.isExistHourRB", paraMap);
        } catch (DataAccessException e) {
            log.error("是否存在小时降水量数据(isExistHourRB)出错", e);
        }
        return bool;
    }

    @Override
    public boolean isExistHourOp(String locationId, Date datetime) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        //转换时间类型
        paraMap.put("datetime", DateUtils.getDate(datetime, HOUR));
        boolean bool = false;
        try {
            int count = (Integer) getSqlSession().selectOne(
                    "DataProcess.isExistHourOp", paraMap);
            bool = count > 0;
        } catch (DataAccessException e) {
            log.error("是否存在小时有机污染物数据(isExistHourOp)出错", e);
        }
        return bool;
    }

    @Override
    public boolean isExistHourIp(String locationId, Date datetime) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        //转换时间类型
        paraMap.put("datetime", DateUtils.getDate(datetime, HOUR));
        boolean bool = false;
        try {
            int count = (Integer) getSqlSession().selectOne(
                    "DataProcess.isExistHourIp", paraMap);
            bool = count > 0;
        } catch (DataAccessException e) {
            log.error("是否存在小时无机污染物数据(isExistHourIp)出错", e);
        }
        return bool;
    }

    @Override
    public boolean isExistHourSp(String locationId, Date datetime) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        //转换时间类型
        paraMap.put("datetime", DateUtils.getDate(datetime, HOUR));
        boolean bool = false;
        try {
            int count = (Integer) getSqlSession().selectOne(
                    "DataProcess.isExistHourSp", paraMap);
            bool = count > 0;
        } catch (DataAccessException e) {
            log.error("是否存在小时无机污染物数据(isExistHourSp)出错", e);
        }
        return bool;
    }


    @Override
    public boolean isExistHourEvap(String locationId, Date datetime) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        //转换时间类型
        paraMap.put("datetime", DateUtils.getDate(datetime, HOUR));
        boolean bool = false;
        try {
            int count = (Integer) getSqlSession().selectOne(
                    "DataProcess.isExistHourEvap", paraMap);
            bool = count > 0;
        } catch (DataAccessException e) {
            log.error("是否存在小时蒸发量数据(isExistHourEvap)出错", e);
        }
        return bool;
    }

    @Override
    public boolean isExistDayRB(String locationId, Date date) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("date", date);
        boolean bool = false;
        try {
            bool = (Boolean) getSqlSession().selectOne(
                    "DataProcess.isExistDayRB", paraMap);
        } catch (DataAccessException e) {
            log.error("是否存在日降水量数据(isExistDayRB)出错", e);
        }
        return bool;
    }


    @Override
    public boolean isExistDayEvap(String locationId, Date date) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("locationId", locationId);
        paraMap.put("date", date);
        boolean bool = false;
        try {
            bool = (Boolean) getSqlSession().selectOne(
                    "DataProcess.isExistDayEvap", paraMap);
        } catch (DataAccessException e) {
            log.error("是否存在日蒸发量数据(isExistDayEvap)出错", e);
        }
        return bool;
    }

    @Override
    public List<LocationVo> getDeviceListBySensor(int sensorPhysicalId) {
        List<LocationVo> list = new ArrayList<LocationVo>();
        try {
            list = getSqlSession().selectList(
                    "DataProcess.getDeviceListBySensor", sensorPhysicalId);
        } catch (DataAccessException e) {
            log.error("获取指定传感设备列表(getDeviceListBySensor)出错", e);
        }
        return list;
    }

    @Override
    public List<Stock> findStocks(String locationId, Date startDate, Date endDate) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return getSqlSession().selectList("DataProcess.findStocks", paramMap);
    }

    @Override
    public Stock findStockKD(String locationId, int sensorId, Date yesterday2) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", yesterday2);
        paramMap.put("sensorId", sensorId);
        return getSqlSession().selectOne("DataProcess.findStockKD", paramMap);
    }

    @Override
    public Float findStockEndValue(String locationId, int sensorId, int num, Date stamp) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("sensorId", sensorId);
        paramMap.put("num", num);
        paramMap.put("stamp", stamp);
        return (Float) getSqlSession().selectOne("DataProcess.findStockEndValue", paramMap);
    }

    @Override
    public void insertStock(Stock stock) {
        getSqlSession().insert("DataProcess.insertStock", stock);
    }

    /**
     * 添加玫瑰图数据
     *
     * @param windMark   风向标
     * @param locationId 监测点
     * @param date       时间
     * @param season     季度
     */
    @Override
    public void addWindRose(WindMark windMark, String locationId, Date date, int season) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", DateUtils.getDate(date, DATE));
        paramMap.put("windCalm", windMark.getWindCalmRate());
        paramMap.put("season", season);
        paramMap.put("sum", windMark.getWindRoseCount());
        paramMap.put("o_n", windMark.getDirectionN());
        paramMap.put("s_n", windMark.getSpeedN());
        paramMap.put("o_nne", windMark.getDirectionNNE());
        paramMap.put("s_nne", windMark.getSpeedNNE());
        paramMap.put("o_ne", windMark.getDirectionNE());
        paramMap.put("s_ne", windMark.getSpeedNE());
        paramMap.put("o_ene", windMark.getDirectionENE());
        paramMap.put("s_ene", windMark.getSpeedENE());
        paramMap.put("o_e", windMark.getDirectionE());
        paramMap.put("s_e", windMark.getSpeedE());
        paramMap.put("o_ese", windMark.getDirectionESE());
        paramMap.put("s_ese", windMark.getSpeedESE());
        paramMap.put("o_se", windMark.getDirectionSE());
        paramMap.put("s_se", windMark.getSpeedSE());
        paramMap.put("o_sse", windMark.getDirectionSSE());
        paramMap.put("s_sse", windMark.getSpeedSSE());
        paramMap.put("o_s", windMark.getDirectionS());
        paramMap.put("s_s", windMark.getSpeedS());
        paramMap.put("o_ssw", windMark.getDirectionSSW());
        paramMap.put("s_ssw", windMark.getSpeedSSW());
        paramMap.put("o_sw", windMark.getDirectionSW());
        paramMap.put("s_sw", windMark.getSpeedSW());
        paramMap.put("o_wsw", windMark.getDirectionWSW());
        paramMap.put("s_wsw", windMark.getSpeedWSW());
        paramMap.put("o_w", windMark.getDirectionW());
        paramMap.put("s_w", windMark.getSpeedW());
        paramMap.put("o_wnw", windMark.getDirectionWNW());
        paramMap.put("s_wnw", windMark.getSpeedWNW());
        paramMap.put("o_nw", windMark.getDirectionNW());
        paramMap.put("s_nw", windMark.getSpeedNW());
        paramMap.put("o_nnw", windMark.getDirectionNNW());
        paramMap.put("s_nnw", windMark.getSpeedNNW());
        try {
            getSqlSession().insert("DataProcess.addWindRose", paramMap);
        } catch (DataAccessException e) {
            log.error("添加玫瑰图数据(addWindRose)出错", e);
        }
    }

    /**
     * 添加玫瑰图数据
     *
     * @param windMark   风向标
     * @param locationId 监测点
     * @param date       时间
     * @param season     季度
     */
    @Override
    public void updateWindRose(WindMark windMark, String locationId, Date date, int season) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", DateUtils.getDate(date, DATE));
        paramMap.put("windCalm", windMark.getWindCalmRate());
        paramMap.put("season", season);
        paramMap.put("sum", windMark.getWindRoseCount());
        paramMap.put("o_n", windMark.getDirectionN());
        paramMap.put("s_n", windMark.getSpeedN());
        paramMap.put("o_nne", windMark.getDirectionNNE());
        paramMap.put("s_nne", windMark.getSpeedNNE());
        paramMap.put("o_ne", windMark.getDirectionNE());
        paramMap.put("s_ne", windMark.getSpeedNE());
        paramMap.put("o_ene", windMark.getDirectionENE());
        paramMap.put("s_ene", windMark.getSpeedENE());
        paramMap.put("o_e", windMark.getDirectionE());
        paramMap.put("s_e", windMark.getSpeedE());
        paramMap.put("o_ese", windMark.getDirectionESE());
        paramMap.put("s_ese", windMark.getSpeedESE());
        paramMap.put("o_se", windMark.getDirectionSE());
        paramMap.put("s_se", windMark.getSpeedSE());
        paramMap.put("o_sse", windMark.getDirectionSSE());
        paramMap.put("s_sse", windMark.getSpeedSSE());
        paramMap.put("o_s", windMark.getDirectionS());
        paramMap.put("s_s", windMark.getSpeedS());
        paramMap.put("o_ssw", windMark.getDirectionSSW());
        paramMap.put("s_ssw", windMark.getSpeedSSW());
        paramMap.put("o_sw", windMark.getDirectionSW());
        paramMap.put("s_sw", windMark.getSpeedSW());
        paramMap.put("o_wsw", windMark.getDirectionWSW());
        paramMap.put("s_wsw", windMark.getSpeedWSW());
        paramMap.put("o_w", windMark.getDirectionW());
        paramMap.put("s_w", windMark.getSpeedW());
        paramMap.put("o_wnw", windMark.getDirectionWNW());
        paramMap.put("s_wnw", windMark.getSpeedWNW());
        paramMap.put("o_nw", windMark.getDirectionNW());
        paramMap.put("s_nw", windMark.getSpeedNW());
        paramMap.put("o_nnw", windMark.getDirectionNNW());
        paramMap.put("s_nnw", windMark.getSpeedNNW());
        try {
            getSqlSession().update("DataProcess.updateWindRose", paramMap);
        } catch (DataAccessException e) {
            log.error("添加玫瑰图数据(updateWindRose)出错", e);
        }
    }


    /**
     * 添加小时降雨量
     *
     * @param locationId 设备编号
     * @param rbSum      降雨量总数
     */
    @Override
    public void addHourRb(String locationId, Date datetime, double rbSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("rbSum", rbSum);
        try {
            getSqlSession().insert("DataProcess.addHourRb", paramMap);
        } catch (DataAccessException e) {
            log.error("添加小时降雨量(addHourRb)出错", e);
        }
    }

    @Override
    public void addHourAvgValue(String locationId, Date datetime, double avgValue, int sensorId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("datetime", datetime);
        paramMap.put("avgValue", avgValue);
        paramMap.put("sensorId", sensorId);
        try {
            getSqlSession().insert("DataProcess.addHourAvgValue", paramMap);
        } catch (Exception e) {
            log.error("添加小时水流量出错", e);
        }
    }

    @Override
    public void addHourOp(String locationId, Date datetime, double opSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("opSum", opSum);
        try {
            getSqlSession().insert("DataProcess.addHourOp", paramMap);
        } catch (DataAccessException e) {
            log.error("添加小时有机污染物(addHourOp)出错", e);
        }
    }

    @Override
    public void addHourIp(String locationId, Date datetime, double ipSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("ipSum", ipSum);
        try {
            getSqlSession().insert("DataProcess.addHourIp", paramMap);
        } catch (DataAccessException e) {
            log.error("添加小时无机污染物(addHourIp)出错", e);
        }
    }

    @Override
    public void addHourSp(String locationId, Date datetime, double spSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("spSum", spSum);
        try {
            getSqlSession().insert("DataProcess.addHourSp", paramMap);
        } catch (DataAccessException e) {
            log.error("添加小时含硫污染物(addHourSp)出错", e);
        }
    }

    /**
     * 添加小时蒸发量
     *
     * @param locationId 设备编号
     * @param evapSum    蒸发量总数
     */
    @Override
    public void addHourEvap(String locationId, Date datetime, double evapSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("evapSum", evapSum);
        try {
            getSqlSession().insert("DataProcess.addHourEvap", paramMap);
        } catch (DataAccessException e) {
            log.error("添加小时蒸发量(addHourEvap)出错", e);
        }
    }

    /**
     * 修改小时降雨量
     *
     * @param locationId 设备编号
     * @param datetime   数据的时间
     * @param rbSum      降雨量总数
     */
    @Override
    public void updateHourRb(String locationId, Date datetime, double rbSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("rbSum", rbSum);
        try {
            getSqlSession().update("DataProcess.updateHourRb", paramMap);
        } catch (DataAccessException e) {
            log.error("修改小时降雨量(updateHourRb)出错", e);
        }
    }

    @Override
    public void updateHourOp(String locationId, Date datetime, double opSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("opSum", opSum);
        try {
            getSqlSession().update("DataProcess.updateHourOp", paramMap);
        } catch (DataAccessException e) {
            log.error("修改小时有机污染物(updateHourOp)出错", e);
        }
    }

    @Override
    public void updateHourIp(String locationId, Date datetime, double ipSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("ipSum", ipSum);
        try {
            getSqlSession().update("DataProcess.updateHourIp", paramMap);
        } catch (DataAccessException e) {
            log.error("修改小时无机污染物(updateHourIp)出错", e);
        }
    }

    @Override
    public void updateHourSp(String locationId, Date datetime, double spSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("spSum", spSum);
        try {
            getSqlSession().update("DataProcess.updateHourSp", paramMap);
        } catch (DataAccessException e) {
            log.error("修改小时含硫污染物(updateHourSp)出错", e);
        }
    }

    /**
     * 修改小时蒸发量
     *
     * @param locationId 设备编号
     * @param datetime   数据的时间
     * @param evapSum    蒸发量总数
     */
    @Override
    public void updateHourEvap(String locationId, Date datetime, double evapSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", datetime);
        paramMap.put("evapSum", evapSum);
        try {
            getSqlSession().update("DataProcess.updateHourEvap", paramMap);
        } catch (DataAccessException e) {
            log.error("修改小时蒸发量(updateHourEvap)出错", e);
        }
    }

    /**
     * 添加天降雨量
     *
     * @param locationId 设备编号
     * @param date       数据的时间
     * @param rbSum      降雨量总数
     */
    @Override
    public void addDayRb(String locationId, Date date, double rbSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", DateUtils.getDate(date, DATE));
        paramMap.put("rbSum", rbSum);
        try {
            getSqlSession().insert("DataProcess.addDayRb", paramMap);
        } catch (DataAccessException e) {
            log.error("添加天降雨量(addDayRb)出错", e);
        }
    }

    /**
     * 修改天降雨量
     *
     * @param locationId 设备编号
     * @param date       数据的时间
     * @param rbSum      降雨量总数
     */
    @Override
    public void updateDayRb(String locationId, Date date, double rbSum) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", DateUtils.getDate(date, DATE));
        paramMap.put("rbSum", rbSum);
        try {
            getSqlSession().update("DataProcess.updateDayRb", paramMap);
        } catch (DataAccessException e) {
            log.error("修改天降雨量(updateDayRb)出错", e);
        }
    }

    /**
     * 添加均峰值信息
     *
     * @param locationId  监测点编号
     * @param avgPeakInfo 均峰值数据
     */
    @Override
    public void addAvgPeak(String locationId, AvgPeak avgPeakInfo) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("sensorPhysicalid", avgPeakInfo.getSensorPhysicalid());
        paramMap.put("max", avgPeakInfo.getMaxValue());
        paramMap.put("maxTime", avgPeakInfo.getMaxTime());
        paramMap.put("min", avgPeakInfo.getMinValue());
        paramMap.put("minTime", avgPeakInfo.getMinTime());
        paramMap.put("avg", avgPeakInfo.getAvgValue());
        paramMap.put("avgTime", avgPeakInfo.getAvgTime());
        paramMap.put("waveValue", avgPeakInfo.getWaveValue());
        try {
            getSqlSession().insert("DataProcess.addAvgPeak", paramMap);
        } catch (DataAccessException e) {
            log.error("添加均峰值信息(addAvgPeak)出错", e);
        }
    }

    @Override
    public void addZoneAvgPeak(Date date) {
        getSqlSession().insert("DataProcess.addZoneAvgPeak", date);
    }

    /**
     * 修改均峰值信息
     *
     * @param locationId  监测点编号
     * @param avgPeakInfo 均峰值数据
     */
    @Override
    public void updateAvgPeak(String locationId, AvgPeak avgPeakInfo) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("sensorPhysicalid", avgPeakInfo.getSensorPhysicalid());
        paramMap.put("max", avgPeakInfo.getMaxValue());
        paramMap.put("maxTime", avgPeakInfo.getMaxTime());
        paramMap.put("min", avgPeakInfo.getMinValue());
        paramMap.put("minTime", avgPeakInfo.getMinTime());
        paramMap.put("avg", avgPeakInfo.getAvgValue());
        paramMap.put("avgTime", DateUtils.getDate(avgPeakInfo.getAvgTime(), DATE));
        paramMap.put("waveValue", avgPeakInfo.getWaveValue());
        try {
            getSqlSession().update("DataProcess.updateAvgPeak", paramMap);
        } catch (DataAccessException e) {
            log.error("修改均峰值信息(updateAvgPeak)出错", e);
        }
    }

    @Override
    public void updateKDJ(Stock stock) {
        try {
            getSqlSession().update("DataProcess.updateKDJ", stock);
        } catch (DataAccessException e) {
            log.error("修改kdj信息(updateKDJ)出错", e);
        }
    }

    @Override
    public void updateComplianceRate(Threshold threshold, Date date, String locationId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("locationId", locationId);
        params.put("sensorId", threshold.getSensorId());
        params.put("conditionType", threshold.getConditionType());
        params.put("target", threshold.getTarget());
        params.put("floating", threshold.getFloating());
        Date startTime = new DateTime(date.getTime())
                .hourOfDay().withMinimumValue()
                .minuteOfHour().withMinimumValue()
                .secondOfMinute().withMinimumValue()
                .millisOfSecond().withMinimumValue().toDate();
        Date endTime = new DateTime(date.getTime())
                .hourOfDay().withMaximumValue()
                .minuteOfHour().withMaximumValue()
                .secondOfMinute().withMaximumValue()
                .millisOfSecond().withMaximumValue().toDate();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        try {
            getSqlSession().update("DataProcess.updateComplianceRate", params);
        } catch (DataAccessException e) {
            log.error("修改设备监测指标达标率（updateComplianceRate）出错", e);
        }
    }

    /**
     * 添加光照量信息
     *
     * @param locationId 监测点编号
     * @param date       时间
     * @param season     季度
     * @param sumLux     照度总量
     */
    @Override
    public void addHourLux(String locationId, Date date, int season, double sumLux) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", DateUtils.getDate(date, HOUR));
        paramMap.put("season", season);
        paramMap.put("sumLux", sumLux);
        try {
            getSqlSession().insert("DataProcess.addHourLux", paramMap);
        } catch (DataAccessException e) {
            log.error("添加光照量信息(addHourLux)出错", e);
        }

    }

    /**
     * 修改光照量信息
     *
     * @param locationId 监测点编号
     * @param date       时间
     * @param season     季度
     * @param sumLux     照度总量
     */
    @Override
    public void updateHourLux(String locationId, Date date, int season, double sumLux) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("date", DateUtils.getDate(date, HOUR));
        paramMap.put("season", season);
        paramMap.put("sumLux", sumLux);
        try {
            getSqlSession().update("DataProcess.updateHourLux", paramMap);
        } catch (DataAccessException e) {
            log.error("修改光照量信息(updateHourLux)出错", e);
        }

    }

}
