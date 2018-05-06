package com.microwise.msp.hardware.businessbean;

import com.google.common.base.Strings;
import com.google.common.collect.EvictingQueue;
import com.microwise.msp.hardware.vo.LocationSensor;
import com.microwise.msp.hardware.vo.NodeSensor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备
 *
 * @author heming
 * @since 2011-10-12
 */
//TODO 添加位置点字段信息
public class DeviceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 0-其他 1-恒湿机 2-空调组
     */
    public static final int TYPE_HUMIDITY = 1;
    public static final int TYPE_AIRCONDITIONER = 2;

    /**
     * 站点ID号长度
     */
    public static final int SITE_ID_LENGTH = 8;

    /**
     * 设备ID号长度
     */
    public static final int DEVICE_ID_LENGTH = 13;

    public static final int TYPE_GATEWAY = 7;
    public static final int TYPE_CONTROL_MODULE = 5;
    public static final int TYPE_SLAVE_MODULE = 4;
    public static final int TYPE_MASTER_MODULE = 3;
    public static final int TYPE_RELAY = 2;
    public static final int TYPE_NODE = 1;

    /**
     * 默认工作周期, 1200秒/20分钟
     */
    public static final int DEFAULT_INTERVAL = 1200;

    /**
     * 振动设备灵敏度(1-高，2-中，3-低)
     */
    public static final int SENSITIVITY_HIGH = 1;
    public static final int SENSITIVITY_MID = 2;
    public static final int SENSITIVITY_LOW = 3;

    // *********************Base*****************
    /**
     * 协议类型 1Byte
     */
    public int packageType;

    /**
     * 位置点ID
     */
    public String locationId;

    /**
     * 设备名称
     */
    public String deviceName;

    /**
     * 设备类型 1Byte
     */
    public int deviceType;

    /**
     * 协议版本 1Byte
     */
    public int version;

    /**
     * 包长 1Byte （包括crc，但不包括本身）
     */
    public int size;

    /**
     * 子网ID 1Byte
     */
    public int netid;

    /**
     * 3.11.10之后定义为可控标识：isControl 跳数 1Byte 协议包在WSN阶段的路由跳数
     *
     * @deprecated
     */
    public int jump;

    /**
     * 可控标识 1Byte 0可反控、1不可反控
     */
    public int isControl = 1;

    /**
     * 父节点IP号 2Byte(低位在前，高位在后)
     */
    public int parentid;

    /**
     * 设备IP号 2Byte(低位在前，高位在后)
     */
    public int selfid;

    /**
     * 反馈地址IP号 2Byte(低位在前，高位在后)
     */
    public int feedback;

    /**
     * 包序列号 1Byte,0x01--0xFF循环自增(无论发送成功与否),设备初始化后首个包序列号为0x00
     */
    public int sequence;

    /**
     * <pre>
     * 电压值1Byte,
     * 0x00:外部供电正常,,0x14--0xFF:标识电池电压值
     * 呈现时做除10处理，保留小数点后1位(0：正常 1：低电压 2：设备掉电)	 *
     * </pre>
     */
    public float voltage;

    /**
     * 接收信号强度(有符号型) RSSI 16 1Byte
     */
    public int rssi;

    /**
     * 连接质量参数(有符号型) LQI 17 1Byte
     */
    public int lqi;

    /**
     * 远程通讯地址
     */
    public String remoteAddress;

    /**
     * 远程通讯端口
     */
    public int remotePort;

    // ********************扩展信息***************
    /**
     * 工作周期
     */
    public int interval;

    /**
     * 时间戳
     */
    public Timestamp timeStamp;

    /**
     * 实时数据的时间戳，仅用于缓存
     */
    private Date dataTimestamp;

    /**
     * 阈值报警状态
     * <p/>
     * 0-禁用
     * 1-启用
     */
    public int isThresholdAlarm;

    /**
     * 设备检测指标阈值
     */
    public List<Threshold> thresholdList;
    /**
     * 接入点
     */
    public String siteId;

    /**
     * 产品入网唯一标识：接入点(8位)+IP号(5位)
     */
    public String deviceid;

    /**
     * <pre>
     * SD卡状态(仅网关时适用，即设备类型为0x07)：
     * 0x00表示没插卡或卡没插好
     * 0x01表示卡已插好
     * 0x02表示卡已写满
     * </pre>
     */
    public int sdCardState = 0;

    // 分包(总包数、第几包)2byte
    // 分包，总包数
    public int paginateCount;
    // 分包，第几包
    public int paginateIndex;

    // TODO 故障代码(保留字段、故障类型)2byte

    // TODO 断网指示(搜网次数高、低)2byte

    /**
     * 工作模式：0-正常(默认) 1-巡检
     */
    public int deviceMode = 0;

    /**
     * 原始数据包，2013-1-15
     */
    public String packet;

    /**
     * 传感数据Map<传感标识，传感值>
     */
    public Map<Integer, SensorPhysicalBean> sensorData = new HashMap<Integer, SensorPhysicalBean>();

    /**
     * 光照
     */
    public double lux;

    /**
     * 紫外
     */
    public double uv;

    /**
     * 降雨量
     */
    public double rbcount;

    /**
     * 雨量累计, 默认为 -1
     */
    public double cumulativeRainfall = -1;

    /**
     * 液面高度，（蒸发量累积值）
     */
    public double cumulativePwl = -1;

    /**
     * qcm 复位标识（系统缓存中用到SysConfig）
     */
    public Map<Integer, Integer> qcmState = new HashMap<Integer, Integer>();

    /**
     * 当前qcm的标识
     */
    public int qcm;

    /**
     * 上一包数据(有机污染物)
     */
    public double last_timeOrganicPol = 0;

    /**
     * 上一包数据(无机污染物)
     */
    public double last_timeInorganicPol = 0;

    /**
     * 上一包数据(含硫污染物)
     */
    public double last_timeSulfurousPol = 0;

    // ********************Other*******************
    /**
     * 数据分组号
     */
    public int groupid;

    /**
     * 数据版本号
     */
    public int dataVersion;

    /**
     * 是否正常 (-1:异常 0：正常 1：低电压 2：设备掉电)
     */
    public int anomaly;

    /**
     * sd卡数据回填算法中，数据库中对应记录的生成时间
     */
    public Timestamp recordTime;

    /**
     * 分包缓存，默认为6包数据（其实是完整的3包数据）
     */
    public EvictingQueue<DeviceSequenceStampBean> deviceSequenceStampBeans = EvictingQueue.create(6);

    // 以下为非数据包数据，用来更新数据使用

    /**
     * 产品序列号
     */
    private String sn = "";

    /**
     * 设备对应 nodeinfomemory 中的id
     */
    private String nodeInfoMemoryId;

    /**
     * sensorPhysicalId => nodeSensor
     */
    private Map<Integer, NodeSensor> nodeSensors;

    /**
     * sensorPhysicalId => locationSensor
     */
    private Map<Integer, LocationSensor> locationSensor;

    /**
     * 控制模块专有：是否有控制模块开关状态
     */
    private boolean switchExists = false;

    /**
     * 控制模块的启用状态
     */
    private int switchEnable;

    /**
     * 控制模块的开关状态
     */
    private int switchStatus;

    /**
     * 开关状态
     */
    private Switches switches;

    /**
     * 控制模块条件反射参数是否存在
     */
    private boolean conditionReflExists = false;

    /**
     * 是否是条件反射期间的数据
     */
    private boolean switchCondRefl;

    /**
     * 控制模块条件反射参数
     */
    private ConditionRefl conditionRefl;

    /**
     * 是否是上传的数据
     */
    private boolean isUpload;

    /**
     * 是否第一包数据
     */
    private boolean isFirstPacket;

    /**
     * 是否最后一包数据
     */
    private boolean isLastPacket;

    /**
     * 是否pm传感
     */
    private boolean isContainPmSensor;


    /**
     * 0-其他 1-恒湿机 2-空调组
     */
    private int type;

    /**
     * 电压阈值
     */
    private Float voltageThreshold;

    /**
     * 故障代码. 可能会出现多个，每个监测指标对应一个
     */
    private List<Integer> faultCodes;

    /**
     * 故障代码,用于数据库存储
     */
    private String faultCode;

    /**
     * 设备属性
     */
    private DeviceProperties deviceProperties;

    /**
     * 扩充字段
     */
    private String content;

    private int uploadState;

    private int humCompensate = 1;

    /**
     * 标定状态
     */
    private int demarcate;

    /**
     * 设备预热时间
     */
    private int warmUpTime;

    /**
     * 是否是1.3节点通过V3网关
     */
    protected boolean isDummyV3Version;
    /**
     * 振动传感器灵敏度（1-高，2-中，3-低）
     */
    private int sensitivity;

    public boolean isHumCompensate() {
        return humCompensate == 1;
    }

    public void setHumCompensate(int humCompensate) {
        this.humCompensate = humCompensate;
    }

    public int getHumCompensate() {
        return humCompensate;
    }


    public DeviceBean() {
        this.interval = DEFAULT_INTERVAL;
    }

    public DeviceBean(String deviceId) {
        this.deviceid = deviceId;
    }

    public boolean isConfigLocation() {
        return !Strings.isNullOrEmpty(this.locationId);
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public boolean isSwitchCondRefl() {
        return switchCondRefl;
    }

    public void setSwitchCondRefl(boolean switchCondRefl) {
        this.switchCondRefl = switchCondRefl;
    }

    public String getNodeInfoMemoryId() {
        return nodeInfoMemoryId;
    }

    public void setNodeInfoMemoryId(String nodeInfoMemoryId) {
        this.nodeInfoMemoryId = nodeInfoMemoryId;
    }

    public Map<Integer, NodeSensor> getNodeSensors() {
        return nodeSensors;
    }

    public void setNodeSensors(Map<Integer, NodeSensor> nodeSensors) {
        this.nodeSensors = nodeSensors;
    }

    public int getSwitchEnable() {
        return switchEnable;
    }

    public void setSwitchEnable(int switchEnable) {
        this.switchEnable = switchEnable;
    }

    public int getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(int switchStatus) {
        this.switchStatus = switchStatus;
    }

    public boolean isSwitchExists() {
        return switchExists;
    }

    public void setSwitchExists(boolean switchExists) {
        this.switchExists = switchExists;
    }

    public boolean isConditionReflExists() {
        return conditionReflExists;
    }

    public void setConditionReflExists(boolean conditionReflExists) {
        this.conditionReflExists = conditionReflExists;
    }

    public ConditionRefl getConditionRefl() {
        return conditionRefl;
    }

    public void setConditionRefl(ConditionRefl conditionRefl) {
        this.conditionRefl = conditionRefl;
    }

    public Date getDataTimestamp() {
        return dataTimestamp;
    }

    public void setDataTimestamp(Date dataTimestamp) {
        this.dataTimestamp = dataTimestamp;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Switches getSwitches() {
        return switches;
    }

    public void setSwitches(Switches switches) {
        this.switches = switches;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public boolean isLastPacket() {
        return isLastPacket;
    }

    public void setLastPacket(boolean isLastPacket) {
        this.isLastPacket = isLastPacket;
    }

    public boolean isFirstPacket() {
        return isFirstPacket;
    }

    public void setFirstPacket(boolean isFirstPacket) {
        this.isFirstPacket = isFirstPacket;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static int getSiteIdLength() {
        return SITE_ID_LENGTH;
    }

    public DeviceProperties getDeviceProperties() {
        return deviceProperties;
    }

    public void setDeviceProperties(DeviceProperties deviceProperties) {
        this.deviceProperties = deviceProperties;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public List<Integer> getFaultCodes() {
        return faultCodes;
    }

    public void setFaultCodes(List<Integer> faultCodes) {
        this.faultCodes = faultCodes;
    }

    public boolean isDummyV3Version() {
        return isDummyV3Version;
    }

    public void setDummyV3Version(boolean isDummyV3Version) {
        this.isDummyV3Version = isDummyV3Version;
    }

    public boolean isContainPmSensor() {
        return isContainPmSensor;
    }

    public void setContainPmSensor(boolean isContainPmSensor) {
        this.isContainPmSensor = isContainPmSensor;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    public Map<Integer, LocationSensor> getLocationSensor() {
        return locationSensor;
    }

    public void setLocationSensor(Map<Integer, LocationSensor> locationSensor) {
        this.locationSensor = locationSensor;
    }

    public int getDemarcate() {
        return demarcate;
    }

    public void setDemarcate(int demarcate) {
        this.demarcate = demarcate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Float getVoltageThreshold() {
        return voltageThreshold;
    }

    public void setVoltageThreshold(Float voltageThreshold) {
        this.voltageThreshold = voltageThreshold;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public int getWarmUpTime() {
        return warmUpTime;
    }

    public void setWarmUpTime(int warmUpTime) {
        this.warmUpTime = warmUpTime;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public int getIsControl() {
        return isControl;
    }

    public void setIsControl(int isControl) {
        this.isControl = isControl;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getSelfid() {
        return selfid;
    }

    public void setSelfid(int selfid) {
        this.selfid = selfid;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getLqi() {
        return lqi;
    }

    public void setLqi(int lqi) {
        this.lqi = lqi;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getIsThresholdAlarm() {
        return isThresholdAlarm;
    }

    public void setIsThresholdAlarm(int isThresholdAlarm) {
        this.isThresholdAlarm = isThresholdAlarm;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int getDeviceMode() {
        return deviceMode;
    }

    public void setDeviceMode(int deviceMode) {
        this.deviceMode = deviceMode;
    }

    public String getPacket() {
        return packet;
    }

    public void setPacket(String packet) {
        this.packet = packet;
    }

    public static int getDeviceIdLength() {
        return DEVICE_ID_LENGTH;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public Map<Integer, SensorPhysicalBean> getSensorData() {
        return sensorData;
    }

    public void setSensorData(Map<Integer, SensorPhysicalBean> sensorData) {
        this.sensorData = sensorData;
    }

    public int getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(int anomaly) {
        this.anomaly = anomaly;
    }
}
