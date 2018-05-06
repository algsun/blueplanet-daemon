<#--
 v13 上行数据包 解析
@author xu.baoji
@date 2013-10-28
-->
<#include "helper.ftl">

<h3>上行数据包</h3>

<table class="table table-striped table-hover">
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
        <td>
        ${packet.deviceType!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>协议版本：</label>
        </td>
        <td>
        ${packet.version!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>包长：</label>
        </td>
        <td>
        ${packet.bodyLength!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>子网ID：</label>
        </td>
        <td>
        ${packet.netId!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>跳数：</label>
        </td>
        <td>
        ${packet.jump!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>父节点ID：</label>
        </td>
        <td>
        ${packet.parentId!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>节点ID：</label>
        </td>
        <td>
        ${packet.selfId!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>反馈地址：</label>
        </td>
        <td>
        ${packet.feedback!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>包序列号：</label>
        </td>
        <td>
        ${packet.sequence!}
        </td>
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
    </tr>
    <tr>
        <td class="text-right">
            <label>LQI：</label>
        </td>
        <td>
        ${packet.lqi!}
        </td>
    </tr>

    <tr>
        <td class="text-right">
            <label>监测指标：</label>
        </td>
        <td>
        ${packet.sensors!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>CRC：</label>
        </td>
        <td>
        ${packet.crc?c}
        </td>
    </tr>
</table>
