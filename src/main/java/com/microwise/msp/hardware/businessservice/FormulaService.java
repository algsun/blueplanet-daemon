package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.businessbean.Formula;
import com.microwise.msp.hardware.dao.FormulaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author gaohui
 * @date 13-12-31 15:12
 */
@Service
@Scope("prototype")
@Transactional
public class FormulaService {

    @Autowired
    private FormulaDao formulaDao;

    public Formula findBySensorId(int sensorId) {
        Formula formula = formulaDao.findBySensorId(sensorId);
        if (formula == null) {
            return null;
        }

        List<Map<String, String>> params = formulaDao.findParamsBySensorId(sensorId);
        Map<String, String> allParam = new LinkedHashMap<String, String>();
        for (Map<String, String> param : params) {
            allParam.put(param.get("name"), param.get("value"));
        }
        formula.setFormulaParams(allParam);
        return formula;
    }

    /**
     * 查询某个设备上某个监测指标的自定义公式。
     *
     * 如果无返回 null。如果只有自定义公式，没有自定义公式系数，公式系数为 null。
     *
     * @param deviceId
     * @param sensorId
     * @return
     */
    public Formula findByDeviceId(String deviceId, int sensorId) {
        Formula formula = formulaDao.findByDeviceId(deviceId, sensorId);
        List<Map<String, String>> params = formulaDao.findParamsByDeviceId(deviceId, sensorId);
        // params 可以为 null
        if (params != null) {
            Map<String, String> allParam = new LinkedHashMap<String, String>();
            for (Map<String, String> param : params) {
                allParam.put(param.get("name"), param.get("value"));
            }
            formula.setFormulaParams(allParam);
        }
        return formula;
    }

    public List<Formula> findAllByDeviceId(String deviceId) {
        return formulaDao.findAllByDeviceId(deviceId);
    }

    public Map<Integer, Map<String, String>> findParamsByDeviceId(String deviceId) {
        return formulaDao.findParamsByDeviceId(deviceId);
    }

    public void saveOrUpdateParamsByDeviceId(String deviceId, int sensorId, Map<String, String> params) {
        formulaDao.deleteParamsByDeviceIdAndSensorId(deviceId, sensorId);

        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            formulaDao.saveCustomParam(deviceId, sensorId, entry.getKey(), entry.getValue());
        }
    }
}
