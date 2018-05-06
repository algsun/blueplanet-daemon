<#function showType(type)>
    <#switch type>
        <#case 0>
            <#return "标量">
        <#case 1>
            <#return "风向类">
        <#case 2>
            <#return "GPS类">
        <#case 3>
            <#return "开关量">
    </#switch>
</#function>

<#function rangeType(type)>
    <#switch type>
        <#case 0>
            <#return "无限制">
        <#case 1>
            <#return "最小值限制">
        <#case 2>
            <#return "最大值限制">
        <#case 3>
            <#return "最小最大值限制">
    </#switch>
</#function>

<#function signType(type)>
    <#switch type>
        <#case 0>
            <#return "无符号">
        <#case 1>
            <#return "有符号">
    </#switch>
</#function>

