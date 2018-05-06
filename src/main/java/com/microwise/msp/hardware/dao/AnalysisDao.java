package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.DataFile;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据解析 analysis
 *
 * @author heming
 * @since 2011-09-22
 */
public interface AnalysisDao extends BaseDao {
    /**
     * 初始化节点信息，nodeinfo
     *
     * @param deviceBean 设备信息
     * @return
     */
    public boolean initNodeInfo(DeviceBean deviceBean);

    /**
     * 初始化节点传感信息，nodeSensor[批量]
     *
     * @param sensorList 传感List
     * @author he.ming
     * @since Jan 24, 2013
     */
    public boolean initNodeSensor(List<NodeSensor> sensorList);

    /**
     * 初始化位置点传感信息，locationSensor[批量]
     *
     * @param sensorList 传感List
     * @author liuzhu
     * @since 2015-6-5
     */
    public boolean initLocationSensor(List<LocationSensor> sensorList);


    /**
     * 初始化实时状态nodeinfomemory, 并返回主键
     *
     * @param device 设备信息
     */
    public String addMemoryInfo(DeviceBean device);

    /**
     * 更新实时状态,nodeinfomemory
     *
     * @param device      设备信息
     * @param dataVersion 最新数据版本号
     */
    public void updateMermoryInfo(DeviceBean device, int dataVersion);

    /**
     * 新增节点历史数据
     *
     * @param deviceid           节点号
     * @param timeStamp          时间戳
     * @param voltage            电压
     * @param sensorPhysicalBean 传感数据
     * @param anomaly            工作状态
     */
    public void addNodeData(String deviceid, Timestamp timeStamp,
                            float voltage, SensorPhysicalBean sensorPhysicalBean, int anomaly);


    /**
     * 批量插入一包数据（包括各个监测指标的数据）
     *
     * @param deviceBean
     */
    public void addNodeDatas(DeviceBean deviceBean);

    /**
     * 更新实时数据，nodesensor
     *
     * @param device             设备信息
     * @param sensorPhysicalBean 传感信息
     */
    public void updateSensorMemory(DeviceBean device, SensorPhysicalBean sensorPhysicalBean);

    /**
     * 根据节点批量获取各监测指标版本号
     * <p/>
     * sensorPhysicalId => dataVersion
     *
     * @param deviceid
     * @return
     */
    public Map<Integer, Integer> getNodeSensorDataVersions(String deviceid);

    /**
     * 根据表名到log_transfer中获取该表最新数据版本号
     *
     * @param tableName 表名
     * @return 表最新数据版本号
     */
    public int getDataVersion(String tableName);

    /**
     * 获取精度(m_sensorinfo)
     *
     * @param sensorPhysicalid 传感标识
     * @return Integer 精度
     */
    public int getPrecison(int sensorPhysicalid);

    /**
     * 实时状态nodeinfomemory,anomaly置为1异常(插入空数据时执行此操作, 表示该节点未按工作周期采集数据)
     *
     * @param nodeInfoMemoryId 节点号
     * @param timestamp
     */
    public void anomalySetting(String nodeInfoMemoryId, Date timestamp, int anomaly);

    /**
     * 检查设备状态，同时更新数据版本
     *
     * @param nodeInfoMemoryId
     * @param timestamp
     * @param dataVersion
     * @author he.ming
     * @since Apr 26, 2013
     */
    public void anomalySetting(String nodeInfoMemoryId, Date timestamp, int anomaly,
                               long dataVersion);

    /**
     * 通过欧姆值计算对应PT100温度值(m_tbl_pt100_mapping)
     *
     * @param ohm 欧姆值
     * @return 对应PT100温度值
     */
    public double pt100Mapping(double ohm);

    /**
     * 更新实时数据，nodesensor,携带数据版本号
     *
     * @param device
     * @param phy
     * @param dataVersion
     */
    void updateSensorMemory(String id, DeviceBean device, SensorPhysicalBean phy, int dataVersion);

    /**
     * 修改数据文件记录
     * @param filename   数据文件
     * @param analysisSign  文件是被解析的标记 0 未解析 1，已经解析
     */
    public void updateDataFileRecord(String filename, int analysisSign);

    /**
     * 获取文件名称
     * @param analysisSign 文件是被解析的标记 0 未解析 1，已经解析
     * @return
     */
    public List<DataFile> getFileRecords(int analysisSign);
}
