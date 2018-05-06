package com.microwise.msp.hardware.handler.formula;

import com.google.common.annotations.VisibleForTesting;
import com.microwise.msp.hardware.businessbean.Formula;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 14-1-2 16:22
 */
public class Formulas {
    // formula id => function
    private static Map<Integer, Function> functions = new HashMap<Integer, Function>();
    private static Map<Integer, ReverseFunction> reverseFunctions = new HashMap<Integer, ReverseFunction>();

    static {
        functions.put(1, new LinearFunction());
        functions.put(2, new QuadraticFunction());
        functions.put(3, new CubicFunction());
        functions.put(4, new DegreeFunction());
        functions.put(5, new TwoLinearFunction());
        functions = Collections.unmodifiableMap(functions);

        reverseFunctions.put(1, new LinearReverseFunction());
        reverseFunctions.put(2, new QuadraticReverseFunction());
        reverseFunctions = Collections.unmodifiableMap(reverseFunctions);
    }


    public static Double compute(double x, Formula formula, Map<String, String> formulaParams) {
        if (!functions.containsKey(formula.getId())) {
            throw new IllegalArgumentException("未知公式: " + formula.getId());
        }

        return functions.get(formula.getId()).compute(x, toDouble(formulaParams));
    }

    public static double reverseCompute(double target, Formula formula, Map<String, String> formulaParams){
        if (!reverseFunctions.containsKey(formula.getId())) {
            throw new IllegalArgumentException("未知公式: " + formula.getId());
        }

        return reverseFunctions.get(formula.getId()).compute(target, formula, toDouble(formulaParams));

    }

    @VisibleForTesting
    public static Map<String, Double> toDouble(Map<String, String> params) {
        Map<String, Double> doubleParams = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            doubleParams.put(entry.getKey(), Double.parseDouble(entry.getValue()));
        }

        return doubleParams;
    }

}
