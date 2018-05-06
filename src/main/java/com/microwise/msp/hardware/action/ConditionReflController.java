package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.businessbean.ConditionRefl;
import com.microwise.msp.hardware.businessservice.DcoOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 计算条件反射的原始值
 *
 * @author bastengao
 * @date 14-4-11 上午11:27
 */
@Component
@Scope("prototype")
@Route("/struts")
public class ConditionReflController {
    public static final Logger log = LoggerFactory.getLogger(ConditionReflController.class);

    @Autowired
    private DcoOperateService dcoOperateService;

    // input
    private String deviceId;
    private int sensorId;
    private double target;

    //output
    private boolean success;
    private int origin;
    private int originLeft;
    private int originRight;

    @Route("condition-refl/origin")
    public String computeOrigin(){
        try{
            ConditionRefl conditionRefl = dcoOperateService.computeConditionReflOrigin(deviceId, sensorId, target);
            origin = conditionRefl.getLow();
            originLeft = conditionRefl.getLowLeft();
            originRight = conditionRefl.getLowRight();
            success = true;
        } catch (Exception e){
            success = false;
            log.error("", e);
        }
        return Results.json().done();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getOriginLeft() {
        return originLeft;
    }

    public void setOriginLeft(int originLeft) {
        this.originLeft = originLeft;
    }

    public int getOriginRight() {
        return originRight;
    }

    public void setOriginRight(int originRight) {
        this.originRight = originRight;
    }
}
