package com.microwise.msp.hardware.dao.impl;

import com.google.common.collect.Lists;
import com.microwise.msp.hardware.businessbean.DataFile;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;
import com.microwise.msp.hardware.vo.NodeVo;
import com.microwise.msp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.sql.Timestamp;
import java.util.*;

/**
 * <pre>
 * 数据解析 analysis
 * </pre>
 *
 * @author heming
 * @since 2011-09-22
 */
public class AnalysisDaoImpl extends BaseDaoImpl implements AnalysisDao {

    private static Logger log = LoggerFactory.getLogger(AnalysisDaoImpl.class);

    /**
     * 过滤double中多余的小数位
     * <p/>
     * TODO 可以使用缓存 AppCache @gaohui 2013-07-19
     *
     * @param overMathValue    数值
     * @param sensorPhysicalid 传感标识
     * @return
     */
    private String getStringSensorValue(double overMathValue, int sensorPhysicalid) {
        // 获取精度
        int precision = getPrecison(sensorPhysicalid);
        // 精度处理
        return StringUtil.round(overMathValue, precision);
    }

    @Override
    public double pt100Mapping(double ohm) {
        double dou = 0.0;
        try {
            dou = (Double) getSqlSession().selectOne("Analysis.pt100Mapping", ohm);
        } catch (DataAccessException e) {
            log.error("\n\n 通过欧姆值计算对应PT100温度值(pt100Mapping)出错，m_tbl_pt100_mapping\n\n", e);
        }
        return dou;
    }

    @Override
    public int getPrecison(int sensorPhysicalid) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("sensorPhysicalid", sensorPhysicalid);
        Integer percison = 0;
        try {
            percison = (Integer) getSqlSession().selectOne("Analysis.getPrecison", paraMap);
        } catch (DataAccessException e) {
            percison = 0;
            log.error("\n\n 获取精度getPrecison(" + sensorPhysicalid + ")出错 \n\n", e);
        }
        return percison;
    }

    @Override
    public boolean initNodeSensor(List<NodeSensor> sensorList) {
        boolean isTrue = true;
        try {
            getSqlSession().insert("Analysis.initNodeSensorList", sensorList);
        } catch (DataAccessException e) {
            log.error("\n\n 批量-初始化节点传感信息(initNodeSensor)出错 \n\n", e);
            isTrue = false;
        }
        return isTrue;
    }

    @Override
    public boolean initLocationSensor(List<LocationSensor> sensorList) {
        boolean isTrue = true;
        try {
            getSqlSession().insert("Analysis.initLocationSensorList", sensorList);
        } catch (DataAccessException e) {
            log.error("\n\n 批量-初始化位置点传感信息(initLocationSensor)出错 \n\n", e);
            isTrue = false;
        }
        return isTrue;
    }

    @Override
    public boolean initNodeInfo(DeviceBean deviceBean) {
        boolean isTrue = true;
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("nodeid", deviceBean.deviceid);
        paraMap.put("nodeType", deviceBean.deviceType);
        paraMap.put("createtime", deviceBean.timeStamp);
        paraMap.put("X", 0);
        paraMap.put("Y", 0);
        paraMap.put("Z", 0);
        paraMap.put("siteid", deviceBean.siteId);
        paraMap.put("isThresholdAlarm", deviceBean.isThresholdAlarm);
        paraMap.put("dataVersion", 0);
        switch (deviceBean.getType()) {
            case DeviceBean.TYPE_HUMIDITY:
                paraMap.put("deviceType", 1);
                break;
            case DeviceBean.TYPE_AIRCONDITIONER:
                paraMap.put("deviceType", 2);
                break;
            default:
                paraMap.put("deviceType", 0);
                break;
        }
        try {
            getSqlSession().insert("Analysis.initNodeInfo", paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 初始化节点信息(initNodeInfo)出错 \n\n", e);
            isTrue = false;
        }
        return isTrue;
    }

    @Override
    public void addNodeData(String deviceid, Timestamp timeStamp,
                            float voltage, SensorPhysicalBean phy, int anomaly) {
        NodeVo node = new NodeVo();
        node.setNodeid(deviceid);
        node.setSensorPhysicalid(phy.sensorPhysical_id);
        node.setSensorvalue(getStringSensorValue(phy.sensor_Value, phy.sensorPhysical_id));
        node.setLowvoltage(voltage);
        node.setTime(timeStamp);
        node.setState(phy.sensor_State);
        node.setAnomaly(anomaly);
        try {
            getSqlSession().insert("Analysis.addNodeData", node);
        } catch (DataAccessException e) {
            log.error("添加历史数据(addNodeData(" + deviceid + "))出错", e);
        }
    }

    @Override
    public void addNodeDatas(DeviceBean deviceBean) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("locationId", deviceBean.locationId);
        param.put("nodeid", deviceBean.deviceid);
        param.put("time", deviceBean.timeStamp);
        param.put("anomaly", deviceBean.anomaly);
        param.put("voltage", deviceBean.voltage);
        Collection<SensorPhysicalBean> sensorValues = Lists.newArrayList(deviceBean.sensorData.values());

        List<Map<String, Object>> sensorValueMaps = new LinkedList<Map<String, Object>>();
        for (SensorPhysicalBean sensorValue : sensorValues) {
            Map<String, Object> sensorValueMap = new HashMap<String, Object>();
            int sensorPId = sensorValue.getSensorPhysical_id();
            sensorValueMap.put("sensorPhysicalId", sensorPId);
            sensorValueMap.put("value", sensorValue.getValueStr());
            sensorValueMap.put("state", sensorValue.getSensor_State());
            sensorValueMaps.add(sensorValueMap);
        }
        param.put("sensorValues", sensorValueMaps);

        try {
            getSqlSession().insert("Analysis.addNodeDatas", param);
        } catch (Exception e) {
            log.error("写历史数据", e);
        }
    }

    @Override
    public String addMemoryInfo(DeviceBean device) {
        String id = StringUtil.uuid();
        device.setNodeInfoMemoryId(id);

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("id", device.getNodeInfoMemoryId());
        paraMap.put("nodeid", device.deviceid);
        paraMap.put("nodeVersion", device.version);
        paraMap.put("isControl", device.isControl);
        paraMap.put("parentIP", device.parentid);
        paraMap.put("childIP", device.selfid);
        paraMap.put("feedbackIP", device.feedback);
        paraMap.put("sequence", device.sequence);
        paraMap.put("stamp", device.timeStamp);
        paraMap.put("emptyStamp", device.timeStamp);
        paraMap.put("interval_i", device.interval);
        paraMap.put("rssi", device.rssi);
        paraMap.put("lqi", device.lqi);
        paraMap.put("lowvoltage", device.voltage);
        paraMap.put("anomaly", device.anomaly);
        paraMap.put("remoteIp", device.remoteAddress);
        paraMap.put("remotePort", device.remotePort);
        paraMap.put("isThresholdAlarm", device.isThresholdAlarm);
        paraMap.put("sdCardState", device.sdCardState);
        try {
            getSqlSession().insert("Analysis.addSensorMemory", paraMap);
            return id;
        } catch (DataAccessException e) {
            log.error("\n\n 添加m_nodeinfomemory(addSensorMemory(" + device.deviceid + "))出错 \n\n", e);
            return null;
        }
    }

    @Override
    public void updateMermoryInfo(DeviceBean device, int dataVersion) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("id", device.getNodeInfoMemoryId());
        paraMap.put("nodeVersion", device.version);
        paraMap.put("isControl", device.isControl);
        paraMap.put("parentIP", device.parentid);
        paraMap.put("childIP", device.selfid);
        paraMap.put("feedbackIP", device.feedback);
        paraMap.put("sequence", device.sequence);
        paraMap.put("stamp", device.timeStamp);
        paraMap.put("emptyStamp", device.timeStamp);
        paraMap.put("warmUp", device.getWarmUpTime());
        paraMap.put("interval_i", device.interval);
        paraMap.put("rssi", device.rssi);
        paraMap.put("lqi", device.lqi);
        paraMap.put("lowvoltage", device.voltage);
        paraMap.put("anomaly", device.anomaly);
        paraMap.put("deviceMode", device.deviceMode); // 工作模式
        paraMap.put("remoteIp", device.remoteAddress);
        paraMap.put("remotePort", device.remotePort);
        paraMap.put("isThresholdAlarm", device.isThresholdAlarm);
        paraMap.put("sdCardState", device.sdCardState);
        paraMap.put("dataVersion", dataVersion);
        paraMap.put("nodeid", device.deviceid);
        paraMap.put("demarcate", device.getDemarcate());
        paraMap.put("sensitivity", device.getSensitivity());
        try {
            getSqlSession().update("Analysis.updateMermoryInfo", paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 更新nodeinfomemory(updateMermoryInfo)出错 \n\n", e);
        }
    }

    @Override
    public void updateSensorMemory(DeviceBean device, SensorPhysicalBean phy) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        String sensorValue = getStringSensorValue(phy.sensor_Value, phy.sensorPhysical_id);
        paraMap.put("sensorPhysicalvalue", sensorValue);
        paraMap.put("state", phy.sensor_State);
        paraMap.put("stamp", device.timeStamp);
        paraMap.put("nodeid", device.deviceid);
        paraMap.put("sensorPhysicalid", phy.sensorPhysical_id);
        try {
            getSqlSession().update("Analysis.updateSensorMemory",
                    paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 更新nodesensor(updateSensorMemory)出错 \n\n", e);
        }
    }

    @Override
    public void updateSensorMemory(String id, DeviceBean device, SensorPhysicalBean phy, int dataVersion) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("id", id);
        paraMap.put("sensorPhysicalid", phy.sensorPhysical_id);
        paraMap.put("sensorPhysicalvalue", phy.getValueStr());
        paraMap.put("stamp", device.timeStamp);
        paraMap.put("state", phy.sensor_State);
        paraMap.put("nodeid", device.deviceid);
        paraMap.put("dataVersion", dataVersion);
        try {
            getSqlSession().update("Analysis.updateSensorMemoryPlus", paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 更新nodeinfomemory版本+1(updateSensorMemoryPlus)出错 \n\n", e);
        }
    }

    @Override
    public Map<Integer, Integer> getNodeSensorDataVersions(String deviceid) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("nodeid", deviceid);

        Map<Integer, Integer> dataVersions2 = null;
        try {
            List<Map<String, Object>> dataVersions = getSqlSession().selectList("Analysis.getNodeSensorDataVersions", paraMap);
            dataVersions2 = new HashMap<Integer, Integer>();
            for (Map<String, Object> dataVersion : dataVersions) {
                dataVersions2.put((Integer) dataVersion.get("sensorPhysicalid")
                        , ((Long) dataVersion.get("dataVersion")).intValue());
            }
        } catch (DataAccessException e) {
            log.error("", e);
        }

        return dataVersions2;
    }

    @Override
    public int getDataVersion(String tableName) {
        int dataVersion = 0;
        try {
            Object obj = getSqlSession().selectOne("Analysis.getDataVersion", tableName);
            if (null != obj) {
                dataVersion = (Integer) obj;
            }
        } catch (DataAccessException e) {
            dataVersion = 0;
            log.error("\n\n 查询日志表数据版本(getDataVersion)出错 \n\n", e);
        }
        return dataVersion;
    }

    @Override
    public void anomalySetting(String nodeInfoMemoryId, Date timestamp, int anomaly) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("id", nodeInfoMemoryId);
        paraMap.put("anomaly", anomaly);
        paraMap.put("timefordb", timestamp);
        try {
            getSqlSession().update("Analysis.anomalySetting", paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 修改实时数据状态(anomalySetting)出错 \n\n", e);
        }
    }

    @Override
    public void anomalySetting(String nodeInfoMemoryId, Date timestamp, int anomaly,
                               long dataVersion) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("id", nodeInfoMemoryId);
        paraMap.put("anomaly", anomaly);
        paraMap.put("timefordb", timestamp);
        paraMap.put("dataVersion", dataVersion);
        try {
            getSqlSession().update("Analysis.anomalySettingAndVersion", paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 修改实时数据状态(anomalySetting)出错 \n\n", e);
        }
    }

    public void updateDataFileRecord(String filename, int analysisSign) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("filename", filename);
        paraMap.put("analysisSign", analysisSign);
        try {
            getSqlSession().update("Analysis.updateDataFileRecord", paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 修改数据文件记录(updateDataFileRecord)出错 \n\n", e);
        }
    }

    public List<DataFile> getFileRecords(int analysisSign) {
        List<DataFile> files = new ArrayList<DataFile>();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("analysisSign", analysisSign);
        try {
            files = getSqlSession().selectList("Analysis.getFileRecords", paraMap);
        } catch (DataAccessException e) {
            log.error("\n\n 获取数据文件记录(getFileRecords)出错 \n\n", e);
        }
        return files;
    }

}
