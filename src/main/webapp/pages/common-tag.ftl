<#--
页面 script, link 标签支持, 并提供缓存过期机制

@author gaohui
@date 2013-02-25
-->
<#macro scriptTag src>
<script type="text/javascript" src="${src}?${Application['app.startTime']?long?c}"></script>
</#macro>


<#macro linkTag href type= "text/css">
<link type="${type}" rel="stylesheet" href="${href}?${Application['app.startTime']?long?c}">
</#macro>

<#--
辅助 select 标签 option 的 selected 属性

@param expected 期待的值
@param value 实际的值
-->
<#macro selected expected, value>
<#if expected == value>selected="selected"</#if>
</#macro>

<#macro checked expected, value>
    <#if expected == value>checked="checked"</#if>
</#macro>

<#-- 返回当前web应用的基路径, 注意结尾不包含 "/" -->
<#function basePath>
    <#local __base =  request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()?c + request.getContextPath()>
    <#return __base>
</#function>
