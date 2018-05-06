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
        <div class="col-md-6">
            <form class="form" action="struts/networks" method="post">
                <fieldset>
                    <legend>添加监听端口</legend>
                    <div class="radio-inline">
                        <label>
                            <input name="model" type="radio" value="2" checked/> UDP
                        </label>

                    </div>
                    <div class="radio-inline">
                        <label>
                            <input name="model" type="radio" value="3"/> TCP
                        </label>
                    </div>

                    <div class="form-group">
                        <label>端口(必需)</label>
                        <input class="form-control" name="port" type="number" min="1024" max="65535" required value="${port?c!}"/>
                    </div>
                    <div class="form-group">
                        <label>站点ID / 接入点号</label>
                        <input class="form-control" name="siteId" type="number" maxlength="8"/>
                        <p class="help-block">
                            站点ID/接入点号必需为8位数字。V1.3协议的网关通讯端口必需要绑定站点ID，V3协议的网关无此限制。
                        </p>
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary" type="submit">保存</button>
                        <a class="btn btn-default" href="struts/networks">取消</a>
                    </div>
                </fieldset>
            </form>
        </div>
        <div class="col-md-6"></div>
    </div>
</div>

</body>
</html>
