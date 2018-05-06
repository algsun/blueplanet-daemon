package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessbean.Sensor;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.businessservice.SensorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 计算传感量
 *
 * @author gaohui
 * @date 13-11-6 17:14
 */
@Component
@Scope("prototype")
@Route("/struts")
public class SensorComputeController {
    @Autowired
    private SensorHelper sensorHelper;

    //input
    // 监测指标
    private int sensorId;
    // 原始值
    private float value;

    //output
    private Sensor sensor;
    private Map<String, Object> result;
    private SensorPhysicalBean sensorBean;

    @Route("/sensors/{sensorId}/compute")
    public String compute() {
        sensorBean = sensorHelper.doMathSensor(sensorId, null, value);
        return Results.json().done();
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public SensorPhysicalBean getSensorBean() {
        return sensorBean;
    }

    public void setSensorBean(SensorPhysicalBean sensorBean) {
        this.sensorBean = sensorBean;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
