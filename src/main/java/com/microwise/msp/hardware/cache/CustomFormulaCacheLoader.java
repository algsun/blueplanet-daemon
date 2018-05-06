package com.microwise.msp.hardware.cache;

import com.google.common.base.Function;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.microwise.msp.hardware.businessbean.CustomFormula;
import com.microwise.msp.hardware.businessbean.Formula;
import com.microwise.msp.hardware.businessservice.FormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备自定义公式
 *
 * @author gaohui
 * @date 14-1-3 13:27
 */
@Component
@Scope("prototype")
public class CustomFormulaCacheLoader extends CacheLoader<String, Map<Integer, CustomFormula>> {

    @Autowired
    private FormulaService formulaService;

    @Override
    public Map<Integer, CustomFormula> load(String deviceId) throws Exception {
        List<Formula> formulas = formulaService.findAllByDeviceId(deviceId);
        Map<Integer, Formula> sensorFormulaMap  = Maps.uniqueIndex(formulas, new Function<Formula, Integer>() {
            @Override
            public Integer apply(Formula formula) {
                return formula.getSensorId();
            }
        });

        Map<Integer, Map<String, String>> sensorParamMap = formulaService.findParamsByDeviceId(deviceId);

        Map<Integer, CustomFormula> customFormulaMap = new HashMap<Integer, CustomFormula>();
        for (Integer sensorId: Sets.union(sensorFormulaMap.keySet(), sensorParamMap.keySet())) {
            CustomFormula customFormula = new CustomFormula(sensorFormulaMap.get(sensorId), sensorParamMap.get(sensorId));
            customFormulaMap.put(sensorId, customFormula);
        }
        return customFormulaMap;
    }
}
