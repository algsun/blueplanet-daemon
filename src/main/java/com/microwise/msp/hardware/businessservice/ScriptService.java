package com.microwise.msp.hardware.businessservice;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.Reader;
import java.util.Map;

/**
 * js脚本引擎类
 *
 * @author heming
 */
public class ScriptService {

    private static Logger log = LoggerFactory.getLogger(ScriptService.class);

    private static ScriptService instance = new ScriptService();

    private ScriptEngine engine;

    /**
     * 私有构造函数
     *
     * @since 2012-03-26
     */
    private ScriptService() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();

            Reader reader = Resources.asCharSource(Resources.getResource("sensor.js"), Charsets.UTF_8).openStream();
            engine = manager.getEngineByName("javascript");
            engine.eval(reader);
            reader.close();

        } catch (Exception e) {
            log.error("脚本加载失败", e);
        }
    }

    /**
     * 获取单类实例
     *
     * @return
     */
    public static ScriptService getScriptService() {
        return instance;
    }

    /**
     * 加载脚本进行判断值是否为低电
     *
     * @param value 原始值
     * @return
     */
    public boolean isLowVoltage(int deviceType, float value) {
        float v_high = 10.5F;
        float v_middle = 8F;
        float v_low = 3.5F;
        float v_low_gateway = 3.6F;
        if (deviceType == 7) {
            if ((value < v_high && value >= v_middle) || value < v_low_gateway) {
                return true;
            }
        } else {
            if ((value < v_high && value >= v_middle) || value < v_low) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据监测指标和公式系数计算结果
     *
     * @param sensorId
     * @param originValue
     * @param params
     * @return
     */
    public Double doMath(int sensorId, Double originValue, Map<String, Double> params) {
        try {
            Boolean isGeneral = (Boolean) callFunc("isGeneral", sensorId);
            if (isGeneral) {
                return doMath("generalCompute", originValue, params);
            } else {
                String funcName = "get_" + sensorId;
                return doMath(funcName, originValue, params);
            }
        } catch (ScriptException e) {
            log.error("计算监测指标", e);
        } catch (NoSuchMethodException e) {
            log.error("计算监测指标", e);
        }

        return null;
    }

    /**
     * 计算
     *
     * @param funcName
     * @param params
     * @return
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public Double doMath(String funcName, Double originValue, Map<String, Double> params) throws ScriptException, NoSuchMethodException {
        Object result = callFunc(funcName, originValue, params);
        if (result == null) {
            return null;
        } else {
            return (Double) result;
        }
    }


    /**
     * 调用函数并返回函数结果。如果无，可能返回 null 或者 Double.NaN
     *
     * @param functionName
     * @param args
     * @return
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public Object callFunc(String functionName, Object... args) throws ScriptException, NoSuchMethodException {
        Invocable inv = (Invocable) engine;
        return inv.invokeFunction(functionName, args);
    }

}
