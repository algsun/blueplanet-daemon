<#--
@author gaohui
@date 2013-10-28
-->

<#include "common-tag.ftl">
<#include "common-head.ftl">

<!DOCTYPE html>
<html>
<head>
<@head "HTTP 接口说明"/>
    <style>
        .nav>li>a {
            padding-top: 5px;
            padding-bottom: 5px;
        }
    </style>
</head>
<body data-spy="scroll" data-target="#navs">


<div class="container">
<#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <p>这里是对外的接口说明文档。接口遵守 rest 风格，无特殊说明，返回结果为<code>json</code>(application/json)。
                访问根路径为<code>${basePath()}/</code>，以下接口均基于根路径。
            </p>
        </div>
    </div>

    <div class="row">
        <div class="col-md-3">
            <#assign __FILE__ = basePath() + "/struts/api-doc">
            <ul id="navs" class="nav nav-stacked nav-pills" data-spy="affix" data-offset-top="20" data-offset-bottom="20">
                <li><a href="${__FILE__}#modifyInterval">修改工作周期</a></li>
                <li><a href="${__FILE__}#patrol-check-open-site">开始巡检(站点)</a></li>
                <li><a href="${__FILE__}#patral-check-close-site">结束巡检(站点)</a></li>
                <li><a href="${__FILE__}#delete-device">删除设备</a></li>
                <li><a href="${__FILE__}#patrol-check-start-device">开始巡检(设备)</a></li>
                <li><a href="${__FILE__}#patrol-check-end-device">结束巡检(设备)</a></li>
                <li><a href="${__FILE__}#set-default-parent">设置设备默认父节点</a></li>
                <li><a href="${__FILE__}#conditionRefl">设备条件反射(控制模块)</a></li>
                <li><a href="${__FILE__}#conditionReflOrigin">计算原始值(控制模块)</a></li>
                <li><a href="${__FILE__}#turnSwitch">控制开关(控制模块)</a></li>
                <li><a href="${__FILE__}#switch-action-time-changed">自动控制时间类改变(控制模块)</a></li>
                <li><a href="${__FILE__}#restart">设备重启</a></li>
                <li><a href="${__FILE__}#suspend">中继待机</a></li>
                <li><a href="${__FILE__}#available-parents">查询可选父节点</a></li>
                <li><a href="${__FILE__}#rf-alive">节点RF不休眠</a></li>
                <li><a href="${__FILE__}#threshold-alarm-state">设置阈值报警状态</a></li>
                <li><a href="${__FILE__}#sensor-threshold">设置监测指标阈值</a></li>
                <li><a href="${__FILE__}#evict-threshold-alarm-cache">清除阈值报警缓存</a></li>
                <li><a href="${__FILE__}#evict-custom-formula-cache">清除设备自定义公式</a></li>
                <li><a href="${__FILE__}#other">其他</a></li>
            </ul>
        </div>
        <div class="col-md-9">
        <@markdown><#include "api-doc.md"></@markdown>
        </div>
    </div>
</div>

<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="assets/bootstrap/3.0.2/js/bootstrap.min.js"></script>
<script>
    // 给 table 添加 bootstrap 样式
    $('table').addClass("table table-bordered");
</script>
</body>
</html>

<#-- pegdown 实现 -->
<#macro markdown >
    <#local md><#nested/></#local>
${action.markdown2html(md)}
</#macro>
