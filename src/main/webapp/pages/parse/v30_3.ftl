<#--
 v13 上行设备状态包 解析
@author xu.baoji
@date 2013-10-28
-->

<h3>设备状态包</h3>

<table>
    <tr>
        <td class="text-right">
            <label>包类型：</label>
        </td>
        <td>
        ${packet.packetType!}
        </td>
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
            <label>标定状态：</label>
        </td>
        <td>
        ${packet.demarcate!}
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
            <label>工作周期：</label>
        </td>
        <td>
        ${packet.interval!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>工作模式：</label>
        </td>
        <td>
        ${packet.workMode!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>搜网次数：</label>
        </td>
        <td>
        ${packet.connectionCount!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>可控：</label>
        </td>
        <td>
        ${packet.control!"1"}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>产品序列号：</label>
        </td>
        <td>
        ${packet.serialNumber!"1"}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>接入点/站点：</label>
        </td>
        <td>
        ${packet.siteId!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>SD卡状态：</label>
        </td>
        <td>
        ${packet.sdCardState!}
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
