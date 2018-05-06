<#--
@author gaohui
@date 2013-10-28
-->

<#include "common-tag.ftl">
<#include "sensors-helper.ftl">

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <base href="${basePath()}/">
    <title>缓存</title>
    <link rel="stylesheet" href="assets/bootstrap/3.0.2/css/bootstrap.min.css"/>
</head>
<body>


<div class="container">
<#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <h3>缓存</h3>
            <table class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>描述</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>设备</td>
                    <td>
                        写后1天清除
                    </td>
                    <td>
                        <a href="struts/caches/device?_method=delete">清除</a>
                    </td>
                </tr>
                <tr>
                    <td>监测指标</td>
                    <td>
                        写后1天清除
                    </td>
                    <td>
                        <a href="struts/caches/sensors?_method=delete">清除</a>
                    </td>
                </tr>
                <tr>
                    <td>默认公式系数</td>
                    <td>
                        写后1天清除
                    </td>
                    <td>
                        <a href="struts/caches/formula?_method=delete">清除</a>
                    </td>
                </tr>
                <tr>
                    <td>设备监测指标</td>
                    <td>
                        写后1小时清除
                    </td>
                    <td>
                        <a href="struts/caches/device-sensors?_method=delete">清除</a>
                    </td>
                </tr>
                <tr>
                    <td>设备自定义公式系数</td>
                    <td>
                        写后1小时清除
                    </td>
                    <td>
                        <a href="struts/caches/device-custom-formula?_method=delete">清除</a>
                    </td>
                </tr>
                <tr>
                    <td>阈值报警</td>
                    <td>
                        写后1天清除
                    </td>
                    <td>
                        <a href="struts/caches/threshold-alarms?_method=delete">清除</a>
                    </td>
                </tr>
                <tr>
                    <td>监测指标浮动值</td>
                    <td>
                        写后1小时清除
                    </td>
                    <td>
                        <a href="struts/caches/evictAllFloatSensor?_method=delete">清除</a>
                    </td>
                </tr>
                <tr>
                    <td>监听端口</td>
                    <td>
                        写后1天清除
                    </td>
                    <td>
                    </td>
                </tr>
                <tr>
                    <td>站点</td>
                    <td>
                        写后1小时清除
                    </td>
                    <td>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
</body>
</html>