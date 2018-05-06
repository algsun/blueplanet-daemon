/**
 *
 */
package com.microwise.msp.hardware.common;

/**
 * <pre>
 *  协议版本:       version
 *  协议类型:       packge
 *  终端类型:       device
 *  通讯模式:       _WS
 *  boolean参数状态：_WS
 *  通讯状态:       _Sock_State
 *  通讯参数:       _NetConf
 *  终端供电状态：   _Power
 *  Email模板类型： _Email_
 *  传感信息：      _Sensor_
 *  传感值状态
 *  传感器标识
 *  传感量标识定义
 *  sd卡数据获取操作执行步骤标识 ：_S_sdCardStep_
 *  下行命令包0x09--控制指令编号
 * </pre>
 *
 * @author heming
 * @since 2011-09-23
 */
public final class Defines {

    /**
     * 线程休眠时间
     */
    public static final int WAITTIME = 1;

    // ================================协议版本================================
    /**
     * 协议版本1.3
     */
    public static final int VERSION_1 = 0x01;
    /**
     * 协议版本2.1
     */
    public static final int VERSION_2 = 0x02;
    /**
     * 协议版本3.0
     */
    public static final int VERSION_3 = 0x03;

    // ================================包头================================
    /**
     * 包头55
     */
    public static final int PACKAGE_55 = 0x55;
    /**
     * 包头AA
     */
    public static final int PACKAGE_AA = 0xAA;

    // ================================Common================================
    /**
     * 默认数据版本号0
     */
    public static final int _dataVersion_Default = 0; // 默认数据版本号
    /**
     * 默认传感值0.0
     */
    public static final double _Sensor_Value_Default = 0.0; // 默认传感值

    // ================================boolean参数状态================================
    /**
     * 正常
     */
    public static final int _WS_OK = 1;
    /**
     * 异常
     */
    public static final int _WS_ERR = 0;

    // ================================协议类型================================
    /**
     * 上行数据包01
     */
    public static final int PACKAGE_DATA = 0x01; // 上行数据包
    /**
     * 数据应答包02
     */
    public static final int PACKAGE_DATARESPONSE = 0x02; // 数据应答包
    /**
     * 上行请求包05
     */
    public static final int PACKAGE_REQUEST = 0x05; // 上行请求包
    /**
     * 请求应答包06
     */
    public static final int PACKAGE_RESPONSE = 0x06; // 请求应答包
    /**
     * 下行命令包09
     */
    public static final int PACKAGE_ORDER = 0x09; // 下行命令包
    /**
     * 命令响应包0A
     */
    public static final int PACKAGE_FEEDBACK = 0x0A; // 命令响应包(下行命令包的反馈)

    // ================================上行请求包05--请求应答包，指令编号================================
    /**
     * 连接请求 0x01
     */
    public static final int ORDER_LINK = 0x01; // 连接请求
    /**
     * 授时请求 0x02
     */
    public static final int ORDER_SETTIME = 0x02; // 授时请求

    // ================================终端类型================================
    /**
     * 节点
     */
    public static final int DEVICE_NODE = 0x01;
    /**
     * 中继
     */
    public static final int DEVICE_RELAY = 0x02;
    /**
     * 节点-主模块，可控+无数据
     */
    public static final int DEVICE_NODE_MASTER = 0x03;
    /**
     * 节点-从模块，不可控+数据
     */
    public static final int DEVICE_NODE_SLAVE = 0x04;
    /**
     * 网关
     */
    public static final int DEVICE_GATEWAY = 0x07;

    // ================================终端供电状态================================
    /**
     * 正常
     */
    public static final int _Power_Ok = 0;
    /**
     * 低电压
     */
    public static final int _Power_low = 1;
    /**
     * 掉电
     */
    public static final int _Power_Lose = 2;
    /**
     * 终端异常
     */
    public static final int _Power_Anomaly = 1;

    // =================================终端参数定义================================
    /**
     * 0xA000 2byte，工作周期
     */
    public static final int interval = 0xA000;
    /**
     * 0xA001 6byte，时间戳(校时)，高→低(年、月、日、时、分、秒)
     */
    public static final int timeStamp = 0xA001;
    /**
     * 0xA002 4byte，接入点号
     */
    public static final int deviceidWithGateway = 0xA002;
    /**
     * 0xA003 2byte，SD卡状态(只有网关携带此标识)
     */
    public static final int sdCardState = 0xA003;
    /**
     * 0xA004 2byte,分包。上行数据包总包数(1byte)，第几包(1byte)
     */
    public static final int assembPacket = 0xA004;
    /**
     * 0xA005 2byte,保留字段、故障类型各一个字节
     */
    public static final int faultCode = 0xA005;
    /**
     * 0xA006 2byte,搜网次数高、低字节
     */
    public static final int networkInstruction = 0xA006;
    /**
     * 0xA007,2byte,高字节保留，低字节为工作模式(0:正常，1:巡检)
     */
    public static final int model = 0xA007;

    // ================================通讯模式================================
    public static final int _WS_Communication_Null = 0; // 位置通讯接口
    public static final int _WS_SerialPort = 1; // 串口通讯
    public static final int _WS_Udp = 2; // udp通讯
    public static final int _WS_Tcp_Client = 3; // tcp客户端通讯
    public static final int _WS_Tcp_Server = 4; // tcp服务端

    // ================================通讯状态================================
    /**
     * 未连接
     */
    public static final int _Sock_State_Disconnect = 0; // 未连接
    /**
     * 正在连接
     */
    public static final int _Sock_State_Conning = 1; // 正在连接
    /**
     * 已连接
     */
    public static final int _Sock_State_Connected = 2; // 已连接
    /**
     * 正在关闭连接
     */
    public static final int _Sock_State_Closing = 3; // 正在关闭连接
    /**
     * 网络断开缓冲时间
     */
    public static final int _Sock_BufferTimer = 10; // 网络断开缓冲时间 15秒

    // ================================通讯参数================================
    public static final int _NetConf_Radds = 1; // 远程主机
    public static final int _NetConf_Rport = 2; // 远程端口
    public static final int _NetConf_Lport = 3; // 本地端口
    public static final int _NetConf_Sport = 4; // 串口
    public static final int _NetConf_Brate = 5; // 波特率
    public static final int _NetConf_Model = 6; // 通讯模式
    public static final int _NetConf_State = 7; // 连接状态

    // ================================Email模板类型================================
    public static final int _Email_LosePower = 1; // 掉电模板
    public static final int _Email_Threshold = 2; // 阀值模板
    public static final int _Email_LowPower = 3; // 低电压模板
    public static final int _Email_Anomaly = 4; // 终端工作异常

    // ================================传感信息定义Map[sensorid,Map[sensorMark,value]],该定义即sensorMark值含义
    /**
     * 传感标识
     */
    public static final int _Sensor_id = 1;
    /**
     * 传感状态
     */
    public static final int _Sensor_State = 2;
    /**
     * 传感值
     */
    public static final int _Sensor_Value = 3;
    /**
     * 传感器版本
     */
    public static final int _Sensor_Version = 4;

    // ================================传感状态定义,传感信息定义中2（传感状态）的取值范围===========
    /**
     * 采样失败异常
     */
    public static final int _Sensor_State_Err = 0;
    /**
     * 采样正常
     */
    public static final int _Sensor_State_OK = 1;
    /**
     * 低于低阀值
     */
    public static final int _Sensor_State_L = 2;
    /**
     * 超过高阀值
     */
    public static final int _Sensor_State_H = 3;
    /**
     * 空数据
     */
    public static final int _Sensor_State_Ept = 4;

    // ================================下行命令包0x09--控制指令定义================================
    /**
     * 0x0010 修改工作周期(单位为s)
     */
    public static final int updateInterval = 0x0010;
    /**
     * 0x0011 开启巡检，2byte
     */
    public static final int pollingOpen = 0x0011;
    /**
     * 0x0012 退出巡检，0byte
     */
    public static final int pollingClose = 0x0012;
    /**
     * 0x0080 读取sd卡历史数据4byte(年、月、日、时)
     */
    public static final int sdCardHistory = 0x0080;
    /**
     * 0x0081 单次读取sd卡历史数据6byte(年、月、日、时、节点号)
     */
    public static final int sdCardSingleHistory = 0x0081;
    /**
     * 0x0082 单次准确读取sd卡历史数据7byte(年、月、日、时、节点号、包序列号)
     */
    public static final int sdCardSingleAccuracyHistory = 0x0082;

    // ================================控制指令参数长度================================
    public static final int sdCardHistoryParamLen = 4; // 读取sd卡历史数据参数长度
    public static final int SetGatewayTimeParamLen = 6; // 网关授时参数长度
    public static final int sdCardSingleHistoryParamLen = 6; // 单次读取sd卡历史数据参数长度

    // ================================下行命令包0x09--指令反馈码定义[0x0A]==============
    /**
     * 路径不通 ff
     */
    public static final int PATHCLOSE = 0xFF;
    /**
     * 指令执行成功01
     */
    public static final int FEEDBACK_OK = 0x01;
    /**
     * 指令执行失败02
     */
    public static final int FEEDBACK_ERROR = 0x02;
    /**
     * 指令送达网关成功03
     */
    public static final int SENDGATEWAY_OK = 0x03;

    // ================================SD卡数据回补,操作执行步骤标识===================
    /**
     * 准备执行操作(命令成功送达网关后),在SD卡操作记录表中生成本次操作的起始时间戳
     */
    public static final int _S_sdCardStep_Ready = 1;
    /**
     * SD卡数据读取中，在SD卡操作记录表中更新本次操作的结束时间戳
     */
    public static final int _S_sdCardStep_Reading = 2;

    /**
     * SD卡数据读取结束，检查到此状态后等待1分钟，然后执行重新加工操作
     */
    public static final int _S_sdCardStep_End = 3;
    /**
     * 重新加工数据结束
     */
    public static final int _S_sdCardStep_ReMath = 4;

    // ================================传感器标识定义================================
    /**
     * 0x00A0 锐研智华 RY-G/W
     */
    public static final int sensorRygw = 0x00A0;
    /**
     * 0x00A1 锐研智华 RY-ZW
     */
    public static final int sensorRyzw = 0x00A1;
    /**
     * 0x00A2 Vaisala WXT520
     */
    public static final int sensorVaisala = 0x00A2;
    /**
     * 0xF9FF 兼容WiseBee-V3前的传感器标识
     */
    public static final int sensorComm13 = 0xF9FF;

    // ================================== 传感量定义========================
    /**
     * 湿度 ％ 32 HUM
     */
    public static final int _RH = 0x0020;
    /**
     * 温度 ℃ 33 TMT
     */
    public static final int _T = 0x0021;
    /**
     * 甲醛 ppm 34 HCHO
     */
    public static final int _HCHO = 0x0022;
    /**
     * 灰尘 mg/cm³ 35 DST
     */
    public static final int _DST = 0x0023;
    /**
     * 二氧化碳 ppm 36 CO2
     */
    public static final int _CO2 = 0x0024;
    /**
     * 硫化氢 ppm 37 H2S
     */
    public static final int _H2S = 0x0025;
    /**
     * 臭氧 ppm 38 O3
     */
    public static final int _O3 = 0x0026;
    /**
     * 二氧化氮 ppm 39 NO2
     */
    public static final int _NO2 = 0x0027;
    /**
     * 加速度 g 40 ACC
     */
    public static final int _ACC = 0x0028;
    /**
     * 光照 lux 41 室内/室外
     */
    public static final int _LUX = 0x0029;
    /**
     * 紫外 uw/cm2 42 室内/davis
     */
    public static final int _UV = 0x002a;
    /**
     * 露点 ℃ 43 ab公式
     */
    public static final int _TD = 0x002b;
    /**
     * 土壤温度 ℃ 44 pt100
     */
    public static final int _SoilTemperature = 0x002c;
    /**
     * 土壤含水率 ％ 45 SHUM
     */
    public static final int _SHUM = 0x002d;
    /**
     * VOC 46 ppm
     */
    public static final int _VOC = 0x002e;
    /**
     * 降雨量 mm 47 RB
     */
    public static final int _Rainfall = 0x002f;
    /**
     * 风向 ° 48 WDD
     */
    public static final int _WDD = 0x0030;
    /**
     * 风速 m/s 49 WDP
     */
    public static final int _Wind_Velocity = 0x0031;
    /**
     * 风力 级 50 WDF 由风速推算，参照蒲福风力等级标准计算
     */
    public static final int _Wind_Force = 0x0032;
    /**
     * 导线温度 ℃ 51 LTMT
     */
    public static final int _LTMT = 0x0033;
    /**
     * 拉力 KN 52 TSN
     */
    public static final int _TSN = 0x0034;
    /**
     * 绝缘子泄露电流(faulty insulator) μa 53 FIT
     */
    public static final int _FIT = 0x0035;
    /**
     * 摆角(横向) ° 54 SWD
     */
    public static final int _SWD = 0x0036;
    /**
     * 线上电流 a 55 AOL
     */
    public static final int _AOL = 0x0037;
    /**
     * 水温 ℃ 56 WT
     */
    public static final int _WT = 0x0038;
    /**
     * PH值 ~ 57 PH
     */
    public static final int _PH = 0x0039;
    /**
     * 容氧 mg/L 58 DO
     */
    public static final int _DO = 0x003a;
    /**
     * 摆角(纵向) ° 59 SWDH
     */
    public static final int _SWDH = 0x003b;
    /**
     * 表面温度 ℃ 60 BTMT
     */
    public static final int _FaceTemperature = 0x003c;
    /**
     * 水温
     */
    public static final int WATER_TEMPERATURE = 0x0038;
    /**
     * 大气压强 hPa 61 ATA
     */
    public static final int _AtmosphericPressure = 0x003d;
    /**
     * 电导率 ds/m 62 COND
     */
    public static final int _COND = 0x003e;
    /**
     * 降雨强度 mm/h 63 RRB
     */
    public static final int _RRB = 0x003f;
    /**
     * 微风风向 ° 65 MWDD
     */
    public static final int _MWDD = 0x0041;
    /**
     * 微风风速 m/s 66 MWDP
     */
    public static final int _MWDP = 0x0042;
    /**
     * 二氧化硫 ppm 67 SO2
     */
    public static final int _SO2 = 0x0043;
    /**
     * 5TE土壤温度 ℃ 68 STMT 公式按值划分
     */
    public static final int _STMT5 = 0x0044;
    /**
     * 5TE土壤含水率 ％ 69 SHUM 四参数
     */
    public static final int _SHUM5 = 0x0045;
    /**
     * 5TE电导率 ds/m 70 COND 公式按值划分
     */
    public static final int _COND5 = 0x0046;
    /**
     * 距离 mm 71 USD
     */
    public static final int _USD = 0x0047;
    /**
     * 辐射度 w/m² 72 RM
     */
    public static final int _RM = 0x0048;
    /**
     * EC值 ms/m 73 EC
     */
    public static final int _EC = 0x0049;
    /**
     * 雨水温度 ℃ 74 RTMT
     */
    public static final int _RTMT = 0x004a;
    /**
     * 液面高度 mm 75 PWL
     */
    public static final int _PWL = 0x004b;
    /**
     * X方向裂隙 mm 76 SGRX
     */
    public static final int _SGRX = 0x004c;
    /**
     * Y方向裂隙 mm 77 SGRY
     */
    public static final int _SGRY = 0x004d;
    /**
     * Z方向裂隙 mm 78 SGRZ
     */
    public static final int _SGRZ = 0x004e;
    /**
     * 位移量 mm 79 LVDT
     */
    public static final int _LVDT = 0x004f;
    /**
     * 蒸发量 mm 80 EVAP
     */
    public static final int _EVAP = 0x0050;
    /**
     * 液位增量 mm 81 DR
     */
    public static final int _DR = 0x0051;
    /**
     * 液位 mm 82 LEVEL
     */
    public static final int _LEVEL = 0x0052;

    /**
     * 有机污染物
     */
    public static final int _ORGANIC_POL = 0x0C00;

    /**
     * 无机污染物
     */
    public static final int _INORGANIC_POL = 0x0C01;

    /**
     * 含硫污染物
     */
    public static final int _SULFUROUS_POL = 0x0C02;

    /**
     * 有机污染物差值
     */
    public static final int _ORGANIC_POL_DIF = 0x0C03;

    /**
     * 无机污染物差值
     */
    public static final int _INORGANIC_POL_DIF = 0x0C04;

    /**
     * 含硫污染物差值
     */
    public static final int _SULFUROUS_POL_DIF = 0x0C05;

    /**
     * 水速
     */
    public static final int _PULSE = 0x005A;

    /**
     * 水位
     */
    public static final int _WATER_LEVEL = 0x0060;

    /**
     * 水流量
     */
    public static final int _WATER_FLOW = 0x0061;
    // ------------------------------------------------

    /**
     * 冰雹量 64
     */
    public static final int Hail = 0x0040;
    /**
     * 冰雹累计时间 65
     */
    public static final int HailTime = 65;
    /**
     * 冰雹强度 66
     */
    public static final int HailIntensity = 66;

    /**
     * 不区分传感，用于获取所有监测点终端
     */
    public static final int _NoneSensor = 0x0000;

    /**
     * 异常标识
     */
    public static final int ANORMALY = -1;

    /**
     * 正常标识
     */
    public static final int NORMALY = 0;

    /**
     * 低电标识
     */
    public static final int LowVoltageFlag = 1;

    /**
     * 掉电标识
     */
    public static final int NoVoltageFlag = 2;

    /**
     * 默认的电压值
     */
    public static final int DefaultVolValue = -1;

    /**
     * 默认上传状态（未上传）
     */
    public static final int uploadState = 0;

    /**
     * 经度
     */
    public static final int LONGITUDE = 12287;

    /**
     * 纬度
     */
    public static final int LATITUDE = 12286;


}
