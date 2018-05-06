<#--
@author gaohui
@date 2013-10-28
-->

<#include "common-tag.ftl">
<#include "common-head.ftl">

<!DOCTYPE html>
<html>
<head>
    <@head "后台任务"/>
</head>
<body>


<div class="container">
<#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <h3>正在执行的任务</h3>
            <ul>
                <#if scheduler.currentlyExecutingJobs?size lt 1>
                    <li>暂无任务</li>
                </#if>
                <#list scheduler.currentlyExecutingJobs as exeCxt>
                    <li>
                        ${exeCxt.trigger} [${exeCxt.jobDetail}]
                    </li>
                </#list>
            </ul>
        </div>
    </div>
</div>

<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
</body>
</html>