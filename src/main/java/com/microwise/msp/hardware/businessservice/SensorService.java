package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.businessbean.CustomFormula;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.LocationSensorDao;
import com.microwise.msp.hardware.dao.QCMDao;
import com.microwise.msp.hardware.vo.LocationSensor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 传感 业务处理
 *
 * 包括衍生新传感及特殊传感量计算等
 * </pre>
 *
 * @author heming
 * @since 2013-1-9
 */
public class SensorService extends DeviceService {

    private static Logger log = LoggerFactory.getLogger(SensorService.class);

    @Autowired
    private SensorHelper sensorHelper;

    @Autowired
    private AppCache appCache;

    @Autowired
    private LocationSensorDao locationSensorDao;

    @Autowired
    private QCMDao qcmDao;

    public SensorService() {
        super();
    }

    /**
     * 衍生露点：温度+湿度--露点
     */
    private SensorPhysicalBean deriveTD(Map<Integer, SensorPhysicalBean> sensorDatas) {
        SensorPhysicalBean sensorBean = new SensorPhysicalBean();
        sensorBean.sensorPhysical_id = Defines._TD; // 露点
        if (getSensorState(sensorDatas, Defines._RH) != 0 && getSensorState(sensorDatas, Defines._T) != 0) {
            sensorBean.sensor_State = Defines._Sensor_State_OK;
            sensorBean.sensor_Value = 0;
        } else {
            sensorBean.sensor_State = Defines._Sensor_State_Err;
            sensorBean.setValueStr(String.valueOf(sensorBean.getSensor_Value()));
        }
        return sensorBean;
    }

    /**
     * <pre>
     * 衍生风力：风速--风力
     * 存在风速时添加风力（用于参数初始化）
     * </pre>
     *
     * @return
     */
    private SensorPhysicalBean deriveWindForce(Map<Integer, SensorPhysicalBean> sensorDatas) {
        SensorPhysicalBean sensorBean = new SensorPhysicalBean();
        sensorBean.sensorPhysical_id = Defines._Wind_Force;
        if (getSensorState(sensorDatas, Defines._Wind_Velocity) != Defines._Sensor_State_Err) { // 非采样失败
            sensorBean.sensor_State = Defines._Sensor_State_OK;
            sensorBean.sensor_Value = 0;
        } else { // 采样失败
            sensorBean.sensor_State = Defines._Sensor_State_Err;
            sensorBean.setValueStr(String.valueOf(sensorBean.getSensor_Value()));
        }
        return sensorBean;
    }

    private SensorPhysicalBean deriveEvap(Map<Integer, SensorPhysicalBean> sensorDatas) {
        SensorPhysicalBean sensorBean = new SensorPhysicalBean();
        sensorBean.sensorPhysical_id = Defines._EVAP;
        if (getSensorState(sensorDatas, Defines._PWL) != Defines._Sensor_State_Err) { // 非采样失败
            sensorBean.sensor_State = Defines._Sensor_State_OK;
            sensorBean.sensor_Value = 0;
        } else { // 采样失败
            sensorBean.sensor_State = Defines._Sensor_State_Err;
            sensorBean.setValueStr(String.valueOf(sensorBean.getSensor_Value()));
        }
        return sensorBean;
    }

    /**
     * <pre>
     * 计算露点:
     * var ln = log(RH/100);
     * var x = (this.TD_1*this.T)/(this.TD_2+this.T) +this.LN;
     * var td = (this.TD_2*x)/(this.TD_1-x);
     * </pre>
     *
     * @return
     */
    private double mathTd(Map<Integer, SensorPhysicalBean> sensorDatas, SensorPhysicalBean sensorBean) {
        if (!isSensorStateOk(sensorDatas, Defines._RH, Defines._T)) {
            sensorBean.sensor_State = Defines._Sensor_State_Err;
            return 0;
        }


        double RH = sensorDatas.get(Defines._RH).sensor_Value;
        double T = sensorDatas.get(Defines._T).sensor_Value;
        double TD_1 = 17.27;
        double TD_2 = 237.7;
        // 第一步计算
        double LN = 0d;
        if (RH == 0) { // 2010年11月22日 宋涛 修改 防止湿度为0
            LN = 0;
        } else {
            LN = Math.log(RH / 100);
        }
        // 第二步计算
        double x = (TD_1 * T) / (TD_2 + T) + LN;
        // 第三步计算
        double td = (TD_2 * x) / (TD_1 - x);
        // 过滤掉负数
        if (td < 0) {
            td = 0;
        } else if (Double.isNaN(td)) {
            sensorBean.sensor_State = Defines._Sensor_State_Err;
            sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_COMPUTE);
            td = 0;
        }
        return td;
    }

    /**
     * <pre>
     * 风速--风力
     * 计算风力
     * </pre>
     *
     * @return
     */
    private double mathWindForce(Map<Integer, SensorPhysicalBean> sensorDatas, SensorPhysicalBean sensorBean) {
        if (!isSensorStateOk(sensorDatas, Defines._Wind_Velocity)) {
            sensorBean.sensor_State = Defines._Sensor_State_Err;
            return 0;
        }

        double windforce;
        double speed = sensorDatas.get(Defines._Wind_Velocity).sensor_Value;
        if (speed <= 0.2) {
            windforce = 0;
        } else if (speed > 0.2 && speed <= 1.5) {
            windforce = 1;
        } else if (speed > 1.5 && speed <= 3.3) {
            windforce = 2;
        } else if (speed > 3.3 && speed <= 5.4) {
            windforce = 3;
        } else if (speed > 5.4 && speed <= 7.9) {
            windforce = 4;
        } else if (speed > 7.9 && speed <= 10.7) {
            windforce = 5;
        } else if (speed > 10.7 && speed <= 13.8) {
            windforce = 6;
        } else if (speed > 13.8 && speed <= 17.1) {
            windforce = 7;
        } else if (speed > 17.1 && speed <= 20.7) {
            windforce = 8;
        } else if (speed > 20.7 && speed <= 24.4) {
            windforce = 9;
        } else if (speed > 24.4 && speed <= 28.4) {
            windforce = 10;
        } else if (speed > 28.4 && speed <= 32.6) {
            windforce = 11;
        } else {
            windforce = 12;
        }
        return windforce;
    }

    /**
     * <pre>
     * 计算降雨量[_Rainfall]: 本次累计雨量-上次累计雨量
     * </pre>
     */
    private double mathRainfall(DeviceBean deviceBean, Map<Integer, SensorPhysicalBean> sensorDatas) {
        if (isHistory(deviceBean)) {//回补数据已处理过累积值，因此不做数据差值重复计算。
            return 0;
        }

        double diff; // 本次降雨量
        double beforeV; // 累计雨量
        double currentV = sensorDatas.get(Defines._Rainfall).sensor_Value;

        DeviceBean cachedDeviceBean = appCache.loadDevice(deviceBean.deviceid);
        beforeV = cachedDeviceBean.cumulativeRainfall;

        // 2009年11月26日修改 宋涛 硬件降水量采样可能失败,软件做处理
        if (currentV == 0) {
            currentV = beforeV;
        }
        // 初次加载节点降雨量信息,用当前累计值替换-1
        if (beforeV == -1) {
            beforeV = currentV;
        }
        if (currentV >= beforeV) {
            diff = currentV - beforeV;
        } else {
            diff = currentV;
        }
        cachedDeviceBean.cumulativeRainfall = currentV;
        return diff;
    }

    /**
     * <pre>
     * 计算qcm 本次-上次
     * </pre>
     */
    private double mathQCM(DeviceBean deviceBean, Map<Integer, SensorPhysicalBean> sensorDatas, int sensorFlag) {
//        if (isHistory(deviceBean)) {//回补数据已处理过累积值，因此不做数据差值重复计算。
//            return 0;
//        }

        //本次qcm
        int currentQcmState = deviceBean.qcm;

        //缓存qcm
        int cacheQcmState = 0;

        //本次差值
        double diff = 0;

        //缓存中的数据
        double last_timeV = 0;

        //当前监测指标值
        double currentV = 0;

        //从系统变量中得到设备缓存
        DeviceBean cachedDeviceState = SysConfig.qcmStates.get(deviceBean.deviceid);

        if (!Strings.isEmpty(deviceBean.locationId)) {
            if (cachedDeviceState.last_timeOrganicPol == 0) {
                LocationSensor locationSensor = locationSensorDao.findLocationSensorData(deviceBean.locationId, Defines._ORGANIC_POL);
                if (locationSensor != null) {
                    cachedDeviceState.last_timeOrganicPol = locationSensor.getSensorPhysicalValue();
                }
            }
            if (cachedDeviceState.last_timeInorganicPol == 0) {
                LocationSensor locationSensor = locationSensorDao.findLocationSensorData(deviceBean.locationId, Defines._INORGANIC_POL);
                if (locationSensor != null) {
                    cachedDeviceState.last_timeInorganicPol = locationSensor.getSensorPhysicalValue();
                }
            }
            if (cachedDeviceState.last_timeSulfurousPol == 0) {
                LocationSensor locationSensor = locationSensorDao.findLocationSensorData(deviceBean.locationId, Defines._SULFUROUS_POL);
                if (locationSensor != null) {
                    cachedDeviceState.last_timeSulfurousPol = locationSensor.getSensorPhysicalValue();
                }
            }
        }


        if (sensorFlag == Defines._ORGANIC_POL_DIF) {
            currentV = sensorDatas.get(Defines._ORGANIC_POL).sensor_Value; // 当前数据
            cacheQcmState = cachedDeviceState.qcmState.get(Defines._ORGANIC_POL);//缓存上一次的qcm复位标识
            last_timeV = cachedDeviceState.last_timeOrganicPol;//缓存上一次qcm数据
        } else if (sensorFlag == Defines._INORGANIC_POL_DIF) {
            currentV = sensorDatas.get(Defines._INORGANIC_POL).sensor_Value; // 当前数据
            cacheQcmState = cachedDeviceState.qcmState.get(Defines._INORGANIC_POL);//缓存上一次的qcm复位标识
            last_timeV = cachedDeviceState.last_timeInorganicPol;//缓存上一次qcm数据
        } else if (sensorFlag == Defines._SULFUROUS_POL_DIF) {
            currentV = sensorDatas.get(Defines._SULFUROUS_POL).sensor_Value; // 当前数据
            cacheQcmState = cachedDeviceState.qcmState.get(Defines._SULFUROUS_POL);//缓存上一次的qcm复位标识
            last_timeV = cachedDeviceState.last_timeSulfurousPol;//缓存上一次qcm数据
        }

        //正常采样
        if (currentQcmState == 0) {
            //缓存不为0
            diff = getDiffValue(last_timeV, currentV);
        }

        //换探头
        if (currentQcmState == 1) {
            //如果当前数据包qcm复位标识和缓存的qcm标识不一致，那么说么是从0--1的过程（第一包）
            //如果是连续的1--1那么重新计算；从1--0缓存不清零继续计算。
            if (cacheQcmState == 0) {
                diff = 0;
                String locationId = deviceBean.locationId;
                Date timeStamp = deviceBean.timeStamp;
                //暂时屏蔽qcm评估功能
                if (!Strings.isEmpty(locationId)) {
                    boolean flag = qcmDao.isExist(locationId, timeStamp);
                    if (!flag) {
                        //第一次更换探头将位置点id，时间存入数据库中，仅供qcm评估等级时使用
                        qcmDao.insertReplaceSensor(locationId, timeStamp);
                    }
                }
            } else {
                diff = getDiffValue(last_timeV, currentV);
            }
        }

        //更新缓存
        updateSysconfigCache(deviceBean, sensorFlag, currentQcmState, currentV, cachedDeviceState);
        return diff;
    }

    /**
     * 更新系统缓存
     *
     * @param deviceBean       设备bean
     * @param sensorFlag       穿感量标识
     * @param currentQcmState  当前qcm状态
     * @param currentV         当前监测指标值
     * @param cachedDeviceBean 系统缓存的设备bean
     */
    private void updateSysconfigCache(DeviceBean deviceBean, int sensorFlag, int currentQcmState, double currentV, DeviceBean cachedDeviceBean) {
        String locationId = deviceBean.locationId;
        //设备没有绑定位置点那么不更新缓存
        if (locationId != null) {
            Map<Integer, Integer> map = cachedDeviceBean.qcmState;
            //更新缓存
            if (Defines._ORGANIC_POL_DIF == sensorFlag) {
                cachedDeviceBean.last_timeOrganicPol = currentV;
                map.put(Defines._ORGANIC_POL, currentQcmState);
            } else if (Defines._INORGANIC_POL_DIF == sensorFlag) {
                map.put(Defines._INORGANIC_POL, currentQcmState);
                cachedDeviceBean.last_timeInorganicPol = currentV;
            } else if (Defines._SULFUROUS_POL_DIF == sensorFlag) {
                map.put(Defines._SULFUROUS_POL, currentQcmState);
                cachedDeviceBean.last_timeSulfurousPol = currentV;
            }
            cachedDeviceBean.qcmState = map;
            //写入到系统缓存中
            SysConfig.qcmStates.put(deviceBean.deviceid, cachedDeviceBean);
        }
    }

    //计算差值
    private double getDiffValue(double last_timeV, double currentV) {
        double diff;
        if (last_timeV != 0) {
            DecimalFormat df = new DecimalFormat("#.000");
            currentV = Double.parseDouble(df.format(currentV));
            last_timeV = Double.parseDouble(df.format(last_timeV));
            diff = currentV - last_timeV;
            BigDecimal b = new BigDecimal(diff);
            diff = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            //第一次上数据,缓存为0,则将当前值赋值给缓存
            diff = 0;
        }
        return diff;
    }

    /**
     * 计算蒸发量
     */
    private double mathEvap(DeviceBean deviceBean, Map<Integer, SensorPhysicalBean> sensorData) {
        if (isHistory(deviceBean)) {//回补数据已处理过累积值，因此不做数据差值重复计算。
            return 0;
        }

        double diff = 0d;        //蒸发量
        double currentEv = sensorData.get(Defines._PWL).sensor_Value;   //当前液面高度值
        DeviceBean cachedDeviceBean = appCache.loadDevice(deviceBean.deviceid);
        double beforeEv = cachedDeviceBean.cumulativePwl;     //之前液面高度值

        // 初次加载节点蒸发量信息,用当前累计值替换-1
        if (beforeEv == -1) {
            beforeEv = currentEv;
        }
        diff = beforeEv - currentEv;
        if (diff < 0) {
            diff = 0;
        }
        cachedDeviceBean.cumulativePwl = currentEv;
        return diff;
    }

    /**
     * <pre>
     * 计算PT100设备 [土壤温度、表面温度]
     *
     * 传感器版本号为1，土壤温度依照PT100查表法计算
     * 2010年4月19日 硬件部要求PT100传感器更换计算方式。
     * 数据包中采样得到的原始值，经过计算得到电阻值，然后根据电阻值到对应分度表中查询对应温度。
     * 目前土壤温度采用PT100传感器，其标识位为44（0x2c）。
     *
     * 数据包中4-5字节表示版本号，现与硬件部门相关人员协商后定义1号版本的土壤温度为PT100，
     * 以后如果使用其他种类传感器进行土壤温度测量需更换版本标识。
     * </pre>
     * <p/>
     * TODO 计算搞在内存里 @gaohui 2013-08-09
     *
     * @param sensorid
     * @return
     * @deprecated 建议使用 mathPt100Device(double) @gaohui 2013-08-18
     */
    private double mathPt100Device(int sensorid, Map<Integer, SensorPhysicalBean> sensorDatas) {
        double ohm = sensorDatas.get(sensorid).sensor_Value;
        return dao.pt100Mapping(ohm);
    }

    /**
     * 计算水流量
     */
    private double mathWaterFlow(DeviceBean deviceBean, Map<Integer, SensorPhysicalBean> sensorDatas, SensorPhysicalBean sensorBean) {
        if (!isSensorStateOk(sensorDatas, Defines._PULSE, Defines._WATER_LEVEL)) {
            sensorBean.sensor_State = Defines._Sensor_State_Err;
            return 0;
        }

        //水速 将水速 m/s 转换成 m/h,每小时流速
        double PULSE = sensorDatas.get(Defines._PULSE).sensor_Value * 3600;
        //水位 将水位 mm  转换成m,水位单位为米
        double WATER_LEVEL = sensorDatas.get(Defines._WATER_LEVEL).sensor_Value / 1000.0;

        //水流量 = 宽度 * 水速 * 水位
        double temp_value = PULSE * WATER_LEVEL;

        Map<Integer, CustomFormula> customFormulas = appCache.loadCustomFormula(deviceBean.deviceid);
        // 监测指标自定义公式系数
        CustomFormula customFormula = customFormulas.get(sensorBean.getSensorPhysical_id());
        sensorBean.sensor_Value = temp_value;
        sensorHelper.doMathSensor(sensorBean, customFormula,null);

        if (Double.isNaN(sensorBean.sensor_Value)) {
            sensorBean.sensor_State = Defines._Sensor_State_Err;
            sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_COMPUTE);
            sensorBean.sensor_Value = 0;
        }
        return sensorBean.sensor_Value;
    }

    /**
     * TODO 计算搞在内存里 @gaohui 2013-08-09
     *
     * @param sensorValue
     * @return
     */
    private double mathPt100Device(double sensorValue) {
        return dao.pt100Mapping(sensorValue);
    }

    /**
     * <pre>
     * 对需要进行特殊处理的传感量进行自定义计算
     * 更新：2012年4月18日 宋涛
     * 添加土壤温度和表面温度的break语句
     * </pre>
     *
     * @param deviceBean 设备
     * @return
     */
    public void defineSensorMath(DeviceBean deviceBean, SensorPhysicalBean sensorBean, Map<Integer, SensorPhysicalBean> sensorDatas) {
        switch (sensorBean.sensorPhysical_id) {
            case Defines._TD: // 计算露点
                // TODO 判断 state 多余 @gaohui 2013-10-28
                if (isSensorStateOk(sensorDatas, Defines._TD)) {
                    sensorBean.valueStr = "0";
                    sensorBean.sensor_Value = mathTd(sensorDatas, sensorBean);
                }
                break;
            case Defines._Wind_Force: // 计算风力
                if (isSensorStateOk(sensorDatas, Defines._Wind_Force)) {
                    sensorBean.valueStr = "0";
                    sensorBean.sensor_Value = mathWindForce(sensorDatas, sensorBean);
                }
                break;
            case Defines._Rainfall: // 计算降雨量
                if (isSensorStateOk(sensorDatas, Defines._Rainfall)) {
                    sensorBean.sensor_Value = mathRainfall(deviceBean, sensorDatas);
                }
                break;
            case Defines._EVAP:  //计算蒸发量
                sensorBean.sensor_Value = mathEvap(deviceBean, sensorDatas);
                break;
            case Defines._SoilTemperature:
            case Defines._FaceTemperature:
            case Defines.WATER_TEMPERATURE:
                // PT100查表法计算
                sensorBean.sensor_Value = mathPt100Device(sensorBean.sensor_Value);
                break;
            case Defines._WATER_FLOW: //计算水流量
                sensorBean.sensor_Value = mathWaterFlow(deviceBean, sensorDatas, sensorBean);
                break;
        }
    }

    public void qcmSensorMath(DeviceBean deviceBean, SensorPhysicalBean sensorBean, Map<Integer, SensorPhysicalBean> sensorDatas) {
        //初始化缓存
        DeviceBean cachedDeviceBean = SysConfig.qcmStates.get(deviceBean.deviceid);
        if (cachedDeviceBean == null) {
            cachedDeviceBean = new DeviceBean();
            Map<Integer, Integer> qcmStateMaps = new HashMap<Integer, Integer>();
            qcmStateMaps.put(Defines._ORGANIC_POL, 0);
            qcmStateMaps.put(Defines._INORGANIC_POL, 0);
            qcmStateMaps.put(Defines._SULFUROUS_POL, 0);
            cachedDeviceBean.qcmState = qcmStateMaps;
            SysConfig.qcmStates.put(deviceBean.deviceid, cachedDeviceBean);
        }

        //计算qcm差值
        switch (sensorBean.sensorPhysical_id) {
            case Defines._ORGANIC_POL_DIF:
            case Defines._INORGANIC_POL_DIF:
            case Defines._SULFUROUS_POL_DIF:
                sensorBean.sensor_Value = mathQCM(deviceBean, sensorDatas, sensorBean.sensorPhysical_id);
                sensorBean.valueStr = String.valueOf(sensorBean.sensor_Value);
                break;
        }
    }

    /**
     * 判断监测指标是否正常
     *
     * @param sensorData
     * @param sensorPhysicalId
     * @return
     */
    private static boolean isSensorStateOk(Map<Integer, SensorPhysicalBean> sensorData, int sensorPhysicalId) {
        return sensorData.get(sensorPhysicalId).sensor_State == Defines._Sensor_State_OK;
    }

    /**
     * 判断监测指标是否正常
     *
     * @param sensorData
     * @param sensorPhysicalIds
     * @return
     */
    private static boolean isSensorStateOk(Map<Integer, SensorPhysicalBean> sensorData, int... sensorPhysicalIds) {
        for (int sensorId : sensorPhysicalIds) {
            if (!isSensorStateOk(sensorData, sensorId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取传感状态
     *
     * @param sensorPhysicalId 传感标识
     * @return
     */
    private int getSensorState(Map<Integer, SensorPhysicalBean> sensorData, int sensorPhysicalId) {
        return sensorData.get(sensorPhysicalId).sensor_State;
    }

    /**
     * <pre>
     * 衍生新传感:
     * 温度+湿度 -- 露点
     * 风速     -- 风力
     * </pre>
     *
     * @param baseSensor 原始传感信息
     * @return SensorPhysicalBean
     */
    public SensorPhysicalBean deriveSensor(Map<Integer, SensorPhysicalBean> baseSensor) {
        SensorPhysicalBean deriveSensor = new SensorPhysicalBean();
        if (!baseSensor.containsKey(Defines._TD)
                && baseSensor.containsKey(Defines._RH)
                && baseSensor.containsKey(Defines._T)) {
            // 衍生露点,存在温度,湿度时添加露点(用于参数初始化)
            deriveSensor = deriveTD(baseSensor);
        } else if (!baseSensor.containsKey(Defines._Wind_Force)
                && baseSensor.containsKey(Defines._Wind_Velocity)) {
            // 衍生风力
            deriveSensor = deriveWindForce(baseSensor);
        } else if (!baseSensor.containsKey(Defines._EVAP) && baseSensor.containsKey(Defines._PWL)) {
            // 衍生蒸发量
            deriveSensor = deriveEvap(baseSensor);
        } else if (!baseSensor.containsKey(Defines._ORGANIC_POL_DIF) && baseSensor.containsKey(Defines._ORGANIC_POL)) {
            //衍生有机污染物差值
            deriveSensor = deriveOPDif();
        } else if (!baseSensor.containsKey(Defines._INORGANIC_POL_DIF) && baseSensor.containsKey(Defines._INORGANIC_POL)) {
            //衍生无机污染物差值
            deriveSensor = deriveIPDif();
        } else if (!baseSensor.containsKey(Defines._SULFUROUS_POL_DIF) && baseSensor.containsKey(Defines._SULFUROUS_POL)) {
            //衍生含硫污染物差值
            deriveSensor = deriveSPDif();
        } else if (!baseSensor.containsKey(Defines._WATER_FLOW)
                && baseSensor.containsKey(Defines._PULSE)
                && baseSensor.containsKey(Defines._WATER_LEVEL)) {
            //衍生水流量,当有水速，水位时，添加水流量
            deriveSensor = deriveWaterFlow();
        }
        return deriveSensor;
    }

    private SensorPhysicalBean deriveOPDif() {
        SensorPhysicalBean sensorBean = new SensorPhysicalBean();
        sensorBean.sensorPhysical_id = Defines._ORGANIC_POL_DIF;
        sensorBean.sensor_Value = 0;
        sensorBean.sensor_State = 1;
        return sensorBean;
    }

    private SensorPhysicalBean deriveIPDif() {
        SensorPhysicalBean sensorBean = new SensorPhysicalBean();
        sensorBean.sensorPhysical_id = Defines._INORGANIC_POL_DIF;
        sensorBean.sensor_Value = 0;
        sensorBean.sensor_State = 1;
        return sensorBean;
    }

    private SensorPhysicalBean deriveSPDif() {
        SensorPhysicalBean sensorBean = new SensorPhysicalBean();
        sensorBean.sensorPhysical_id = Defines._SULFUROUS_POL_DIF;
        sensorBean.sensor_Value = 0;
        sensorBean.sensor_State = 1;
        return sensorBean;
    }

    private SensorPhysicalBean deriveWaterFlow() {
        SensorPhysicalBean sensorBean = new SensorPhysicalBean();
        sensorBean.sensorPhysical_id = Defines._WATER_FLOW;
        sensorBean.sensor_Value = 0;
        sensorBean.sensor_State = 1;
        return sensorBean;
    }
}
