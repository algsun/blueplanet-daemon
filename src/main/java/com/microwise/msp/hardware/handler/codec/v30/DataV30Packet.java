package com.microwise.msp.hardware.handler.codec.v30;

import com.microwise.msp.hardware.handler.codec.DataPacket;
import com.microwise.msp.hardware.handler.codec.Packet;

import java.util.List;

/**
 * v3.0 上行数据包
 *
 * @author gaohui
 * @date 13-8-8 13:00
 */
public class DataV30Packet extends DataPacket {
    /**
     * 0-其他 1-恒湿机 2-空调组
     */
    public static final int TYPE_HUMIDITY = 1;
    public static final int TYPE_AIRCONDITIONER = 2;

    /**
     * 复制基本信息到新的 packet
     *
     * @param packet
     */
    public DataV30Packet(Packet packet) {
        super(packet);
    }

    /**
     * SD卡状态
     */
    protected int sdCardState;

    /**
     * 分包总包数
     */
    protected int paginateCount;

    /**
     * 分包第几包 (从1开始)
     */
    protected int paginateIndex;

    /**
     * 故障代码. 可能会出现多个，每个监测指标对应一个
     */
    protected List<Integer> faultCodes;

    /**
     * 是否有搜网次数
     */
    protected boolean connectionCountExists = false;

    /**
     * 搜网次数
     */
    protected int connectionCount;

    /**
     * 是否有 GPS 参数， 默认一般设备没有
     */
    protected boolean gpsExists = false;

    /**
     * GPS 参数
     */
    List<GpsParam> gpsParams;

    /**
     * 控制模块专有：是否有控制模块开关状态
     */
    protected boolean switchExists = false;

    /**
     * 控制模块的启用状态
     */
    protected int switchEnable;

    /**
     * 控制模块的开关状态
     */
    protected int switchStatus;
    /**
     * 是否是条件反射期间的数据
     */
    private boolean switchCondRefl;

    /**
     * 各个开关状态
     */
    protected List<Switch> switches = null;

    /**
     * 控制模块条件反射参数是否存在
     */
    protected boolean conditionReflExists = false;

    /**
     * 是否携带子网ID
     */
    protected boolean netIdExists;

    /**
     * 控制模块条件反射参数
     */
    protected ConditionRefl conditionRefl;

    /**
     * 降雨状态
     *  0代表无降雨，1代表有降雨
     */
    protected Integer rainState;

    /**
     * 雨量筒状态
     *  0代表已关盖，1代表已开盖，2代表错误，3代表正在开盖或关盖
     */
    protected Integer rainGaugeState;

    /**
     * 恒湿机运行状态
     */
    protected HumidityController humidityController;

    /**
     * 空调组
     */
    protected AirConditionerController airConditionerController;


    /**
     * 设备类型 0-其他 1-恒湿机 2-空调组
     */
    protected int type;
    /**
     * QCM 状态
     *  0代表正常，1代表复位
     */
    protected Integer qcmState;

    /**
     * 显示屏开关状态
     *  0关，1开
     */
    protected Integer screenState;

    /**
     * 预热时间
     */
    protected int warmUpTime;

    protected int sensitivity;

    // TODO 添加条件反射参数属性 @gaohui 2014-02-11
    public Integer getRainState() {
        return rainState;
    }

    public void setRainState(Integer rainState) {
        this.rainState = rainState;
    }

    public Integer getRainGaugeState() {
        return rainGaugeState;
    }

    public void setRainGaugeState(Integer rainGaugeState) {
        this.rainGaugeState = rainGaugeState;
    }

    public Integer getQcmState() {
        return qcmState;
    }

    public void setQcmState(Integer qcmState) {
        this.qcmState = qcmState;
    }

    public int getSdCardState() {
        return sdCardState;
    }

    public void setSdCardState(int sdCardState) {
        this.sdCardState = sdCardState;
    }

    public int getPaginateCount() {
        return paginateCount;
    }

    public void setPaginateCount(int paginateCount) {
        this.paginateCount = paginateCount;
    }

    public int getPaginateIndex() {
        return paginateIndex;
    }

    public void setPaginateIndex(int paginateIndex) {
        this.paginateIndex = paginateIndex;
    }

    public boolean isConnectionCountExists() {
        return connectionCountExists;
    }

    public void setConnectionCountExists(boolean connectionCountExists) {
        this.connectionCountExists = connectionCountExists;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public List<Integer> getFaultCodes() {
        return faultCodes;
    }

    public void setFaultCodes(List<Integer> faultCodes) {
        this.faultCodes = faultCodes;
    }

    public HumidityController getHumidityController() {
        return humidityController;
    }

    public void setHumidityController(HumidityController humidityController) {
        this.humidityController = humidityController;
    }

    public boolean isGpsExists() {
        return gpsExists;
    }

    public void setGpsExists(boolean gpsExists) {
        this.gpsExists = gpsExists;
    }

    public List<GpsParam> getGpsParams() {
        return gpsParams;
    }

    public void setGpsParams(List<GpsParam> gpsParams) {
        this.gpsParams = gpsParams;
    }

    public boolean isSwitchExists() {
        return switchExists;
    }

    public void setSwitchExists(boolean switchExists) {
        this.switchExists = switchExists;
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

    public List<Switch> getSwitches() {
        return switches;
    }

    public void setSwitches(List<Switch> switches) {
        this.switches = switches;
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

    public boolean isSwitchCondRefl() {
        return switchCondRefl;
    }

    public void setSwitchCondRefl(boolean switchCondRefl) {
        this.switchCondRefl = switchCondRefl;
    }

    public boolean isNetIdExists() {
        return netIdExists;
    }

    public void setNetIdExists(boolean netIdExists) {
        this.netIdExists = netIdExists;
    }

    public Integer getScreenState() {
        return screenState;
    }

    public void setScreenState(Integer screenState) {
        this.screenState = screenState;
    }

    public int getType() {
        return type;
    }

    public AirConditionerController getAirConditionerController() {
        return airConditionerController;
    }

    public void setAirConditionerController(AirConditionerController airConditionerController) {
        this.airConditionerController = airConditionerController;
    }

    public void setType(int type) {
        this.type = type;
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

    /**
     * GPS 参数
     */
    public static class GpsParam {
        // 经度
        public static final int LONGITUDE = 0x01;
        // 经度类型
        public static final int LONGITUDE_TYPE = 0x02;

        // 纬度
        public static final int LATITUDE = 0x03;
        // 纬度类型
        public static final int LATITUDE_TYPE = 0x04;

        // 海拔
        public static final int ALTITUDE = 0x05;
        // 速率
        public static final int SPEED = 0x06;
        // 航向
        public static final int DIRECTION = 0x07;

        // 类型
        private int type;
        // 长度
        private int length;
        // 值（ascii）
        private byte[] bytes;
        // 值（ascii）
        private String value;

        public GpsParam(int type, int length, byte[] bytes) {
            this.type = type;
            this.length = length;
            this.bytes = bytes;
            this.value = new String(bytes);
        }

        public int getType() {
            return type;
        }

        public int getLength() {
            return length;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "{" +
                    "type=" + type +
                    ", length=" + length +
                    ", value=" + value +
                    "} ";
        }
    }

    /**
     * 条件反射参数
     */
    public static class ConditionRefl {

        /**
         * 端口/路
         */
        private int route;
        /**
         * 子设备ID
         */
        private int subTerminalId;
        /**
         * 监测指标ID
         */
        private int sensorId;

        /**
         * 左值
         */
        private int lowLeft;
        /**
         * 原始值
         */
        private int low;
        /**
         * 右值
         */
        private int lowRight;

        /**
         * 左值
         */
        private int highLeft;
        /**
         * 原始值
         */
        private int high;
        /**
         * 右值
         */
        private int highRight;

        /**
         * 动作
         */
        private int action;

        public int getRoute() {
            return route;
        }

        public void setRoute(int route) {
            this.route = route;
        }

        public int getSubTerminalId() {
            return subTerminalId;
        }

        public void setSubTerminalId(int subTerminalId) {
            this.subTerminalId = subTerminalId;
        }

        public int getSensorId() {
            return sensorId;
        }

        public void setSensorId(int sensorId) {
            this.sensorId = sensorId;
        }

        public int getLow() {
            return low;
        }

        public void setLow(int low) {
            this.low = low;
        }

        public int getHigh() {
            return high;
        }

        public void setHigh(int high) {
            this.high = high;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public int getLowLeft() {
            return lowLeft;
        }

        public void setLowLeft(int lowLeft) {
            this.lowLeft = lowLeft;
        }

        public int getLowRight() {
            return lowRight;
        }

        public void setLowRight(int lowRight) {
            this.lowRight = lowRight;
        }

        public int getHighLeft() {
            return highLeft;
        }

        public void setHighLeft(int highLeft) {
            this.highLeft = highLeft;
        }

        public int getHighRight() {
            return highRight;
        }

        public void setHighRight(int highRight) {
            this.highRight = highRight;
        }

        @Override
        public String toString() {
            return "ConditionRefl{" +
                    "route=" + route +
                    ", subTerminalId=" + subTerminalId +
                    ", sensorId=" + sensorId +
                    ", low=" + low +
                    ", high=" + high +
                    ", action=" + action +
                    '}';
        }
    }

    public static class HumidityController {
        public static final int TYPE_ZM = 1;
        public static final int TYPE_GQ = 2;
        /**
         * 恒湿机类型
         * 1-致美
         * 2-高强
         */
        private int type;
        /**
         * 当前展柜温度
         */
        private float currentTemperature;
        /**
         * 当前展柜湿度
         */
        private float currentHumidity;
        /**
         * 恒湿机设备状态
         */
        private HumidityControllerState humidityControllerState;
        /**
         * 目标温度
         */
        private int targetHumidity;
        /**
         * 湿度上限
         */
        private int highHumidity;
        /**
         * 湿度下限
         */
        private int lowHumidity;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public float getCurrentTemperature() {
            return currentTemperature;
        }

        public void setCurrentTemperature(float currentTemperature) {
            this.currentTemperature = currentTemperature;
        }

        public float getCurrentHumidity() {
            return currentHumidity;
        }

        public void setCurrentHumidity(float currentHumidity) {
            this.currentHumidity = currentHumidity;
        }

        public HumidityControllerState getHumidityControllerState() {
            return humidityControllerState;
        }

        public void setHumidityControllerState(HumidityControllerState humidityControllerState) {
            this.humidityControllerState = humidityControllerState;
        }

        public int getTargetHumidity() {
            return targetHumidity;
        }

        public void setTargetHumidity(int targetHumidity) {
            this.targetHumidity = targetHumidity;
        }

        public int getHighHumidity() {
            return highHumidity;
        }

        public void setHighHumidity(int highHumidity) {
            this.highHumidity = highHumidity;
        }

        public int getLowHumidity() {
            return lowHumidity;
        }

        public void setLowHumidity(int lowHumidity) {
            this.lowHumidity = lowHumidity;
        }
    }

    public static class HumidityControllerState {

        public HumidityControllerState() {
        }

        public HumidityControllerState(boolean humidityHigh, boolean humidityLow, boolean waterLow,
                                       boolean waterDrainageFull, boolean temperatureLow, boolean outOfRangeCauseStop,
                                       boolean outFanBreakDown, boolean cycleFanBreakDown, boolean withoutSensor,
                                       boolean ptdTemperatureHigh, boolean ptdTemperatureLow) {
            this.humidityHigh = humidityHigh;
            this.humidityLow = humidityLow;
            this.waterLow = waterLow;
            this.waterDrainageFull = waterDrainageFull;
            this.temperatureLow = temperatureLow;
            this.outOfRangeCauseStop = outOfRangeCauseStop;
            this.outFanBreakDown = outFanBreakDown;
            this.cycleFanBreakDown = cycleFanBreakDown;
            this.withoutSensor = withoutSensor;
            this.ptdTemperatureHigh = ptdTemperatureHigh;
            this.ptdTemperatureLow = ptdTemperatureLow;
        }

        public HumidityControllerState(int state) {
            this.humidityHigh = (state & 0x0004) == 0x0004;
            this.humidityLow = (state & 0x0008) == 0x0008;
            this.waterLow = (state & 0x0010) == 0x0010;
            this.waterDrainageFull = (state & 0x0020) == 0x0020;
            this.temperatureLow = (state & 0x0100) == 0x0100;
            this.outOfRangeCauseStop = (state & 0x0200) == 0x0200;
            this.outFanBreakDown = (state & 0x0400) == 0x0400;
            this.cycleFanBreakDown = (state & 0x0800) == 0x0800;
            this.withoutSensor = (state & 0x1000) == 0x1000;
            this.ptdTemperatureHigh = (state & 0x4000) == 0x4000;
            this.ptdTemperatureLow = (state & 0x8000) == 0x8000;
        }


        /**
         * 湿度报警
         * type=2 时有用
         */
        private boolean humidityAlarm;
        /**
         * 展柜湿度过高
         */
        private boolean humidityHigh;
        /**
         * 展柜湿度过低
         */
        private boolean humidityLow;

        /**
         * 缺水
         */
        private boolean waterLow;

        /**
         * 排水容器满
         */
        private boolean waterDrainageFull;

        /**
         * 温度报警
         * type=2 时有用
         */
        private boolean temperatureAlarm;

        /**
         * 环境温度过低
         */
        private boolean temperatureLow;
        /**
         * 湿度超限停机
         */
        private boolean outOfRangeCauseStop;
        /**
         * 外部风扇损坏
         */
        private boolean outFanBreakDown;
        /**
         * 循环风扇损坏
         */
        private boolean cycleFanBreakDown;
        /**
         * 无环境传感器
         */
        private boolean withoutSensor;
        /**
         * PTD温度过高
         */
        private boolean ptdTemperatureHigh;
        /**
         * PTD温度过低
         */
        private boolean ptdTemperatureLow;

        public boolean isHumidityHigh() {
            return humidityHigh;
        }

        public void setHumidityHigh(boolean humidityHigh) {
            this.humidityHigh = humidityHigh;
        }

        public boolean isHumidityLow() {
            return humidityLow;
        }

        public void setHumidityLow(boolean humidityLow) {
            this.humidityLow = humidityLow;
        }

        public boolean isWaterLow() {
            return waterLow;
        }

        public void setWaterLow(boolean waterLow) {
            this.waterLow = waterLow;
        }

        public boolean isWaterDrainageFull() {
            return waterDrainageFull;
        }

        public void setWaterDrainageFull(boolean waterDrainageFull) {
            this.waterDrainageFull = waterDrainageFull;
        }

        public boolean isTemperatureLow() {
            return temperatureLow;
        }

        public void setTemperatureLow(boolean temperatureLow) {
            this.temperatureLow = temperatureLow;
        }

        public boolean isOutOfRangeCauseStop() {
            return outOfRangeCauseStop;
        }

        public void setOutOfRangeCauseStop(boolean outOfRangeCauseStop) {
            this.outOfRangeCauseStop = outOfRangeCauseStop;
        }

        public boolean isOutFanBreakDown() {
            return outFanBreakDown;
        }

        public void setOutFanBreakDown(boolean outFanBreakDown) {
            this.outFanBreakDown = outFanBreakDown;
        }

        public boolean isCycleFanBreakDown() {
            return cycleFanBreakDown;
        }

        public void setCycleFanBreakDown(boolean cycleFanBreakDown) {
            this.cycleFanBreakDown = cycleFanBreakDown;
        }

        public boolean isWithoutSensor() {
            return withoutSensor;
        }

        public void setWithoutSensor(boolean withoutSensor) {
            this.withoutSensor = withoutSensor;
        }

        public boolean isPtdTemperatureHigh() {
            return ptdTemperatureHigh;
        }

        public void setPtdTemperatureHigh(boolean ptdTemperatureHigh) {
            this.ptdTemperatureHigh = ptdTemperatureHigh;
        }

        public boolean isPtdTemperatureLow() {
            return ptdTemperatureLow;
        }

        public void setPtdTemperatureLow(boolean ptdTemperatureLow) {
            this.ptdTemperatureLow = ptdTemperatureLow;
        }

        public boolean isHumidityAlarm() {
            return humidityAlarm;
        }

        public void setHumidityAlarm(boolean humidityAlarm) {
            this.humidityAlarm = humidityAlarm;
        }

        public boolean isTemperatureAlarm() {
            return temperatureAlarm;
        }

        public void setTemperatureAlarm(boolean temperatureAlarm) {
            this.temperatureAlarm = temperatureAlarm;
        }
    }

    public static class AirConditionerController {
        /**
         * 空调组类型
         * 1-融通
         */
        public static final int TYPE_RT = 1;

        /**
         * 空调组开关状态 0-关 1-开
         */
        public static final int SWITCH_OFF = 0;
        public static final int SWITCH_ON = 1;
        /**
         * 空调模组类型
         * 1-融通
         */
        private int type;
        /**
         * 目标温度
         */
        private float targetTemperature;
        /**
         * 目标湿度
         */
        private float targetHumidity;

        /**
         * 空调组开关状态 0-关 1-开
         */
        private int switchState;


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public float getTargetTemperature() {
            return targetTemperature;
        }

        public void setTargetTemperature(float targetTemperature) {
            this.targetTemperature = targetTemperature;
        }

        public float getTargetHumidity() {
            return targetHumidity;
        }

        public void setTargetHumidity(float targetHumidity) {
            this.targetHumidity = targetHumidity;
        }

        public int getSwitchState() {
            return switchState;
        }

        public void setSwitchState(int switchState) {
            this.switchState = switchState;
        }
    }

}
