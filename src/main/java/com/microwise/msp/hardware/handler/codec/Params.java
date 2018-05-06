package com.microwise.msp.hardware.handler.codec;

/**
 * 参数枚举
 *
 * @author gaohui
 * @date 13-8-8 17:43
 */
public class Params {
    private Params() {
    }

    /**
     * 工作周期(秒)
     */
    public static final int INTERVAL_S = 0xA000;

    /**
     * 工作周期(分钟)
     */
    public static final int INTERVAL_M = 0xA011;

    /**
     * 时间戳
     */
    public static final int TIMESTAMP = 0x0A001;
    /**
     * 接入点(一般站点ID)
     */
    public static final int SITE_ID = 0xA002;
    /**
     * SD卡状态
     */
    public static final int SD_CARD_STATE = 0xA003;
    /**
     * 分包(总包数, 第几包)
     */
    public static final int PACKET_PAGINATE = 0xA004;
    /**
     * 故障代码
     */
    public static final int FAULT_CODE = 0xA005;
    /**
     * 断网指示
     */
    public static final int OFFLINE_INSTRUCTION = 0xA006;
    /**
     * 工作模式(0: 正常, 1: 巡检)
     */
    public static final int WORK_MODE = 0xA007;

    /**
     * GPS 参数
     */
    public static final int GPS = 0xA008;

    /**
     * 控制模块的开关状态
     */
    public static final int SWITCH = 0xA009;

    /**
     * 控制模块的条件反射参数
     */
    public static final int CONDITION_REFL = 0xA00A;

    /**
     * 节点阈值报警
     */
    public static final int DEVICE_THRESHOLD = 0xA00B;

    /**
     * 子网ID
     */
    public static final int NET_ID = 0xA00C;

    /**
     * 酸雨设备状态
     */
    public static final int ACID_RAIN_STATE = 0xA00D;

    /**
     * 致美恒湿机运行状态
     */
    public static final int ZM_HUMIDITY_CONTROLLER_STATE = 0xA00E;

    /**
     * 高强无水恒湿机设备参数
     */
    public static final int GQ_HUMIDITY_CONTROLLER_STATE = 0xA012;
    /**
     * QCM 复位标识
     */
    public static final int QCM_STATE = 0xA00F;

    /**
     * 设备显示屏开关状态
     */
    public static final int SCREEN_STATE = 0xA010;

    /**
     * 设备预热时间
     */
    public static final int WARM_UP_TIME = 0xA013;

    /**
     * 振动传感器灵敏度级别（1-高；2-中；3-低）
     */
    public static final int SENSITIVITY = 0xA014;
    /**
     * 融通空调模组
     */
    public static final int RT_AIRCONDITIONER_CONTROLLER_STATE = 0xA015;
}
