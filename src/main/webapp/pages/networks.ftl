<#--
@author gaohui
@date 2013-10-28
-->
<#include "common-tag.ftl">
<#include "common-head.ftl">

<!DOCTYPE html>
<html>
<head>
    <@head "监听端口"/>
</head>
<body>


<div class="container">
    <#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <h3>监听端口</h3>

            <a class="btn btn-default pull-right" href="struts/networks/new"><i class="glyphicon glyphicon-plus"></i> 添加</a>

            <table class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>ID</th>
                    <th>监听端口</th>
                    <th>类型</th>
                    <th>站点ID/接入点号</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list netinfos as netinfo>
                <tr>
                    <td>${netinfo_index + 1}</td>
                    <td>${netinfo.id}</td>
                    <td>${netinfo.lport?c}</td>
                    <td><#if netinfo.model == 2 > UDP <#elseif netinfo.model == 3> TCP </#if></td>
                    <td>${netinfo.siteId!"无"}</td>
                    <td>
                        <a class="btn btn-xs btn-danger" href="struts/networks/${netinfo.id}?_method=delete">删除</a>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>

            <p class="help-block">温馨提示：站点ID/接入点号必需为8位数字。V1.3协议的网关通讯端口必需要绑定站点ID，V3协议的网关无此限制。</p>
        </div>
    </div>
</div>

</body>
</html>