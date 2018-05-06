package com.microwise.msp.hardware.businessservice;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.DcoOperateDao;
import com.microwise.msp.hardware.dao.DeviceDao;
import com.microwise.msp.hardware.dao.SupplymentDataDao;
import com.microwise.msp.hardware.handler.agent.CmdDeferredManager;
import com.microwise.msp.hardware.handler.agent.CmdPromise;
import com.microwise.msp.hardware.handler.codec.Packets;
import com.microwise.msp.hardware.handler.codec.v30.Commands;
import com.microwise.msp.hardware.handler.codec.v30.PacketEncoder;
import com.microwise.msp.hardware.handler.formula.Formulas;
import com.microwise.msp.hardware.netlink.DeviceChannelSuggestor;
import com.microwise.msp.hardware.netlink.UDPClient;
import com.microwise.msp.hardware.vo.Order;
import com.microwise.msp.util.CRCUtil;
import com.microwise.msp.util.DateUtils;
import com.microwise.msp.util.MergeUtil;
import com.microwise.msp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;

/**
 * <pre>
 * 下行命令包[0x0A]
 *
 * 负责生成（下行命令包）并发送, 合并反馈操作和命令生成业务
 * </pre>
 *
 * @author heming
 * @since 2011-10-08
 */
@Transactional
public class DcoOperateService {

    private static Logger log = LoggerFactory.getLogger(DcoOperateService.class);

    /**
     * 指令流水号 1-255
     */
    public static int SEQUENCE = 0;

    /**
     * 反控dao
     */
    private DcoOperateDao dao;

    /**
     * 数据回补
     */
    private SupplymentDataDao supplymentDataDao;


    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private AppCache appCache;
    @Autowired
    private DeviceChannelSuggestor deviceChannelSuggestor;

    /**
     * 设备类
     *
     * @deprecated 废弃 @gaohui 2013-11-15
     */
    private DeviceBean device = new DeviceBean();

    /**
     * 是否重发命令
     *
     * @deprecated 废弃 @gaohui 2013-11-15
     */
    private boolean isReSend;

    /**
     * 包序列号
     *
     * @deprecated 废弃 @gaohui 2013-11-15
     */
    private int sequence;

    /**
     * 下行命令对象
     *
     * @deprecated 废弃 @gaohui 2013-11-15
     */
    private Order order = new Order();

    /**
     * 下行命令包
     *
     * @deprecated 废弃 @gaohui 2013-11-15
     */
    private List<Byte> orderList = new ArrayList<Byte>(); // 下行命令包

    /**
     * 节点编号
     *
     * @deprecated 废弃 @gaohui 2013-11-15
     */
    public String nodeid;

    /**
     * 修改工作周期
     *
     * @param device 设备
     * @return 下行命令包-修改工作周期
     * @author he.ming
     * @since Jan 31, 2013
     */
    public CmdPromise modifyInterval(DeviceBean device) {
        //修改内存中间隔周期
        if (!updateMemoryInterval(device.deviceid, device.interval)) {
            return null;
        }
        // 获取设备属性
        DeviceBean dev = deviceDao.findById(device.deviceid);
        if (dev == null) {
            return null;
        }
        device.version = dev.version;
        //获取协议版本号
        if (device.version == Defines.VERSION_3) {
            return sendCommand(device.deviceid, Commands.CMD_CHANGE_INTERVAL, new byte[]{(byte) (device.interval >> 8), (byte) device.interval});
        } else {
            return null;
        }
    }

    public CmdPromise setHumidityController(String deviceId, int targetHumidity, int highHumidity, int lowHumidity) {
        byte[] cmdValue = new byte[6];
        cmdValue[0] = (byte) (targetHumidity >> 8);
        cmdValue[1] = (byte) targetHumidity;
        cmdValue[2] = (byte) (highHumidity >> 8);
        cmdValue[3] = (byte) highHumidity;
        cmdValue[4] = (byte) (lowHumidity >> 8);
        cmdValue[5] = (byte) lowHumidity;
        return sendCommand(deviceId, Commands.CMD_HUMIDITY_CONTROLLER, cmdValue);
    }

    /**
     * 修改内存中的间隔周期
     *
     * @param deviceId 设备编号
     * @param interval 间隔周期
     */
    private boolean updateMemoryInterval(String deviceId, int interval) {
        DeviceBean deviceBean = appCache.loadDevice(deviceId);
        if (deviceBean == null) {
            // 设备不存在
            return false;
        }
        deviceBean.interval = interval;
        return true;
    }

    /**
     * 开启全网巡检
     *
     * @param siteId 站点编号
     * @return 返回反馈结果
     */
    public CmdPromise openPolling(String siteId, int interval) {
        return polling(siteId, 1, interval);
    }

    /**
     * 关闭全网巡检
     *
     * @param siteId 站点编号
     * @return 返回反馈结果
     */
    public CmdPromise closePolling(String siteId) {
        return polling(siteId, 0, -1);
    }

    /**
     * 巡检
     *
     * @param siteId   站点编号
     * @param mode     模式 0 关闭巡检，1 开启巡检
     * @param interval 间隔周期
     * @return 返回反馈结果
     */
    public CmdPromise polling(String siteId, int mode, int interval) {
        CmdPromise cmdPromise = null;
        // 查询站点下的所有根节点 @gaohui 2014-04-18
        List<String> nodeIds = dao.findRootNodeIdsBySiteId(siteId);
        if (nodeIds.isEmpty()) {
            return cmdPromise;
        }
        for (String gatewayId : nodeIds) {
            switch (mode) {
                // 关闭巡检
                case 0:
                    cmdPromise = pollingClose(gatewayId);
                    break;

                // 开启巡检
                case 1:
                    cmdPromise = pollingOpen(gatewayId, interval);
                    break;
            }
        }
        return cmdPromise;
    }

    /**
     * 开启全网巡检
     *
     * @param deviceId 网关节点Id
     * @param interval 工作周期
     * @return 下行命令包-开启全网巡检
     * @author he.ming
     * @since Jan 31, 2013
     */
    public CmdPromise pollingOpen(String deviceId, int interval) {
        byte[] commandValue = new byte[]{(byte) (interval >> 8), (byte) interval};
        return sendCommand(deviceId, Commands.CMD_PATROL_CHECK_START, commandValue);
    }

    /**
     * 退出全网巡检
     *
     * @param deviceId 网关ID
     * @return 下行命令包-退出全网巡检
     * @author he.ming
     * @since Jan 31, 2013
     */
    public CmdPromise pollingClose(String deviceId) {
        return sendCommand(deviceId, Commands.CMD_PATROL_CHECK_END, new byte[0]);
    }

    /**
     * 设置默认父节点
     * <p/>
     * parentId 最多 5 位，不能为负数
     *
     * @param deviceId
     * @param parentId
     */
    public CmdPromise setDefaultParentId(String deviceId, int parentId) {
        return sendCommand(deviceId, Commands.CMD_SET_PARENT, new byte[]{(byte) parentId, (byte) (parentId >> 8)});
    }

    /**
     * 中继待机
     *
     * @param deviceId
     * @param inOrOut  进入待机 true, 退出待机 false
     */
    public CmdPromise suspendRelay(String deviceId, boolean inOrOut) {
        DeviceBean device = deviceDao.findById(deviceId);
        if (device == null) {
            return null;
        }

        if (device.deviceType != DeviceBean.TYPE_RELAY) {
            return null;
        }
        return sendCommand(deviceId, Commands.CMD_SUSPEND, new byte[]{inOrOut ? (byte) 0x01 : (byte) 0x00});
    }

    /**
     * 标定模式
     *
     * @param deviceId
     * @param inOrOut
     * @return
     */
    public CmdPromise demarcate(String deviceId, boolean inOrOut) {
        DeviceBean device = deviceDao.findById(deviceId);
        if (device == null) {
            return null;
        }

        //获取协议版本号
        if (device.version == Defines.VERSION_3) {
            return sendCommand(deviceId, Commands.CMD_DEMARCATE, new byte[]{inOrOut ? (byte) 0x01 : (byte) 0x00});
        } else {
            return null;
        }
    }

    /**
     * 设置振动传感器灵敏度级别
     *
     * @param deviceId 设备Id
     * @param level    1-高；2-中；3-低
     * @return
     */
    public CmdPromise setSensitivity(String deviceId, byte level) {
        DeviceBean device = deviceDao.findById(deviceId);
        if (device == null) {
            return null;
        }
        //获取协议版本号
        if (device.version == Defines.VERSION_3) {
            return sendCommand(deviceId, Commands.CMD_SENSITIVITY_LEVEL, new byte[]{level});
        } else {
            return null;
        }
    }

    /**
     * 设置空调目标湿度
     *
     * @param deviceId
     * @param targetHumidity
     * @return
     */
    public CmdPromise setAirConditionerHumidity(String deviceId, int targetHumidity) {
        DeviceBean device = deviceDao.findById(deviceId);
        if (device == null) {
            return null;
        }
        //获取协议版本号
        if (device.version == Defines.VERSION_3) {
            byte[] cmdValue = new byte[]{(byte) (targetHumidity >> 8), (byte) targetHumidity};
            return sendCommand(deviceId, Commands.CMD_AIRCONDITIONER_HUMIDITY_CONTROLLER, cmdValue);
        } else {
            return null;
        }
    }

    /**
     * 设置空调目标温度
     *
     * @param deviceId
     * @param targetTemperature
     * @return
     */
    public CmdPromise setAirConditionerTemperature(String deviceId, int targetTemperature) {
        DeviceBean device = deviceDao.findById(deviceId);
        if (device == null) {
            return null;
        }
        //获取协议版本号
        if (device.version == Defines.VERSION_3) {
            byte[] cmdValue = new byte[]{(byte) (targetTemperature >> 8), (byte) targetTemperature};
            return sendCommand(deviceId, Commands.CMD_AIRCONDITIONER_TEMPERATURE_CONTROLLER, cmdValue);
        } else {
            return null;
        }
    }

    /**
     * 设置空调开关状态
     *
     * @param deviceId
     * @param switchState
     * @return
     */
    public CmdPromise setAirConditionerSwitchState(String deviceId, int switchState) {
        DeviceBean device = deviceDao.findById(deviceId);
        if (device == null) {
            return null;
        }

        //获取协议版本号
        if (device.version == Defines.VERSION_3) {
            return sendCommand(deviceId, Commands.CMD_AIRCONDITIONER_SWITCH_CONTROLLER, new byte[]{switchState == 1 ? (byte) 0x01 : (byte) 0x00});
        } else {
            return null;
        }
    }

    public CmdPromise switchScreen(String deviceId, boolean onOrOff) {
        DeviceBean device = deviceDao.findById(deviceId);
        if (device == null) {
            return null;
        }

        //获取协议版本号
        if (device.version == Defines.VERSION_3) {
            return sendCommand(deviceId, Commands.CMD_SWITCH_SCREEN, new byte[]{onOrOff ? (byte) 0x01 : (byte) 0x00});
        } else {
            return null;
        }
    }

//    public CmdPromise buzzerSwitch(String deviceId, boolean onOrOff) {
////        DeviceBean device = deviceDao.findById(deviceId);
////        if (device == null) {
////            return null;
////        }
////
////        //获取协议版本号
////        if (device.version == Defines.VERSION_3) {
////            return sendCommand(deviceId, Commands.CMD_SWITCH_BUZZER, new byte[]{onOrOff ? (byte) 0x01 : (byte) 0x00});
////        } else {
////            return null;
////        }
//        return null;
//    }

    /**
     * 设备条件反射
     *
     * @param deviceId
     * @param route
     * @param subNodeId
     * @param sensorId
     * @param low
     * @param high
     * @param action
     */
    public CmdPromise setConditionRefl(String deviceId, int route, String subNodeId, int sensorId, float low, float high, int action) {
        if (route < 1 || route > 8) {
            throw new IllegalArgumentException("无效端口");
        }

        // 默认值都是零
        double originLowLeft = 0;
        double originLow = 0;
        double originLowRight = 0;
        double originHighLeft = 0;
        double originHigh = 0;
        double originHighRight = 0;

        int terminalId = 0;
        DeviceBean subDevice = deviceDao.findById(subNodeId);
        // 根据 action 判断计算一个点还是俩个点
        switch (action) {
            // 一个点
            case ConditionRefl.ACTION_LEFT_OFF_RIGHT_ON:
            case ConditionRefl.ACTION_LEFT_ON_RIGHT_OFF:
                terminalId = subDevice.selfid;
                // TODO catch exception @gaohui 2014-02-27
                originLow = computeOrigin(subNodeId, sensorId, low);
                originLowLeft = computeOrigin(subNodeId, sensorId, low * (1 - ConditionRefl.SENSOR_VALUE_DELTA));
                originLowRight = computeOrigin(subNodeId, sensorId, low * (1 + ConditionRefl.SENSOR_VALUE_DELTA));
                break;

            // 两个点
            case ConditionRefl.ACTION_RANGE_IN_OFF_OUT_ON:
            case ConditionRefl.ACTION_RANGE_IN_ON_OUT_OFF:
                terminalId = subDevice.selfid;

                originLow = computeOrigin(subNodeId, sensorId, low);
                originLowLeft = computeOrigin(subNodeId, sensorId, low * (1 - ConditionRefl.SENSOR_VALUE_DELTA));
                originLowRight = computeOrigin(subNodeId, sensorId, low * (1 + ConditionRefl.SENSOR_VALUE_DELTA));

                originHigh = computeOrigin(subNodeId, sensorId, high);
                originHighLeft = computeOrigin(subNodeId, sensorId, high * (1 - ConditionRefl.SENSOR_VALUE_DELTA));
                originHighRight = computeOrigin(subNodeId, sensorId, high * (1 + ConditionRefl.SENSOR_VALUE_DELTA));

                break;

            // 无阈值
            case ConditionRefl.ACTION_ALWAYS_ON:
            case ConditionRefl.ACTION_ALWAYS_OFF:
                break;

            // 无阈值
            case ConditionRefl.ACTION_NONE:
                break;
        }

        ByteBuffer commandParam = null;
        if (sensorId >= 0x0800 && sensorId <= 0xFFFF) { // 在此区间的传感量数值长度为4字节
            commandParam = ByteBuffer.allocate(30);
            commandParam.put((byte) route)
                    .put((byte) terminalId)
                    .put((byte) (terminalId >> 8))
                    .putShort((short) sensorId)
                    .putFloat((float) originHighLeft)
                    .putFloat((float) originHigh)
                    .putFloat((float) originHighRight)
                    .putFloat((float) originLowLeft)
                    .putFloat((float) originLow)
                    .putFloat((float) originLowRight)
                    .put((byte) action);
        } else if (sensorId >= 0x0000 && sensorId <= 0x07FF) {  // 在此区间的传感量参数值长度为 2 字节
            commandParam = ByteBuffer.allocate(18);
            commandParam.put((byte) route)
                    .put((byte) terminalId)
                    .put((byte) (terminalId >> 8))
                    .putShort((short) sensorId)
                    .putShort((short) originHighLeft)
                    .putShort((short) originHigh)
                    .putShort((short) originHighRight)
                    .putShort((short) originLowLeft)
                    .putShort((short) originLow)
                    .putShort((short) originLowRight)
                    .put((byte) action);
        }
        // return cmdPromise;
        return sendCommand(deviceId, Commands.CMD_SET_CONDITIONAL_REFL, commandParam.array());
    }

    /**
     * 通过结果值计算条件反射原始值
     *
     * @param deviceId
     * @param sensorId
     * @param target
     */
    public ConditionRefl computeConditionReflOrigin(String deviceId, int sensorId, double target) {
        double originLow = computeOrigin(deviceId, sensorId, target);
        double originLowLeft = computeOrigin(deviceId, sensorId, target * (1 - ConditionRefl.SENSOR_VALUE_DELTA));
        double originLowRight = computeOrigin(deviceId, sensorId, target * (1 + ConditionRefl.SENSOR_VALUE_DELTA));
        ConditionRefl conditionRefl = new ConditionRefl();
        conditionRefl.setLow((int) originLow);
        conditionRefl.setLowLeft((int) originLowLeft);
        conditionRefl.setLowRight((int) originLowRight);

        return conditionRefl;
    }

    /**
     * 控制 "控制模块" 开关
     *
     * @param deviceId   设备ID
     * @param whichRoute 哪一路(1-6)
     * @param onOrOff    开 true, 关 false
     */
    public CmdPromise turnSwitch(String deviceId, int whichRoute, boolean onOrOff) {
        DeviceBean controlModuleBean = appCache.loadDevice(deviceId);
        //如果设备关联的控制模块超时或电池供电情况下不发送控制指令
        if (controlModuleBean.anomaly == -1 || (int) controlModuleBean.voltage != -1) {
            return null;
        }

        if (whichRoute < 1 && whichRoute > 6) {
            return null;
        }
        return sendCommand(deviceId, Commands.CMD_TURN_SWITCH, new byte[]{
                (byte) (whichRoute),
                onOrOff ? (byte) 0x01 : (byte) 0x00});
    }

    /**
     * 重启设备
     *
     * @param deviceId
     */
    public CmdPromise restart(String deviceId) {
        return sendCommand(deviceId, Commands.CMD_RESTART, new byte[0]);
    }

    /**
     * 查询可选父节点
     *
     * @param deviceId
     */
    public CmdPromise queryAvailableParents(String deviceId) {
        return sendCommand(deviceId, Commands.CMD_QUERY_AVAILABLE_PARENTS, new byte[0]);
    }

    /**
     * 节点 RF 不休眠
     *
     * @param deviceId
     * @param enable
     * @return
     */
    public CmdPromise setRFAlive(String deviceId, boolean enable) {
        return sendCommand(deviceId, Commands.CMD_RF_ALIVE, new byte[]{(byte) (enable ? 0x01 : 0x00)});
    }

    /**
     * 修改设备阈值报警状态
     *
     * @param deviceId 设备ID
     * @param enable   true-启用 false-禁用
     * @return 命令执行结果
     */
    public CmdPromise changeThresholdAlarmState(String deviceId, boolean enable) {
        return sendCommand(deviceId, Commands.CMD_THRESHOLD_ALARM_STATE, new byte[]{(byte) (enable ? 0x01 : 0x00)});
    }

    /**
     * 修改设备监测指标阈值
     *
     * @param deviceId 设备ID
     * @param sensorId 监测指标ID
     * @param high     高阈值
     * @param low      低阈值
     * @return 命令执行结果
     */
    public CmdPromise setSensorThreshold(String deviceId, int sensorId, float high, float low) {
        ByteBuffer params = null;
        double originHigh = computeOrigin(deviceId, sensorId, high);
        double originLow = computeOrigin(deviceId, sensorId, low);
        if (sensorId >= 0x0800 && sensorId <= 0xFFFF) { // 在此区间的传感量数值长度为4字节
            params = ByteBuffer.allocate(10);
            params.putShort((short) sensorId)
                    .putFloat((float) originHigh).putFloat((float) originLow);
        } else if (sensorId >= 0x0000 && sensorId <= 0x07FF) {  // 在此区间的传感量参数值长度为 2 字节
            params = ByteBuffer.allocate(6);
            params.putShort((short) sensorId)
                    .putShort((short) originHigh)
                    .putShort((short) originLow);
        }
        return sendCommand(deviceId, Commands.CMD_SENSOR_THRESHOLD, params.array());
    }

    /**
     * 设备定位
     *
     * @param deviceId 设备id
     * @param interval 蜂鸣器响应时长
     * @return 命令执行结果
     */
    public CmdPromise locate(String deviceId, int interval) {
        return sendCommand(deviceId, Commands.CMD_LOCATE, new byte[]{(byte) interval});
    }

    /**
     * 设备定位
     *
     * @param deviceId 设备id
     * @return 命令执行结果
     */
    public CmdPromise locate(String deviceId) {
        return sendCommand(deviceId, Commands.CMD_LOCATE, new byte[0]);
    }

    /**
     * 发送命令
     *
     * @param deviceId     设备ID
     * @param command      命令标示
     * @param commandValue 命令参数
     * @return
     */
    private CmdPromise sendCommand(String deviceId, int command, byte[] commandValue) {
        DeviceBean device = appCache.loadDevice(deviceId);

        if (device == null) {
            return null;
        }


        // 获取设备路由
        List<Integer> routes = getRoutes(device);
        // 获取包序列号
        int sequence = getSequence();
        // 生成命令期望
        CmdPromise cmdPromise = CmdDeferredManager.getInstance().newCommand(deviceId, sequence);

        // 编码数据包
        byte[] datagram = PacketEncoder.encodeCommand(device.deviceType,
                device.selfid,
                sequence,
                routes.size(),
                Ints.toArray(routes),
                command,
                commandValue
        );

        // 查询建议的通道
        DeviceChannelSuggestor.SuggestChannel suggestChannel = deviceChannelSuggestor.querySuggestChannel(deviceId);

        if (suggestChannel == null) {
            return null;
        }

        // 根据通道类型 UDP/TCP 采用不同的方式发送
        if (suggestChannel.getType() == DeviceChannelSuggestor.SuggestChannel.TYPE_UDP) {
            InetSocketAddress remoteAddress = suggestChannel.getRemoteAddress();
            UDPClient.send(datagram, remoteAddress.getAddress().getHostAddress(), remoteAddress.getPort());
            logCmd(device, command, datagram);
        } else if (suggestChannel.getType() == DeviceChannelSuggestor.SuggestChannel.TYPE_TCP) {
            suggestChannel.getChannel().write(datagram);
        }

        // TODO 修改缓存位置，在 @see DeviceCommandController.java @lijianfei 2014-05-26
//        // 只有开关控制业务处理对数据包进行缓存
//        if (cmd != null) {
//            cmd.setSequence(sequence);
//            CmdCache.getInstance().put(deviceId, sequence, cmd);
//        }

        return cmdPromise;
    }

    /**
     * 打印下行命令日志
     *
     * @param device
     * @param cmdId
     * @param datagram
     */
    private static void logCmd(DeviceBean device, int cmdId, byte[] datagram) {
        Packets.log.info("[{}:{}] => 下行命令[09]-{}-{}: cmd {}, {}",
                device.remoteAddress,
                device.remotePort,
                device.deviceType,
                device.deviceid.substring(8),
                Commands.cmdName(cmdId),
                StringUtil.toHex(datagram));
    }


    /**
     * 根据设备某个监测指标的结果计算原始值
     *
     * @param deviceId
     * @param sensorId
     * @param target
     * @return
     */
    private double computeOrigin(String deviceId, int sensorId, double target) {
        // 查公式，公式系数
        // 默认公式
        Formula defaultFormula = appCache.loadFormula(sensorId);
        // 自定义公式
        Map<Integer, CustomFormula> customFormulas = null;
        if (Strings.isNullOrEmpty(deviceId)) {
            customFormulas = new HashMap<Integer, CustomFormula>();
        } else {
            customFormulas = appCache.loadCustomFormula(deviceId);
        }
        CustomFormula customFormula = customFormulas.get(sensorId);

        Formula formula = null;
        Map<String, String> formulaParams = null;
        if (customFormula != null && customFormula.getFormula() != null) {
            formula = customFormula.getFormula();
        } else {
            formula = defaultFormula;
        }

        if (customFormula != null && customFormula.getFormulaParams() != null) {
            formulaParams = customFormula.getFormulaParams();
        } else {
            formulaParams = defaultFormula.getFormulaParams();
        }

        // 首先判断 target 是否超过范围，如果超过直接返回
        if (formula.isYMinEnable() && target < formula.getMinY()) {
            // 超出范围
            throw new IllegalArgumentException("结果超出范围");
        }

        if (formula.isYMaxEnable() && target > formula.getMaxY()) {
            // 超出范围
            throw new IllegalArgumentException("结果超出范围");
        }

        // 根据 sensorId，low 与 high 计算出原始值
        return Formulas.reverseCompute(target, formula, formulaParams);
    }

    /**
     * 指令序列号
     * <p/>
     * TODO rename to nextSequence() @gaohui 2014-05-09
     *
     * @return
     */
    private static int getSequence() {
        // 0~254
        // (0~254) + 1 => 1~255
        SEQUENCE = SEQUENCE % 255;
        int seq = SEQUENCE++;
        return seq + 1;
    }

    /**
     * 获取路由
     *
     * @param device
     * @return
     */
    private List<Integer> getRoutes(DeviceBean device) {
        List<Integer> routes = new ArrayList<Integer>();

        getRoutes(device, routes);

        //倒序排列
        Collections.reverse(routes);
        return routes;
    }

    /**
     * 获取路由信息
     *
     * @param device 设备信息
     * @param routes 路由信息
     */
    private void getRoutes(DeviceBean device, List<Integer> routes) {
        String parentId = getParentIp(device.deviceid, device.parentid, device.version);
        DeviceBean parentDevice = deviceDao.findById(parentId);
        //如果父设备信息没有或者是自己，结束
        if (parentDevice == null || device.deviceid.equals(parentDevice.deviceid)) {
            return;
        }
        routes.add(parentDevice.selfid);
        if (parentDevice.deviceType == Defines.DEVICE_GATEWAY) {  // 网关停止递归查询
            return;
        }
        getRoutes(parentDevice, routes);
    }

    /**
     * 获取父节点编号
     *
     * @param nodeId      设备编号
     * @param nodeVersion 协议版本号
     * @return String 父节点编号
     * @author xiedeng
     * @date 2013-09-29
     */
    @VisibleForTesting
    public static String getParentIp(String nodeId, int parentIp, int nodeVersion) {
        int strLen = (nodeVersion == 1) ? DeviceBean.SITE_ID_LENGTH + 2 : DeviceBean.SITE_ID_LENGTH;
        int prefixLen = DeviceBean.DEVICE_ID_LENGTH - strLen; // (nodeVersion == 1) ? 3 : 5;
        return nodeId.substring(0, strLen) + StringUtil.fillZero(parentIp, prefixLen);
    }

    /**
     * 是否根节点
     *
     * @param device
     * @return
     */
    public static boolean isRoot(DeviceBean device) {
        if (device.deviceType == DeviceBean.TYPE_GATEWAY) {
            return true;
        }

        if (device.parentid == device.selfid) {
            return true;
        }

        return false;
    }

    /**
     * using StringUtil#fillZero
     *
     * @param srcValue
     * @param length
     * @return
     * @deprecated
     */
    private static String fillZero(int srcValue, int length) {
        return String.format("%0" + length + "d", srcValue);
    }


    /*   以下方法均废弃 @gaohui 2013-12-30   */


    /**
     * 单次读取SD卡里的历史数据，读取一个节点SD卡里的某小时的历史数据(小时+节点号)
     *
     * @param timestamp 小时
     * @param deviceId  节点号
     * @return
     * @author he.ming
     * @since Feb 4, 2013
     * @deprecated 废弃 @gaohui 2013-12-30
     */
    @SuppressWarnings("deprecation")
    public List<Byte> coveringDataSingle(Timestamp timestamp, String deviceId) {
        List<Byte> rList = new ArrayList<Byte>();
        device = deviceDao.findById(deviceId);

        if (null != device) {
            nodeid = deviceId;
            rList.add((byte) Defines.PACKAGE_55);
            rList.add((byte) Defines.PACKAGE_AA);
            rList.add((byte) Defines.PACKAGE_ORDER); // 协议类型（0x09表示控制协议）
            rList.add((byte) Defines.DEVICE_GATEWAY); // 终端类型
            rList.add((byte) device.version); // 协议版本
            rList.add((byte) 0x01); // 包长，封装结束后替换

            // TODO 该节点的网关ID
            // int gateway = dao.getGateWayByDeviceBean(device);
            // 节点号
            String str = deviceId.substring(8);
            int nodeid = Integer.parseInt(str);
            rList.add((byte) nodeid);
            rList.add((byte) (nodeid >> 8));
            // 包序列号
            int seq = getSequence();
            rList.add((byte) (seq));
            this.sequence = seq; // 为序列号赋值

            rList.add((byte) 0x00); // 跳数=0
            // 路由信息-无

            // 指令编号
            rList.add((byte) 0x00);
            rList.add((byte) Defines.sdCardSingleHistory);

            // 指令参数-年月日时

            rList.add((byte) nodeid);
            rList.add((byte) (nodeid >> 8));

            Calendar ca = Calendar.getInstance();
            ca.setTime(timestamp);

            // 设置sd卡回补时间
            SysConfig.sd_date = timestamp;
            int year = ca.get(Calendar.YEAR) - 2000;
            int month = ca.get(Calendar.MONTH) + 1;
            int day = ca.get(Calendar.DAY_OF_MONTH);
            int hour = ca.get(Calendar.HOUR_OF_DAY);
            rList.add((byte) year);
            rList.add((byte) month);
            rList.add((byte) day);
            rList.add((byte) hour);

            // 重新计算包长(总长度-包头5字节-包长1字节+CRC2字节)
            int size = rList.size() - 6 + 2;
            rList.remove(5);
            rList.add(5, (byte) size);

            // CRC
            int c = CRCUtil.generateCRC(rList);
            rList.add((byte) (c >> 8));
            rList.add((byte) c);

            orderList = new ArrayList<Byte>();
            orderList.addAll(rList);

            log.info("\n\n 单次读取SD卡历史数据：" + StringUtil.toHex(rList) + " \n\n");

            // UDPClient.send(rList, deviceBean.remoteAddress,
            // deviceBean.remotePort);
            order.setRemoteAddress(device.remoteAddress);
            order.setRemotePort(device.remotePort);
            order.setOrderCode(Defines.sdCardSingleHistory);
            order.setNodeId(device.deviceid);
            String tmpseq = StringUtil.fillZero(sequence, 3);
            String seriStr = StringUtil.fillZero(order.getOrderCode(), 5);
            order.setSerialNum(StringUtil.fillZero(nodeid, 5).concat(tmpseq)
                    .concat(seriStr));
            submitToGateWay();
        } else {
            log.error("\n\n 单次读取SD卡历史数据：未找到网关 \n\n");
        }

        return rList;
    }

    /**
     * 向网关提交指令包
     *
     * @deprecated 废弃 @gaohui 2013-12-30
     */
    void submitToGateWay() {
        if (isReSend) {
            dao.updateOrder(order); // 更新指令包（适用于重发命令）
            UDPClient.sendDirectly(orderList, device.remoteAddress,
                    device.remotePort);
        } else if (!dao.isSameOrder(order)) { // 不存在相同命令时生成新命令
            order.setSubmitTime(StringUtil.nowTimestamp());
            Gson gson = new Gson();
            order.setOrderStr(gson.toJson(orderList));
            dao.storageOrder(order); // 存储指令包
            UDPClient.sendDirectly(orderList, device.remoteAddress,
                    device.remotePort);
        }
    }

    /**
     * 重新生成下行命令包
     *
     * @param orderId 库中的命令Id
     * @author he.ming
     * @since Feb 1, 2013
     * @deprecated 废弃 @gaohui 2013-12-30
     */
    public void orderRemake(int orderId) {
        isReSend = true; // 将isReSend设为true，重新发送
        order = dao.getOrderPropertyFromDB(orderId); // 加载命令参数
        order.setOrderId(orderId);
        if (!checkSubmitCount()) {
            return;
        } else {
            switch (order.getOrderCode()) { // 重新生成下行命令
                case Defines.updateInterval: // TODO 修改工作周期

                    break;
                case Defines.pollingOpen: // TODO 开启巡检

                    break;
                case Defines.pollingClose: // TODO 退出巡检

                    break;
                case Defines.sdCardHistory: // TODO 读取SD卡历史数据,3月前不考虑此指令，2013-2-1

                    break;
                case Defines.sdCardSingleHistory: // 单次读取SD卡历史数据(小时+节点号)
                    // TODO 数据回补

                    break;
            }
            submitToGateWay();
        }
    }

    /**
     * 设置下行命令,命令失效
     *
     * @param orderId 下行命令在库中的记录标识
     * @author he.ming
     * @since Feb 1, 2013
     * @deprecated 废弃 @gaohui 2013-12-30
     */
    private void setOrderInvalid(int orderId) {
        dao.setOrderInvalid(orderId);
    }

    /**
     * <pre>
     * 检查下行命令发送次数。
     *
     * 发送次数大于5次不再发送，将命令状态(invalid=0)改为失效
     * </pre>
     *
     * @return
     * @deprecated 废弃 @gaohui 2013-12-30
     */
    boolean checkSubmitCount() {
        if (order.getSubmitCount() >= 5) {
            String msg = "";
            msg = device.deviceid + "执行(" + codeToZh(order.getOrderCode())
                    + ")发送失败！";
            // SysConfig.getInstance().getMessage_queue().offer(msg);
            this.setOrderInvalid(order.getOrderId());
            return false;
        }
        return true;
    }

    /**
     * 解析命令响应包(0x0A)
     *
     * @param datagram 命令响应包
     * @return
     * @author he.ming
     * @since Jan 31, 2013
     * @deprecated 废弃 @gaohui 2013-12-30
     */
    public boolean analysisFeedBackPackage(List<Byte> datagram) {
        order = new Order();
        device = new DeviceBean();

        int index = 0;
        datagram.get(index++); // 帧头1
        datagram.get(index++); // 帧头2
        device.packageType = datagram.get(index++); // 协议类型
        device.deviceType = datagram.get(index++); // 响应终端类型
        device.version = datagram.get(index++); // 协议版本
        device.size = datagram.get(index++); // 包长

        byte ipLow = datagram.get(index++); // Low
        byte ipHigh = datagram.get(index++); // High
        int deviceId = MergeUtil.merge2((char) ipHigh, (char) ipLow); // 反馈地址ID
        device.selfid = deviceId;

        sequence = datagram.get(index++); // 源命令包序列号
        order.setSerialNum(String.valueOf(sequence));

        byte orderHigh = datagram.get(index++); // High
        byte orderLow = datagram.get(index++); // Low
        int orderCode = MergeUtil.merge2((char) orderHigh, (char) orderLow);// 指令编号
        order.setOrderCode(orderCode);

        int currentState = datagram.get(index++); // 指令反馈结果[01成功/02失败/03送达成功/FF路径不通]
        order.setCurrentState(currentState); // 指令执行结果

        if (currentState != Defines.SENDGATEWAY_OK
                || currentState != Defines.PATHCLOSE) { // 非03/FF
            byte intervalHigh;
            byte intervalLow;
            int interval = 0;

            switch (orderCode) { // 依据指令编号，解析当前状态
                case Defines.updateInterval: // 0010，修改工作周期
                    // 当前状态（工作周期2byte）
                    intervalHigh = datagram.get(index++);
                    intervalLow = datagram.get(index++);
                    interval = MergeUtil.merge2((char) intervalHigh,
                            (char) intervalLow);
                    break;

                case Defines.pollingOpen: // 0011，开启巡检
                    // 当前状态（工作周期2byte）
                    intervalHigh = datagram.get(index++);
                    intervalLow = datagram.get(index++);
                    interval = MergeUtil.merge2((char) intervalHigh,
                            (char) intervalLow);
                    break;

                case Defines.pollingClose: // 0012，关闭巡检
                    // 当前状态（无）
                    break;

                case Defines.sdCardHistory: // 0080，读取SD卡历史数据
                    // 当前状态（无）
                    break;

                case Defines.sdCardSingleHistory: // 0081，单次读取SD卡历史数据
                    // 当前状态（无）
                    break;

                case Defines.sdCardSingleAccuracyHistory: // 0082，单次准确读取SD卡历史数据
                    // 当前状态（无）
                    break;

                default:
                    break;
            }
            // SysConfig.getInstance().getMessage_queue().offer(msg);
            log.info(deviceId + "执行(" + codeToZh(orderCode) + ")的反馈当前状态(工作周期)为：" + interval);
        }

        byte crcHigh = datagram.get(index++); // CRC高位
        byte crcLow = datagram.get(index++); // CRC低位
        int crc = MergeUtil.merge2((char) crcHigh, (char) crcLow);

        if (CRCUtil.getCRC(datagram) != crc) { // 命令响应包crc错误
            log.error("\n\n 命令响应包[0x0A]crc错误 \n\n");
            return false;
        } else {
            // 指令标识(目标ID[2byte]+包序列号[1byte]+指令编号[2byte])
            String tmpstr = StringUtil.fillZero(deviceId, 5);
            String tmpseq = StringUtil.fillZero(sequence, 3);
            String odc = StringUtil.fillZero(orderCode, 5);
            String orderSerial = tmpstr.concat(tmpseq).concat(odc);
            order.setOrderSerial(orderSerial);
            // 获取库中orderId并检查有效性
            // TODO 是否还有此逻辑 @gaohui 2013-12-27
            int orderId = dao.isActiveOrder(orderSerial);
            if (orderId != 0) {
                order.setOrderId(orderId);
                return true;
            }
        }
        return false;
    }

    /**
     * 处理命令响应[0x0A]信息
     *
     * @author he.ming
     * @since Feb 1, 2013
     * @deprecated 废弃 @gaohui 2013-12-30
     */
    public void operateFeedBackForOrder() {
        String msg = "";
        // 修改命令执行的反馈码[01成功/02失败/03送达成功/FF路径不通]
        dao.updateState(order.getOrderId(), order.getCurrentState());

        // 获取命令的目标ID
        // String deviceId = dao.getSelfIdByOrderid(order.getOrderId());
        String deviceId = nodeid;

        // 依据反馈码[01成功/02失败/03送达成功/FF路径不通]处理命令反馈
        switch (order.getCurrentState()) {
            case Defines.FEEDBACK_OK:
                // TODO 这块同步是世界上最神奇的同步 @gaohui 2013-07-18
                // TODO 终端执行命令成功
                synchronized (SysConfig.sdCardStep + "") {
                    if (order.getOrderCode() == Defines.sdCardSingleHistory
                            && SysConfig.sdCardStep == Defines._S_sdCardStep_Reading) {
                        // 当SD卡数据获取完成后将执行步骤设置为：读取完成
                        this.processSdCardEndSignal();
                        // 通过命令参数获取数据更新时间 yyyy-MM-dd hh
                        Order ord = dao.getOrderPropertyFromDB(order.getOrderId());
                        dao.updateDataReciveCount(ord);
                    }
                }
                // msg = deviceId + "执行(" + codeToZh(order.getOrderCode()) + ")成功！";
                // SysConfig.getInstance().getMessage_queue().offer(msg);

                // 判断是否为回补命令执行成功
                if (order.getOrderCode() == Defines.sdCardSingleHistory
                        || order.getOrderCode() == Defines.sdCardSingleAccuracyHistory) {
                    // 设置回补状态为未回补
                    int state = 0;
                    SysConfig.sd_state.put("state", state);
                    SysConfig.sd_state.put("stamp", new Date());

                    // 空数据次数加1
                    EmptyDataBean emptyDataBean = new EmptyDataBean();
                    emptyDataBean.setNodeid(deviceId);
                    emptyDataBean.setStamp(Timestamp.valueOf(DateUtils
                            .getDate(SysConfig.sd_date)));

                    emptyDataBean = supplymentDataDao.getEmptyInfo(emptyDataBean);
                    emptyDataBean.setGatewaySuccess(emptyDataBean
                            .getGatewaySuccess() + 1);
                    supplymentDataDao.updateGatewaySign(emptyDataBean);

                    // 处理二次统计
                    Date date = new Date();
                    // 设置1小时
                    date.setTime(SysConfig.sd_date.getTime() + 3600 * 1000);
//				dataProcessService.doNewDayInit();
//				dataProcessService.operateNewData(date);

                }
                break;

            case Defines.FEEDBACK_ERROR: // 指令执行失败，重发
                // msg = deviceId + "执行(" + codeToZh(order.getOrderCode()) + ")失败！";
                // SysConfig.getInstance().getMessage_queue().offer(msg);
                orderRemake(order.getOrderId());
                break;

            case Defines.SENDGATEWAY_OK: // 指令送达网关成功
                msg = deviceId + "(" + codeToZh(order.getOrderCode())
                        + ")指令送达网关成功！";
                // SysConfig.getInstance().getMessage_queue().offer(msg);
                // // 判断是否为回补命令
                // if (order.getOrderCode() == Defines.sdCardHistory
                // || order.getOrderCode() == Defines.sdCardSingleAccuracyHistory) {
                // // 设置回补状态为正在回补
                // int state = 1;
                // SysConfig.sd_state.put("state", state);
                // SysConfig.sd_state.put("stamp", DateUtils
                // .getSystemCurrentTime());
                // }
                break;

            case Defines.PATHCLOSE: // 路径不通，重发
                // msg = deviceId + "(" + codeToZh(order.getOrderCode()) + ")路径不通！";
                // SysConfig.getInstance().getMessage_queue().offer(msg);
                orderRemake(order.getOrderId());
                break;

            default:
                break;
        }
    }

    /**
     * 状态码转换中文
     *
     * @param ordercode
     * @return
     */
    String codeToZh(int ordercode) {
        String result = "";
        switch (ordercode) {
            case Defines.updateInterval: // 0010
                result = "修改工作周期";
                break;
            case Defines.pollingOpen: // 0011
                result = "开启巡检";
                break;
            case Defines.pollingClose: // 0012
                result = "关闭巡检";
                break;
            case Defines.sdCardHistory: // 0080
                result = "读取sd卡历史数据";
                break;
            case Defines.sdCardSingleHistory: // 0081,本次侧重点
                result = "单次读取sd卡历史数据";
                break;
            default:
                result = "指令未定义";
                break;
        }
        return result;
    }

    /**
     * 处理SD卡获取命令结束标识
     *
     * @deprecated 废弃 @gaohui 2013-12-30
     */
    void processSdCardEndSignal() {
        List<Byte> sdCardEndSignal = new ArrayList<Byte>();
        sdCardEndSignal.add((byte) 0x55);
        sdCardEndSignal.add((byte) 0xaa);
        sdCardEndSignal.add((byte) 0x01);
        sdCardEndSignal.add((byte) 0x07);
        sdCardEndSignal.add((byte) 0x80);
        sdCardEndSignal.add((byte) 0x01);

        // TODO 有问题, 之前逻辑是四组一包 @gaohui 2013-07-18
        SysConfig.getInstance().getDataArray().offer(sdCardEndSignal);
    }

    /**
     * 下行命令包，包头
     *
     * @author he.ming
     * @since Feb 1, 2013
     * @deprecated 2013-2-5
     */
    void appendFoot() {
        // 重新计算包长(总长度-包头5字节-包长1字节+CRC2字节)
        int size = orderList.size() - 6 + 2;
        orderList.remove(5);
        orderList.add(5, (byte) size);

        int c = CRCUtil.generateCRC(orderList);

        order.setOrderSerial(MergeUtil.merge3(sequence, (c >> 8), c) + ""); // 为序列号赋值，加上CRC

        orderList.add((byte) (c >> 8));
        orderList.add((byte) c);
    }

    public void setDao(DcoOperateDao dao) {
        this.dao = dao;
    }

    public void setSupplymentDataDao(SupplymentDataDao supplymentDataDao) {
        this.supplymentDataDao = supplymentDataDao;
    }

}
