<#--
@author gaohui
@date 2013-10-28
-->

<#include "common-tag.ftl">
<#include "common-head.ftl">

<!DOCTYPE html>
<html>
<head>
<@head "解析"/>
    <style type="text/css">
        .table>thead>tr>td,
        .table>tbody>tr>td{
            padding-top: 3px;
            padding-bottom: 3px;
        }
    </style>
</head>
<body>


<div class="container">
<#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <h3>数据包解析</h3>

            <form role="form" action="struts/parse" method="post">
                <div class="row">
                    <div class="col-md-1">
                        <label>数据包<span class="text-muted">(十六进制)</span></label>
                    </div>

                    <div class="col-md-7">
                        <textarea class="form-control" id="pack" rows="3" name="pack">${pack!}</textarea>
                    </div>

                    <div class="col-md-4">
                        <button class="btn btn-lg btn-primary">解析</button>
                        <span class="btn btn-lg btn-warning " id="clean">清空</span>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12" id="message">
        <#if isSuccess == true>
            <h5>输出： <span style="color: green">${message!"解析成功！"}</span></h5>
        <#else >
            <h5>输出：<span style="color: red">${message!}</span></h5>
        </#if>
        </div>
    </div>

    <div class="row">
        <div id="afterParse" class="col-md-8">
        <#if packet??>
            <#if packet.version == 1>
                <#if packet.packetType == 1>
                    <#include "parse/v13_1.ftl">
                <#elseif packet.packetType == 2>
                    <#include "parse/v13_2.ftl">
                <#elseif packet.packetType == 7>
                    <#include "parse/v13_7.ftl">
                </#if>
            <#elseif  packet.version == 3>
                <#if packet.packetType == 1>
                    <#include "parse/v30_1.ftl">
                <#elseif packet.packetType == 3>
                    <#include "parse/v30_3.ftl">
                <#elseif packet.packetType == 5>
                    <#include "parse/v30_5.ftl">
                <#elseif packet.packetType == 6>
                    <#include "parse/v30_6.ftl">
                <#elseif packet.packetType == 10>
                    <#include "parse/v30_0A.ftl">
                </#if>
            </#if>
        </#if>
        </div>
        <div class="col-md-5"></div>
    </div>
</div>


<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
    $(function () {
        $('#pack').focus();

        $("#clean").click(function () {
            $("#pack").text("").focus();
        });
    });
</script>
</body>
</html>
