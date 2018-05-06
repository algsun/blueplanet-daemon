<#--
@author gaohui
@date 2013-10-28
-->

<#include "common-tag.ftl">
<#include "common-head.ftl">
<#include "sensors-helper.ftl">

<!DOCTYPE html>
<html>
<head>
<@head "传感量"/>
</head>
<body>


<div class="container">
<#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <h3>监测指标/传感量</h3>
            <h3>${sensor.cnName}</h3>
            <table class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>ID</th>
                    <th>HEX/16进制</th>
                    <th>名称</th>
                    <th>单位</th>
                    <th>类型</th>
                    <th>精度</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>${1}</td>
                    <td>${sensor.physicalId?c}</td>
                    <td>0x${statics["java.lang.String"].format("%04X", sensor.physicalId)}</td>
                    <td>${sensor.cnName}</td>
                    <td>${sensor.unit}</td>
                    <td>${showType(sensor.showType)}</td>
                    <td>${sensor.precision?c}</td>
                </tr>
                </tbody>
            </table>

            <h4>公式</h4>
            <table class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>公式</th>
                    <th>名称</th>
                    <th>描述</th>
                    <th>原始值符号类型</th>
                    <th>原始值范围</th>
                    <th>原始值范围类型</th>
                    <th>结果范围</th>
                    <th>结果范围类型</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>${formula.id}</td>
                    <td>${formula.name}</td>
                    <td>${formula.description}</td>
                    <td>${signType(formula.signType)}</td>
                    <td>${formula.minX} ~ ${formula.maxX}</td>
                    <td>${rangeType(formula.xRangeType)}</td>
                    <td>${formula.minY} ~ ${formula.maxY}</td>
                    <td>${rangeType(formula.yRangeType)}</td>
                </tr>
                </tbody>
            </table>

            <h4>默认系数</h4>
            <ul>
            <#list formula.formulaParams?keys as key>
                <li><strong>${key}</strong>：${formula.formulaParams.get(key)}</li>
            </#list>
            </ul>

            <h4>原始值计算</h4>

            <div class="form-inline">
                <div class="form-group">
                    <label>原始值</label>
                </div>
                <div class="form-group">
                    <input id="value" name="value" type="number" class="form-control" min="0" max="65535" required/>
                    <input id="sensorId" type="hidden" value="${sensorId?c}"/>
                </div>
                <div class="form-group">
                    <span class="help-inline">范围为0~65535</span>
                </div>
                <div class="form-group">
                    <button id="compute" type="submit" class="btn btn-default">计算</button>
                </div>
            </div>
            <p id="output"></p>
        </div>
    </div>
</div>

<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
    $(function () {
        $("#compute").click(function () {
            var sensorId = $("#sensorId").val();
            var value = $("#value").val();
            if (value) {
                $.get("struts/sensors/" + sensorId + "/compute", {value: value}, function (result) {
                    var $output = $("#output");
                    $output.empty();

                    var sensor = result.sensorBean;
                    if (!sensor) {
                        return;
                    }

                    if (sensor.sensor_State === 1) {
                        $output.append(sensor.sensor_Value);
                    } else if (sensor.sensor_State === 0) {
                        $output.append(sensor.sensor_Value);
                        $output.append("<br>");
                        switch (sensor.errorType) {
                            case 0:
                                $output.append("采样异常");
                                $output.append("<br>");
                                break;
                            case 1:
                                $output.append("计算异常");
                                $output.append("<br>");
                                break;
                            case 2:
                                $output.append("结果超出范围");
                                $output.append("<br>");
                                break;
                        }
                    }
                });
            }
        });
    });
</script>
</body>
</html>