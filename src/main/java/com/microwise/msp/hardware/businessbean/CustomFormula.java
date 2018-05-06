package com.microwise.msp.hardware.businessbean;

import java.util.Map;

/**
 * 设备自定义公式
 *
 * @author gaohui
 * @date 14-1-3 13:25
 */
public class CustomFormula {

    // 设备自定义公式, 无则为 null
    private Formula formula;

    // 设备自定义公式系数, 无则为 null
    private Map<String, String> formulaParams;

    public CustomFormula(Formula formula, Map<String, String> formulaParams) {
        this.formula = formula;
        this.formulaParams = formulaParams;
    }

    public Formula getFormula() {
        return formula;
    }

    public Map<String, String> getFormulaParams() {
        return formulaParams;
    }

    @Override
    public String toString() {
        return "CustomFormula{" +
                "formula=" + formula +
                ", formulaParams=" + formulaParams +
                '}';
    }
}
