//1.WiseBee-V1协议下的所有传感量

// 是否是常规计算公式, 如果不是,将调用监测指标定制的函数
function isGeneral(sensorId) {
    switch (sensorId) {
        case 43:
            return false;
        case 48:
            return false;
        case 50:
            return false;
        case 65:
            return false;
        case 68:
            return false;
        case 69:
            return false;
        case 70:
            return false;
        case 33:
            return false;//2013年12月6日 Vaisala采用补码上传数据，因此温度需要做特殊处理
    }
    return true;
}

//计算温度
function get_33(value, params) {
    if (value > 32767) {
        value = value - 65536;
    }
    return a2_b1_c0(value, params);
}

//计算露点 -- 不做脚本计算
function get_43(value, params) {
    return value;
}

//计算风向
function get_48(value, params) {
    var result = a2_b1_c0(value, params);
    return doDegree(result);
}

//风力--不做脚本计算
function get_50(value, params) {
    return value;
}

//计算导线温度
function get_51(value, params) {
    return generalCompute(value, params);
}

//计算微风风向
function get_65(value, params) {
    var result = a2_b1_c0(value, params);
    return doDegree(result);
}

//计算5TE土壤温度
function get_68(value, params) {
    var result;
    if (value <= 900) {
        result = a2_b1_c0(value, params);
    } else {
        result = d2_e1_f(value, params);
    }

    return result;
}

//计算5TE土壤含水率
function get_69(value, params) {
    return a3_b2_c1_d(value, params);
}

//计算5TE电导率
function get_70(value, params) {
    var result;
    if (value <= 700) {
        result = a2_b1_c0(value, params);
    } else {
        result = d2_e1_f(value, params);
    }

    return result;
}

//计算低电标识
function isLowVoltage(deviceType, value) {
    var v_high = 10.5;
    var v_middle = 8;
    var v_low = 3.5;
    var v_low_gateway = 3.6;
    if (deviceType === 7) {
        if ((value < v_high && value >= v_middle) || parseFloat(value).toFixed(2) < parseFloat(v_low_gateway).toFixed(2)) {
            return true;
        }
    } else {
        if ((value < v_high && value >= v_middle) || value < v_low) {
            return true;
        }
    }
    return false;
}

// 通用计算方法
function generalCompute(originValue, params) {
    return a2_b1_c0(originValue, params);
}

// 一元二次方程(a, b, c)
function a2_b1_c0(value, params) {
    // 转为 float, 否则可能会字符串拼接
    var a = parseFloat(params.get("a"));
    var b = parseFloat(params.get("b"));
    var c = parseFloat(params.get("c"));

    return  (a * value * value) + (b * value) + c;
}

// 一元三次方程(a, b, c d)
function a3_b2_c1_d(value, params) {
    var a = parseFloat(params.get("a"));
    var b = parseFloat(params.get("b"));
    var c = parseFloat(params.get("c"));
    var d = parseFloat(params.get("d"));

    return (a * value * value * value) + (b * value * value) + ( c * value) + d;
}

// 一元二次方程(d, e, f)
function d2_e1_f(value, params) {
    var d = parseFloat(params.get("d"));
    var e = parseFloat(params.get("e"));
    var f = parseFloat(params.get("f"));

    return  (d * value * value) + (e * value) + f;
}

// 角度规整
function doDegree(degree) {
    if (degree > 11.25 && degree <= 33.75) {
        degree = 22.5;
    } else if (degree > 33.75 && degree <= 56.25) {
        degree = 45;
    } else if (degree > 56.25 && degree <= 78.75) {
        degree = 67.5;
    } else if (degree > 78.75 && degree <= 101.25) {
        degree = 90;
    } else if (degree > 101.25 && degree <= 123.75) {
        degree = 112.5;
    } else if (degree > 123.75 && degree <= 146.25) {
        degree = 135;
    } else if (degree > 146.25 && degree <= 168.75) {
        degree = 157.5;
    } else if (degree > 168.75 && degree <= 191.25) {
        degree = 180;
    } else if (degree > 191.25 && degree <= 213.75) {
        degree = 202.5;
    } else if (degree > 213.75 && degree <= 236.25) {
        degree = 225;
    } else if (degree > 236.25 && degree <= 258.75) {
        degree = 247.5;
    } else if (degree > 258.75 && degree <= 281.25) {
        degree = 270;
    } else if (degree > 281.25 && degree <= 303.75) {
        degree = 292.5;
    } else if (degree > 303.75 && degree <= 326.25) {
        degree = 315;
    } else if (degree > 326.25 && degree <= 348.75) {
        degree = 337.5;
    } else {
        degree = 0;
    }

    return degree;
}
