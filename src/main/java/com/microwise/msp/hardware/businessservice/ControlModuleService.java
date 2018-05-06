package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.businessbean.ConditionRefl;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.SwitchDao;
import com.microwise.msp.hardware.service.SwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gaohui
 * @date 14-2-11 16:55
 */
@Component
@Scope("prototype")
@Transactional
public class ControlModuleService extends DeviceService {

    @Autowired
    private SwitchService switchService;

    @Autowired
    private SwitchDao switchDao;

    @Autowired
    private SensorHelper sensorHelper;

    @Override
    public boolean businessProcess(DeviceBean deviceBean) {
        boolean isHistoryData = isHistory(deviceBean);
        if (super.businessProcess(deviceBean)) {
            // 更新控制端口状态
            if (deviceBean.isSwitchExists()) {
                switchDao.saveSwitches(deviceBean);
                switchService.saveSwitchChange(deviceBean.deviceid, deviceBean.getSwitches(),deviceBean.isSwitchCondRefl());
            }

            saveOrUpdateConditionRefl(deviceBean);

            if(!isHistoryData){
                fireListener(deviceBean);
            }
        }

        return true;
    }

    /**
     * 保存或者更新条件反射参数
     *
     * @param deviceBean
     */
    private void saveOrUpdateConditionRefl(DeviceBean deviceBean) {
        if (!deviceBean.isConditionReflExists()) {
            return;
        }

        ConditionRefl newConditionRefl = deviceBean.getConditionRefl();

        String cacheKey = conditionReflKey(deviceBean.deviceid, newConditionRefl.getRoute());
        ConditionRefl currentConditionRefl = SysConfig.getInstance().getConditionReflCache().get(cacheKey);

        ConditionRefl conditionRefl = null;
        if (currentConditionRefl == null) {
            conditionRefl = deviceDao.findConditionRefl(deviceBean.deviceid, newConditionRefl.getRoute());
        } else {
            conditionRefl = currentConditionRefl;
        }


        // 计算阈值的结果值，用于界面显示
        int sensorId = newConditionRefl.getSensorId();
        if(sensorId != 0){
            if(isLowUsed(newConditionRefl.getAction())){
                SensorPhysicalBean lowResult = sensorHelper.doMathSensor(sensorId, null, newConditionRefl.getLow());
                newConditionRefl.setLowTarget(lowResult.getSensor_Value());
            }

            if(isHighUsed(newConditionRefl.getAction())){
                SensorPhysicalBean highResult = sensorHelper.doMathSensor(sensorId, null, newConditionRefl.getHigh());
                newConditionRefl.setHighTarget(highResult.getSensor_Value());
            }
        }

        // 如果数据库或者缓存都没有, 则插入
        if (conditionRefl == null) {
            conditionRefl = deviceDao.saveConditionRefl(deviceBean.deviceid, newConditionRefl);
        } else {
            conditionRefl = deviceDao.updateConditionRefl(conditionRefl.getId(), newConditionRefl);
        }

        // 更新缓存
        SysConfig.getInstance().getConditionReflCache().put(cacheKey, conditionRefl);
    }

    private static String conditionReflKey(String deviceId, int route) {
        return deviceId + route;
    }

    /**
     * 判断低阈值是否使用
     *
     * @param action
     * @return
     */
    private static boolean isLowUsed(int action){
        switch  (action){
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
        }

        return false;
    }

    /**
     * 判断高阈值是否使用
     *
     * @param action
     * @return
     */
    private static boolean isHighUsed(int action){
        switch  (action){
            case 2:
            case 5:
                return true;
        }

        return false;
    }
}
