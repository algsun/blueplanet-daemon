<#--
 上行请求包
@author xu.baoji
@date 2013-10-28
-->

<h3>上行请求包</h3>

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
            <label>版本号：</label>
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
            <label>指令编号：</label>
        </td>
        <td>
        ${packet.orderId!}
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

<#if packet.orderId == 3>
    <tr>
        <td class="text-right">
            <label>节点ID：</label>
        </td>
        <td>
        ${packet.terminalId!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>时间：</label>
        </td>
        <td>
        ${packet.timestamp!}
        </td>
    </tr>
</#if>
    <tr>
        <td class="text-right">
            <label>CRC：</label>
        </td>
        <td>
        ${packet.crc?c}
        </td>
    </tr>
</table>
