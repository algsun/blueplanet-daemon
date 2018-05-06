<#--
 请求应答
@author xu.baoji
@date 2013-10-28
-->

<h3>请求应答包</h3>

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
            <label>源请求包序列号：</label>
        </td>
        <td>
        ${packet.sourceSequence!}
        </td>
    </tr>
    <tr>
        <td class="text-right">
            <label>反馈结果：</label>
        </td>
        <td>
        ${packet.feedback!}
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
