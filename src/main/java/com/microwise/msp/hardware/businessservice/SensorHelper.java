package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.businessbean.*;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.handler.formula.Formulas;
import com.microwise.msp.util.Maths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监测指标计算帮助类
 *
 * @author bastengao
 * @date 14-3-5 下午5:05
 */
@Component
@Scope("prototype")
public class SensorHelper {

    @Autowired
    private AppCache appCache;

    public SensorPhysicalBean doMathSensor(int sensorId, CustomFormula customFormula, float originValue) {
        SensorPhysicalBean sensorBean = new SensorPhysicalBean(sensorId, originValue);

        Sensor sensor = appCache.loadSensor(sensorId);
        if (sensor == null) {
            return sensorBean;
        }

        this.doMathSensor(sensorBean, customFormula, null);
        double d = Maths.roundToDouble(sensorBean.getSensor_Value(), sensor.getPrecision());
        sensorBean.setSensor_Value(d);

        return sensorBean;
    }


    /**
     * 计算检测指标值
     *
     * @param sensorBean
     * @param customFormula 如果无自定义公式，传 null
     */
    public void doMathSensor(SensorPhysicalBean sensorBean, CustomFormula customFormula, List<FloatSensor> floatSensors) {
        int sensorId = sensorBean.sensorPhysical_id; // 传感标识
        double srcValue = sensorBean.sensor_Value; // 获取计算所需采样值

        Formula defaultFormula = appCache.loadFormula(sensorId);
        if (defaultFormula == null) {
            sensorBean.setSensor_State(Defines._Sensor_State_Err);
            return;
        }

        Formula formula = defaultFormula;
        Map<String, String> formulaParams = defaultFormula.getFormulaParams();

        // 如果有自定义使用自定义参数
        if (customFormula != null && customFormula.getFormula() != null) {
            formula = customFormula.getFormula();
        }
        if (customFormula != null && customFormula.getFormulaParams() != null) {
            formulaParams = customFormula.getFormulaParams();
        }
        // 如果有符号(两个字节)
//        if (formula.getSignType() == Formula.SIGN_TYPE_YES) {
//            if (srcValue >= 32768) {
//                srcValue = srcValue - 65536;
//            }
//        }
        // x 超范围
        if (formula.isXMinEnable() && srcValue < formula.getMinX()) {
            sensorBean.setSensor_State(Defines._Sensor_State_Err);
            return;
        }
        if (formula.isXMaxEnable() && srcValue > formula.getMaxX()) {
            sensorBean.setSensor_State(Defines._Sensor_State_Err);
            return;
        }

        // 计算
        Double result = Formulas.compute(srcValue, formula, formulaParams);

        // 计算异常
        if (result == null) {
            sensorBean.setSensor_State(Defines._Sensor_State_Err);
            sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_COMPUTE);
            return;
        }

        sensorBean.sensor_Value = result;

        FloatSensor floatSensor = null;
        for (FloatSensor fs : floatSensors) {
            if (sensorId == fs.getSensorId()) {
                floatSensor = fs;
                break;
            }
        }
        if (floatSensor != null) {
            // y 超范围
            if (formula.isYMaxEnable()) {
                double maxValue = formula.getMaxY();
                double maxValueFloat = maxValue + floatSensor.getMax_up_float();
                if (maxValue < result && result <= maxValueFloat) {
                    sensorBean.sensor_Value = maxValue;
                    return;
                }

                if (maxValueFloat < result) {
                    sensorBean.setSensor_State(Defines._Sensor_State_Err);
                    sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_OUT_RANGE);
                    return;
                }
            }

            if (formula.isYMinEnable()) {
                if (formula.getMinY() == 0) {
                    double float_min_y = 0 - Math.abs(floatSensor.getMin_down_float());
                    double zero_float_min_y = floatSensor.getMin_up_float();
                    if (float_min_y <= result && result < 0) {
                        sensorBean.sensor_Value = 0;
                        return;
                    }

                    if (result < float_min_y) {
                        sensorBean.setSensor_State(Defines._Sensor_State_Err);
                        sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_OUT_RANGE);
                        return;
                    }

                    if (0 < result && result <= zero_float_min_y) {
                        sensorBean.sensor_Value = 0;
                        return;
                    }
                } else {
                    double minYMinusFloat_min_y = formula.getMinY() - Math.abs(floatSensor.getMin_down_float());
                    double minY = formula.getMinY();
                    if (minY > result && result >= minYMinusFloat_min_y) {
                        sensorBean.sensor_Value = minY;
                        return;
                    }
                    if (minYMinusFloat_min_y > result) {
                        sensorBean.setSensor_State(Defines._Sensor_State_Err);
                        sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_OUT_RANGE);
                        return;
                    }
                }
            }
        } else {
            // y 超范围
            if (formula.isYMinEnable() && result < formula.getMinY()) {
                sensorBean.setSensor_State(Defines._Sensor_State_Err);
                sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_OUT_RANGE);
                return;
            }

            if (formula.isYMaxEnable() && result > formula.getMaxY()) {
                sensorBean.setSensor_State(Defines._Sensor_State_Err);
                sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_OUT_RANGE);
                return;
            }

            //y 超出范围显示限定值
            if (formula.isDisplayYMaxAndMin() && result < formula.getMinY()) {
                sensorBean.sensor_Value = formula.getMinY();
                return;
            }

            if (formula.isDisplayYMaxAndMin() && result > formula.getMaxY()) {
                sensorBean.sensor_Value = formula.getMaxY();
                return;
            }
        }
    }

    public void doCheckSensor(SensorPhysicalBean sensorBean, String deviceId) {
        CustomFormula customFormula = appCache.loadCustomFormula(deviceId).get(sensorBean.getSensorPhysical_id());
        Formula defaultFormula = appCache.loadFormula(sensorBean.sensorPhysical_id);
        if (defaultFormula == null) {
            sensorBean.setSensor_State(Defines._Sensor_State_Err);
            return;
        }

        Formula formula = defaultFormula;
        // 如果有自定义使用自定义参数
        if (customFormula != null && customFormula.getFormula() != null) {
            formula = customFormula.getFormula();
        }
        if (formula.isYMinEnable() && sensorBean.getSensor_Value() < formula.getMinY()) {
            sensorBean.setSensor_State(Defines._Sensor_State_Err);
            sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_OUT_RANGE);
            return;
        }
        if (formula.isYMaxEnable() && sensorBean.getSensor_Value() > formula.getMaxY()) {
            sensorBean.setSensor_State(Defines._Sensor_State_Err);
            sensorBean.setErrorType(SensorPhysicalBean.ERROR_TYPE_OUT_RANGE);
            return;
        }
        //y 超出范围显示限定值
        if (formula.isDisplayYMaxAndMin() && sensorBean.getSensor_Value() < formula.getMinY()) {
            sensorBean.setSensor_Value(formula.getMinY());
            return;
        }

        if (formula.isDisplayYMaxAndMin() && sensorBean.getSensor_Value() > formula.getMaxY()) {
            sensorBean.setSensor_Value(formula.getMaxY());
            return;
        }
    }

    /**
     * 处理有符号数据
     *
     * @param sensorId    传感编号
     * @param sensorValue 传感值
     * @return
     */
    public double handleSignedData(int sensorId, double sensorValue) {
        Formula defaultFormula = appCache.loadFormula(sensorId);
        if (defaultFormula == null) {
            return sensorValue;
        }
        // 如果有符号(两个字节)
        if (defaultFormula.getSignType() == Formula.SIGN_TYPE_YES) {
            if (sensorValue >= 32768) {
                sensorValue = sensorValue - 65536;
            }
        }
        return sensorValue;
    }
}
