<#--
 v13 上行数据包 解析
@author xu.baoji
@date 2013-10-28
-->
<#include "helper.ftl">
<#setting number_format="computer">

<h3>上行数据包</h3>

<table class="table table-striped table-hover">
<thead>
<tr>
    <th>名称</th>
    <th>值</th>
    <th>说明</th>
</tr>
</thead>
<tbody>

<tr>
    <td class="text-right">
        <label>包类型：</label>
    </td>
    <td>
    ${packet.packetType!}
    </td>
    <td> ${v30PacketType(packet.packetType)} </td>
</tr>
<tr>
    <td class="text-right">
        <label>终端类型：</label>
    </td>
    <td>${packet.deviceType!}</td>
    <td>${deviceType(packet.deviceType)}</td>
</tr>
<tr>
    <td class="text-right">
        <label>版本号：</label>
    </td>
    <td>
    ${packet.version!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>包长：</label>
    </td>
    <td>
    ${packet.bodyLength!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>可控：</label>
    </td>
    <td>
    ${packet.control!"1"}
    </td>
    <td>${isControl(packet.control)}</td>
</tr>
<tr>
    <td class="text-right">
        <label>父节点ID：</label>
    </td>
    <td>
    ${packet.parentId!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>节点ID：</label>
    </td>
    <td>
    ${packet.selfId!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>包序列号：</label>
    </td>
    <td>
    ${packet.sequence!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>电压：</label>
    </td>
    <td>
    ${packet.voltage!}
    </td>
    <td>${voltage(packet.voltage)}</td>
</tr>
<tr>
    <td class="text-right">
        <label>RSSI：</label>
    </td>
    <td>
    ${packet.rssi!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>LQI：</label>
    </td>
    <td>
    ${packet.lqi!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>监测指标：</label>
    </td>
    <td>
    ${packet.sensors!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>预热时间：</label>
    </td>
    <td>
    ${packet.warmUpTime!}
    </td>
    <td>单位：秒</td>
</tr>
<tr>
    <td class="text-right">
        <label>工作周期：</label>
    </td>
    <td>
    ${packet.interval!}
    </td>
    <td>单位：秒</td>
</tr>
<tr>
    <td class="text-right">
        <label>时间戳：</label>
    </td>
    <td>
    ${(packet.timestamp?string('yyyy-MM-dd HH:mm:ss'))!"无"}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>接入点/站点：</label>
    </td>
    <td>
    ${packet.siteId!}
    </td>
    <td></td>
</tr>
<tr>
    <td class="text-right">
        <label>SD卡状态：</label>
    </td>
    <td>
    ${packet.sdCardState!}
    </td>
    <td></td>
</tr>
<#if packet.faultCodes??>
<tr>
    <td class="text-right">
        <label>故障代码：</label>
    </td>
    <td>
    ${packet.faultCodes!}
    </td>
    <td>
        <ul style="font-size: 12px;">
            <#list packet.faultCodes as faultCode>
            <li>${faultCode}: ${descOfFaultCode(faultCode)}</li>
            </#list>
        </ul>
    </td>
</tr>
</#if>
<#if packet.connectionCountExists>
<tr>
    <td class="text-right">
        <label>搜网次数：</label>
    </td>
    <td>
    ${packet.connectionCount!}
    </td>
    <td></td>
</tr>
</#if>
<tr>
    <td class="text-right">
        <label>工作模式：</label>
    </td>
    <td>
    ${packet.workMode!}
    </td>
    <td>${workMode(packet.workMode)}</td>
</tr>
<#if packet.gpsExists>
<tr>
    <td class="text-right">
        <label>GPS：</label>
    </td>
    <td>
    ${packet.gpsParams!}
    </td>
    <td></td>
</tr>
</#if>
<#if packet.switchExists>
<tr>
    <td class="text-right">
        <label>开关状态：</label>
    </td>
    <td> 标识：${packet.switchCondRefl?string("条件反射", "正常")}
        <table class="table">
            <tr>
                <td>index</td>
                <td>enable</td>
                <td>on/off</td>
                <td>changed</td>
            </tr>
            <#list packet.switches as switch>
                <tr>
                    <td>${switch.index!}</td>
                    <td>${switch.enable?string("启用","禁用")}</td>
                    <td>${switch.on?string("开","关")}</td>
                    <td>${switch.changed?string("切换", "无")}</td>
                </tr>
            </#list>
        </table>
    </td>
    <td></td>
</tr>
</#if>
<#if packet.conditionReflExists>
<tr>
    <td class="text-right">
        <label>条件反射参数：</label>
    </td>
    <td>
        <#assign conditionRefl = packet.conditionRefl>
        路数: ${conditionRefl.route},
        设备ID: ${conditionRefl.subTerminalId},
        监测指标: ${conditionRefl.sensorId},
        低阈值:[${conditionRefl.lowLeft}, ${conditionRefl.low}, ${conditionRefl.lowRight}],
        高阈值:[${conditionRefl.highLeft}, ${conditionRefl.high}, ${conditionRefl.highRight}],
        动作: ${conditionRefl.action} (${descOfCondtionReflAction(conditionRefl.action)})
    </td>
    <td></td>
</tr>
</#if>
<tr>
    <td class="text-right">
        <label>CRC：</label>
    </td>
    <td>
    ${packet.crc?c}
    </td>
    <td></td>
</tr>
</tbody>
</table>
