package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.vo.LocationVo;
import com.microwise.msp.hardware.vo.NodeVo;

import java.util.Date;
import java.util.List;

/**
 * 数据二次加工dao
 *
 * @author he.ming
 * @since 2013-1-10
 */

public interface DataProcessDao extends BaseDao {


    /**
     * 获取小时降雨量信息
     *
     * @param locationId
     * @param datetime
     * @return
     */
    public Double getHourRbSum(String locationId, Date datetime);

    /**
     * 根据时间段获取水流量平均值
     *
     * @param locationId 位置点id
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 时间段内的水流量平均值
     */
    public Double getAvgByTimeAndSensorId(String locationId, Date startDate, Date endDate, int sensorId);

    /**
     * 获取小时有机污染物
     *
     * @param locationId
     * @param datetime
     * @return
     */
    public Double getHourOpSum(String locationId, Date datetime);

    /**
     * 获取小时无机污染物
     *
     * @param locationId
     * @param datetime
     * @return
     */
    public Double getHourIpSum(String locationId, Date datetime);

    /**
     * 获取小时含硫污染物
     *
     * @param locationId
     * @param datetime
     * @return
     */
    public Double getHourSpSum(String locationId, Date datetime);

    /**
     * 获取小时蒸发量总数
     *
     * @param locationId 位置点编号
     * @param datetime   时间
     * @return 蒸发量总数
     */
    public Double getHourEvapSum(String locationId, Date datetime);

    /**
     * 获取天蒸发量总数
     *
     * @param locationId 位置点编号
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 蒸发量总数
     */
    public Double getDayEvapSum(String locationId, Date startTime, Date endTime);

    /**
     * 获取小时光照量总数
     *
     * @param locationId 位置点编号
     * @param datetime   时间
     * @return 光照量总数
     */
    public List<Lux> getHourLuxs(String locationId, Date datetime);


    /**
     * 功能：根据数据查询数据的时间
     *
     * @param locationId       位置点编号
     * @param value            监测指标值
     * @param sensorPhysicalId 监测指标编号
     * @param date             监测指标时间
     * @return 数据的时间
     */
    public Date getCreateTimeByData(String locationId, double value, int sensorPhysicalId, Date date);

    /**
     * 获取均峰值信息
     *
     * @param locationId 位置点编号
     * @param date       时间
     * @return 均峰值信息
     */
    public List<AvgPeak> getAvgPeaks(String locationId, Date date);

    /**
     * 获取天降雨量信息
     *
     * @param locationId 位置点编号
     * @param startTime  时间
     * @return 降雨量信息
     */
    public Double getDayRbSum(String locationId, Date startTime, Date endTime);

    /**
     * 获取玫瑰图
     *
     * @param locationId 位置点编号
     * @param date       时间
     * @return 玫瑰图信息
     */
    public List<WindRose> getWindRoses(String locationId, Date date);

    /**
     * 获取玫瑰图的数据量
     *
     * @param locationId 位置点编号
     * @param date       时间
     * @param isCalmWind 是否查询静风(true是,false不是)
     * @return
     */
    public Integer getWindRoseCount(String locationId, Date date, boolean isCalmWind);


    /**
     * 获取pm 数据
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public List<NodeData> getPmSensorData(Date startDate, Date endDate);

    /**
     * 删除传感数据
     *
     * @param deviceId 设备编号
     * @param sensorId 传感编号
     */
    public void deleteSensors(String deviceId, int sensorId);

    /**
     * 删除区域均值数据
     *
     * @param date 日期
     */
    public void deleteZoneAvgPeak(Date date);

    /**
     * <pre>
     * 功能：是否存在均峰值数据
     * </pre>
     *
     * @param locationId       位置点编号
     * @param date             时间
     * @param sensorPhysicalId 传感量id
     * @return
     * @author xuexu
     */
    boolean isExistDatePeakAvg(String locationId, Date date, int sensorPhysicalId);

    /**
     * 功能：是否存在kdj数据
     *
     * @param locationId       位置点编号
     * @param date             时间
     * @param sensorPhysicalId 传感量id
     * @return
     * @author liuzhu
     */
    boolean isExistDateKDJ(String locationId, Date date, int sensorPhysicalId);

    /**
     * <pre>
     * 功能：是否存在风向玫瑰图数据
     * </pre>
     *
     * @param locationId 位置点编号
     * @param date       时间
     * @return
     * @author xuexu
     */
    boolean isExistDateRose(String locationId, Date date);


    /**
     * 是否存在小时照度累计数据
     *
     * @param locationId 位置点编号
     * @param datetime   时间
     * @return
     */
    boolean isExistHourLux(String locationId, Date datetime);


    /**
     * 功能：是否存在小时降水量数据
     *
     * @param locationId 位置点编号
     * @param datetime   时间
     * @return
     */
    boolean isExistHourRB(String locationId, Date datetime);

    /**
     * 功能：是否存在小时有机污染物数据
     *
     * @param locationId 位置点编号
     * @param datetime   时间
     * @return
     */
    boolean isExistHourOp(String locationId, Date datetime);

    /**
     * 功能：是否存在小时无机污染物数据
     *
     * @param locationId 位置点编号
     * @param datetime   时间
     * @return
     */
    boolean isExistHourIp(String locationId, Date datetime);

    /**
     * 功能：是否存在小时含硫污染物数据
     *
     * @param locationId 位置点编号
     * @param datetime   时间
     * @return
     */
    boolean isExistHourSp(String locationId, Date datetime);

    /**
     * 功能：是否存在日降水量数据
     *
     * @param locationId 位置点编号
     * @param date       时间
     * @return
     */
    boolean isExistDayRB(String locationId, Date date);

    /**
     * 是否存在小时蒸发量
     *
     * @param locationId 位置点编号
     * @param datetime   时间
     * @return
     */
    public boolean isExistHourEvap(String locationId, Date datetime);


    /**
     * <pre>
     * 功能：是否存在日蒸发量数据
     * </pre>
     *
     * @param locationId 位置点编号
     * @param date       时间
     * @return
     */
    public boolean isExistDayEvap(String locationId, Date date);

    /**
     * <pre>
     * 功能：获取指定传感设备列表,sensorId为0时表示获取所有监测点设备列表
     * </pre>
     *
     * @param sensorId 传感id
     * @return 设备集合
     * @author xuexu
     */
    List<LocationVo> getDeviceListBySensor(int sensorId);

    /**
     * 获取开始值、结束值、最高值、最低值
     *
     * @param locationId 位置点id
     * @param startDate  开始时间
     * @return
     */
    public List<Stock> findStocks(String locationId, Date startDate, Date endDate);

    /**
     * 获取前一天的KD
     *
     * @param locationId 位置点id
     * @param sensorId   监测指标id
     * @param yesterday2 时间
     * @return
     */
    public Stock findStockKD(String locationId, int sensorId, Date yesterday2);

    /**
     * 根据位置点id，监测指标id
     *
     * @param locationId 位置点id
     * @param sensorId   监测指标id
     * @param num        数量（五日均线：5 十日均线：10 三十日均线30）
     * @return
     */
    public Float findStockEndValue(String locationId, int sensorId, int num, Date stamp);

    /**
     * 添加stock
     *
     * @param stock 对象
     */
    public void insertStock(Stock stock);

    /**
     * 添加玫瑰图数据
     *
     * @param windMark   风向标
     * @param locationId 监测点
     * @param date       时间
     * @param season     季度
     */
    public void addWindRose(WindMark windMark, String locationId, Date date, int season);

    /**
     * 添加玫瑰图数据
     *
     * @param windMark   风向标
     * @param locationId 监测点
     * @param date       时间
     * @param season     季度
     */
    public void updateWindRose(WindMark windMark, String locationId, Date date, int season);

    /**
     * 添加小时降雨量
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param rbSum      降雨量总数
     */
    public void addHourRb(String locationId, Date datetime, double rbSum);

    /**
     * 添加小时水流量
     *
     * @param locationId   位置点编号
     * @param datetime     数据的时间
     * @param waterFlowAvg 水流量的平均值
     */
    public void addHourAvgValue(String locationId, Date datetime, double waterFlowAvg, int sensorId);

    /**
     * 添加小时有机污染物
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param opSum      有机污染物总数
     */
    public void addHourOp(String locationId, Date datetime, double opSum);

    /**
     * 添加小时无机污染物
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param ipSum      无机污染物总数
     */
    public void addHourIp(String locationId, Date datetime, double ipSum);

    /**
     * 添加小时含硫污染物
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param spSum      含硫污染物总数
     */
    public void addHourSp(String locationId, Date datetime, double spSum);

    /**
     * 添加小时蒸发量
     *
     * @param locationId 设备编号
     * @param datetime   数据的时间
     * @param evapSum    蒸发量总数
     */
    public void addHourEvap(String locationId, Date datetime, double evapSum);

    /**
     * 修改小时降雨量
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param rbSum      降雨量总数
     */
    public void updateHourRb(String locationId, Date datetime, double rbSum);

    /**
     * 修改小时有机污染物
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param rbSum      降雨量总数
     */
    public void updateHourOp(String locationId, Date datetime, double rbSum);

    /**
     * 修改小时无机污染物
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param rbSum      降雨量总数
     */
    public void updateHourIp(String locationId, Date datetime, double rbSum);

    /**
     * 修改小时含硫污染物
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param rbSum      降雨量总数
     */
    public void updateHourSp(String locationId, Date datetime, double rbSum);

    /**
     * 修改小时蒸发量
     *
     * @param locationId 位置点编号
     * @param datetime   数据的时间
     * @param evapSum    蒸发量总数
     */
    public void updateHourEvap(String locationId, Date datetime, double evapSum);

    /**
     * 添加天降雨量
     *
     * @param locationId 位置点编号
     * @param date       数据的时间
     * @param rbSum      降雨量总数
     */
    public void addDayRb(String locationId, Date date, double rbSum);

    /**
     * 修改天降雨量
     *
     * @param locationId 位置点编号
     * @param date       数据的时间
     * @param rbSum      降雨量总数
     */
    public void updateDayRb(String locationId, Date date, double rbSum);

    /**
     * 添加均峰值信息
     *
     * @param locationId  位置点编号
     * @param avgPeakInfo 均峰值数据
     */
    public void addAvgPeak(String locationId, AvgPeak avgPeakInfo);


    /**
     * 添加区域均值信息
     */
    public void addZoneAvgPeak(Date date);

    /**
     * 修改均峰值信息
     *
     * @param locationId  位置点编号
     * @param avgPeakInfo 均峰值数据
     */
    public void updateAvgPeak(String locationId, AvgPeak avgPeakInfo);

    /**
     * 修改kdj信息
     */
    public void updateKDJ(Stock stock);

    /**
     * 计算设备指标达标率
     *
     * @param threshold 达标条件
     * @param date      日期
     */
    public void updateComplianceRate(Threshold threshold, Date date, String locationId);


    /**
     * 添加光照量信息
     *
     * @param locationId 位置点编号
     * @param date       时间
     * @param season     季度
     * @param sumLux     照度总量
     */
    public void addHourLux(String locationId, Date date, int season, double sumLux);

    /**
     * 修改光照量信息
     *
     * @param locationId 位置点编号
     * @param date       时间
     * @param season     季度
     * @param sumLux     照度总量
     */
    public void updateHourLux(String locationId, Date date, int season, double sumLux);


}
