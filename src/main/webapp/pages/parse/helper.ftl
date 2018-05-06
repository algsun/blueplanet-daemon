<#function v30PacketType(packetType)>
    <#switch packetType>
        <#case 1>  <#return "上行数据包">
        <#case 2>  <#return "数据响应包">
        <#case 3>  <#return "上行设备状态包">
        <#case 4>  <#return "状态应答包">
        <#case 5>  <#return "上行请求包">
        <#case 6>  <#return "请求应答包">
        <#case 9>  <#return "下行命令包">
        <#case 10> <#return "命令响应包">
    </#switch>
</#function>

<#function deviceType(deviceType)>
    <#switch deviceType>
        <#case 1> <#return "节点">
        <#case 2> <#return "中继">
        <#case 3> <#return "主模块">
        <#case 4> <#return "从模块">
        <#case 5> <#return "控制模块">
        <#case 7> <#return "网关">
    </#switch>
</#function>

<#function isControl(control)>
    <#switch control>
        <#case 0> <#return "可控">
        <#case 1> <#return "不可控">
    </#switch>
</#function>

<#function voltage(voltage)>
    <#switch voltage>
        <#case 0> <#return "正常">
        <#case 1> <#return "低电">
        <#case 2> <#return "掉电">
        <#default>
            <#if (voltage >= 20)><#return voltage / 10.0 + "V" ></#if>
    </#switch>
</#function>

<#function workMode(workMode)>
    <#switch workMode>
        <#case 0><#return "正常">
        <#case 1><#return "巡检">
    </#switch>
</#function>


<#-- 返回条件反射运作描述 -->
<#function descOfCondtionReflAction action>
    <#switch action>
        <#case 0><#return "无条件关">
        <#case 2><#return "范围内开，范围外关">
        <#case 3><#return "高于高阈值开，低于低阈值关">
        <#case 4><#return "高于高阈值关，低于低阈值开">
        <#case 5><#return "范围外关，范围外开">
        <#case 7><#return "无条件关">
        <#case 8><#return "无条件反射">
    </#switch>
</#function>

<#-- 返回故障代码对应的描述 -->
<#function descOfFaultCode code>
    <#switch code>
        <#case statics["java.lang.Long"].parseLong("0000", 16)><#return "正常">
        <#case statics["java.lang.Long"].parseLong("0001", 16)><#return "采样值超出传感器量化范围（传感器自身无无效特征符输出）">
        <#case statics["java.lang.Long"].parseLong("0002", 16)><#return "传感器没接，或者硬件故障原因造成无法采样到数据">
        <#case statics["java.lang.Long"].parseLong("0003", 16)><#return "传感器输出数据无效，(传感器自身有无效特征符输出)">
        <#case statics["java.lang.Long"].parseLong("0004", 16)><#return "地址越界">
        <#case statics["java.lang.Long"].parseLong("0005", 16)><#return "操作超时">
        <#case statics["java.lang.Long"].parseLong("0080", 16)><#return "主从通信超时">
        <#case statics["java.lang.Long"].parseLong("0081", 16)><#return "异常功能码(除03、04 之外的功能码)">
        <#case statics["java.lang.Long"].parseLong("0082", 16)><#return "地址越界">
        <#case statics["java.lang.Long"].parseLong("0083", 16)><#return "读取的寄存器个数">
        <#case statics["java.lang.Long"].parseLong("0084", 16)><#return "读取寄存器失败">
        <#case statics["java.lang.Long"].parseLong("00FE", 16)><#return "不明原因的采样失败">
        <#case statics["java.lang.Long"].parseLong("FFFF", 16)><#return "软件部专用">
    </#switch>
</#function>
