<#--
 v13 网关心跳包 解析
@author xu.baoji
@date 2013-10-28
-->

<h3>网关心跳包</h3>

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
            <label>CRC：</label>
        </td>
        <td>
        ${packet.crc?c}
        </td>
    </tr>
</table>
