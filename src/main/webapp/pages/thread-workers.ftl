<#include "common-tag.ftl">
<#include "common-head.ftl">
<#setting number_format="0.####">

<!DOCTYPE html>
<html>
<head>
    <@head "线程队列"/>
    <meta http-equiv="refresh" content="5; URL=${basePath()}/struts/threads">
</head>
<body>

<div class="container">

<#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <h3>
                线程队列
            </h3>

            <div>
                总数：<strong style="font-size: 2em;">${workQueueSize}</strong>
            </div>

            <ul>
                <#list workQueueSizes as workQueueSize>
                <li>#${workQueueSize_index} => ${workQueueSize}</li>
                </#list>
            </ul>
        </div>
    </div>
</div>

</body>
</html>