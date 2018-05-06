<#--
@author gaohui
@date 2013-10-28
-->

<#include "common-tag.ftl">
<#include "common-head.ftl">

<!DOCTYPE html>
<html>
<head>
    <@head "日志"/>
</head>
<body>


<div class="container">
<#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <h3>日志文件</h3>

            <h4><a href="struts/logs/files">blueplanet-daemon</a>${dir}</h4>
            <table class="table">
                <thead>
                <tr>
                    <th>文件名</th>
                    <th>最后修改时间</th>
                </tr>
                </thead>
                <tbody>
                <#list files as file>
                <tr>
                    <td>
                        <#if file.isDirectory()>
                            <a href="struts/logs/files?dir=${dir}/${file.name}">
                                <i class="glyphicon glyphicon-folder-open"></i> ${file.name}
                            </a>
                        </#if>
                        <#if file.isFile()>
                            <a href="struts/logs/raw?file=${dir}/${file.name}">
                            <i class="glyphicon glyphicon-file"></i> ${file.name}
                            </a>
                        </#if>
                    </td>
                    <td>${(file.lastModified()?number_to_date)?string("yyyy-MM-dd HH:mm:ss")}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>