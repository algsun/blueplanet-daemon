package com.microwise.msp.hardware.handler.codec.v30;

/**
 * @date 2014-01-26
 * @autor gaohui
 */
public class Commands {
    /**
     * 修改工作周期
     */
    public static final int CMD_CHANGE_INTERVAL = 0X0010;

    /**
     * 开始巡检
     */
    public static final int CMD_PATROL_CHECK_START = 0X0011;

    /**
     * 开始巡检
     */
    public static final int CMD_PATROL_CHECK_END = 0X0012;

    /**
     * 强制授时
     */
    public static final int CMD_FORCE_SET_TIME = 0x0013;

    /**
     * 指定默认父节点
     */
    public static final int CMD_SET_PARENT = 0x0014;

    /**
     * 设置条件反射
     */
    public static final int CMD_SET_CONDITIONAL_REFL = 0x0015;

    /**
     * 设置开关状态
     */
    public static final int CMD_TURN_SWITCH = 0x0016;

    /**
     * 重启
     */
    public static final int CMD_RESTART = 0x0020;

    /**
     * 中继待机
     */
    public static final int CMD_SUSPEND = 0x0021;

    /**
     * 查询可选父节点
     */
    public static final int CMD_QUERY_AVAILABLE_PARENTS = 0x0024;

    /**
     * 节点 RF 不休眠
     */
    public static final int CMD_RF_ALIVE = 0x0025;

    /**
     * 设置阈值报警状态
     */
    public static final int CMD_THRESHOLD_ALARM_STATE = 0x0026;

    /**
     * 设置设备检测指标阈值
     */
    public static final int CMD_SENSOR_THRESHOLD = 0x0027;

    /**
     * 恒湿机参数设置
     */
    public static final int CMD_HUMIDITY_CONTROLLER = 0x0028;

    /**
     * 显示屏开关控制
     */
    public static final int CMD_SWITCH_SCREEN = 0X0029;

    /**
     * 设备定位
     */
    public static final int CMD_LOCATE = 0x0083;

    /**
     * 标定模式
     */
    public static final int CMD_DEMARCATE = 0x0084;

    /**
     * 灵敏度级别 （1-高;2-中;3-低）
     */
    public static final int CMD_SENSITIVITY_LEVEL = 0x0085;
    /**
     * 空调组开关控制
     */
    public static final int CMD_AIRCONDITIONER_SWITCH_CONTROLLER = 0x0086;
    /**
     * 空调组湿度控制
     */
    public static final int CMD_AIRCONDITIONER_HUMIDITY_CONTROLLER = 0x0087;
    /**
     * 空调组温度控制
     */
    public static final int CMD_AIRCONDITIONER_TEMPERATURE_CONTROLLER = 0x0088;


    /**
     * 命令执行成功
     */
    public static final int FEEDBACK_SUCCESS = 0x01;
    /**
     * 命令执行失败
     */
    public static final int FEEDBACK_FAILED = 0x02;
    /**
     * 命令收到
     */
    public static final int FEEDBACK_GOT = 0x03;


    /**
     * 指令标识对应的名称
     *
     * @param orderId
     * @return
     */
    public static String cmdName(int orderId) {
        String orderHex = String.format("0x%04X", orderId);
        String orderName = "";
        switch (orderId) {
            case 0x0010:
                orderName = "[change interval]";
                break;
            case 0x0012:
                orderName = "[patrol check end]";
                break;
            case 0x0011:
                orderName = "[patrol check start]";
                break;
            case 0x0013:
                orderName = "[set time]";
                break;
            case 0x0014:
                orderName = "[set parent id]";
                break;
            case 0x0015:
                orderName = "[set conditional reflect]";
                break;
            case 0x0016:
                orderName = "[turn switch]";
                break;
            case 0x0020:
                orderName = "[restart device]";
                break;
            case 0x0021:
                orderName = "[relay suspend]";
                break;
            case 0x0024:
                orderName = "[query available parents]";
                break;
            case 0x0025:
                orderName = "[rf alive]";
                break;
            case 0x0026:
                orderName = "[change device threshold alarm state]";
                break;
            case 0x0027:
                orderName = "[set device sensor threshold]";
                break;
            case 0x0028:
                orderName = "[set humidity machine params]";
                break;
            case 0x0029:
                orderName = "[switch screen state]";
                break;
            case 0x0084:
                orderName = "[set device demarcate]";
                break;
            case 0x0085:
                orderName = "[set device sensitivity level]";
                break;
            case 0x0086:
                orderName = "[set airConditioner switchState]";
                break;
            case 0x0087:
                orderName = "[set airConditioner targetHumidity]";
                break;
            case 0x0088:
                orderName = "[set airConditioner targetTemperature]";
                break;


        }
        return orderHex + orderName;
    }

    /**
     * 反馈对应的名称
     *
     * @param feedback
     * @return
     */
    public static String feedbackName(int feedback) {
        String feedbackHex = String.format("0x%02X", feedback);
        String feedbackName = "";
        switch (feedback) {
            case 0x01:
                feedbackName = "[success]";
                break;
            case 0x02:
                feedbackName = "[failure]";
                break;
            case 0x03:
                feedbackName = "[get]";
                break;
            case 0xFF:
                feedbackName = "[bad route]";
                break;
        }

        return feedbackHex + feedbackName;
    }
}
