package com.microwise.msp.hardware.cache;

import com.google.common.cache.CacheLoader;
import com.microwise.msp.hardware.businessbean.Formula;
import com.microwise.msp.hardware.businessservice.FormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author gaohui
 * @date 14-1-2 13:30
 */
@Component
@Scope("prototype")
public class DefaultFormulaLoader extends CacheLoader<Integer, Formula> {
    @Autowired
    private FormulaService formulaService;

    @Override
    public Formula load(Integer sensorId) throws Exception {
        Formula formula = formulaService.findBySensorId(sensorId);
        if(formula == null){
            throw new Exception("未找到默认公式: " + sensorId);
        }
        return formula;
    }
}
