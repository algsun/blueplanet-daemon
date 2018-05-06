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
                <#list sensors as sensor>
                <tr>
                    <td>${sensor_index + 1}</td>
                    <td>${sensor.physicalId?c}&nbsp;<#if sensor.escapeSensorId !=0 >(转义后监测指标${sensor.escapeSensorId})</#if></td>
                    <td>0x${statics["java.lang.String"].format("%04X", sensor.physicalId)}</td>
                    <td><a href="struts/sensors/${sensor.physicalId?c}">${sensor.cnName}</a></td>
                    <td>${sensor.unit}</td>
                    <td>${showType(sensor.showType)}</td>
                    <td>${sensor.precision?c}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>