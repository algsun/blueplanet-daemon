package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessbean.Formula;
import com.microwise.msp.hardware.businessbean.Sensor;
import com.microwise.msp.hardware.businessservice.FormulaService;
import com.microwise.msp.hardware.service.SensorService;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaohui
 * @date 13-10-23 20:28
 */
@Component
@Scope("prototype")
@Route("/struts")
public class SensorsController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private FormulaService formulaService;

    //output
    private List<Sensor> sensors;

    private TemplateHashModel statics = BeansWrapper.getDefaultInstance().getStaticModels();

    @Route("/sensors")
    public String all() {
        sensors = sensorService.findAll();
        return Results.ftl("/pages/sensors");
    }

    //input
    private int sensorId;
    //output
    private Sensor sensor;
    private Formula formula;

    @Route("/sensors/{sensorId}")
    public String detail() {
        sensor = sensorService.findById(sensorId);
        formula = formulaService.findBySensorId(sensorId);
        return Results.ftl("/pages/sensors-show");
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public TemplateHashModel getStatics() {
        return statics;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }
}
