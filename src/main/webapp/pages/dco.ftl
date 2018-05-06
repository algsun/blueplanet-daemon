<#--
@author gaohui
@date 2013-10-28
-->

<#include "common-tag.ftl">
<#include "common-head.ftl">
<#include "sensors-helper.ftl">

<!DOCTYPE html>
<html>
<head>
<@head "调试"/>
    <meta charset="utf-8">
    <style>
        hr {
            margin-top: 30px;
            margin-bottom: 30px;

        }

        #output {
            color: orangered;
            background-color: #ddd;
            font-size: 1.2em;
            padding: 10px;

            position: fixed;
            bottom: 100px;
            min-width: 100px;
        }
    </style>
    <script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="assets/jquery-form/jquery.form.min.js"></script>
    <script type="text/javascript" src="assets/bootstrap/3.0.2/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="assets/angular/angular.min.js"></script>
    <script type="text/javascript">
        $(function () {

            $("form").ajaxForm({
                beforeSubmit: function (arr, $form, options) {
                    $.each(arr, function (_, param) {
                        if (param.name === '_url') {
                            options.url = param.value;
                        }
                        if(param.name === 'targetTemperature') {
                          param.value = param.value * 10;

                        }
                        if(param.name === 'targetHumidity') {
                            param.value = param.value * 10;
                        }
                    });

                    for (var i = 0; i < arr.length; i++) {
                        if (arr[i].name === '_url') {
                            arr.splice(i, 1);
                        }
                    }

                    $('#output').empty().append("执行中...");
                },
                success: function (result) {
                    var $output = $('#output');
                    if (result.success) {
                        if (result.hasOwnProperty('sendSuccess')) {
                            if (result.sendSuccess) {
                                if (result.doSuccess) {
                                    $output.empty().append("执行成功");
                                } else {
                                    $output.empty().append("发送成功，执行失败");
                                }
                            } else {
                                $output.empty().append("发送失败");
                            }
                        } else {
                            $output.empty().append("发送成功");
                        }
                    } else {
                        $output.empty().append("错误");
                    }
                }
            });

        });
    </script>
</head>
<body ng-app data-spy="scroll" data-target="#navs" style="margin-bottom: 100px;">


<div class="container">

<div id="output"></div>

<#include "nav.ftl">

<div class="row">
<div id="navs" class="col-md-4">
    <ul data-spy="affix" data-offset-top="60" data-offset-bottom="200" class="nav nav-pills nav-stacked">
        <li><a href="${basePath()}/struts/debug#modifyInterval">修改工作周期</a></li>
        <li><a href="${basePath()}/struts/debug#patrol-check-start">开始巡检</a></li>
        <li><a href="${basePath()}/struts/debug#patrol-check-end">结束巡检</a></li>
        <li><a href="${basePath()}/struts/debug#set-default-parent">设置默认父节点</a></li>
        <li><a href="${basePath()}/struts/debug#relay-suspend">中继待机</a></li>
        <li><a href="${basePath()}/struts/debug#available-parents">查询可选父节点</a></li>
        <li><a href="${basePath()}/struts/debug#turn-switch">开关</a></li>
        <li><a href="${basePath()}/struts/debug#condition-refl">设置条件反射</a></li>
        <li><a href="${basePath()}/struts/debug#rf-alive">节点RF不休眠</a></li>
        <li><a href="${basePath()}/struts/debug#locate">定位设备</a></li>
        <li><a href="${basePath()}/struts/debug#sensitivity">灵敏度级别(振动传感器)</a></li>
        <li><a href="${basePath()}/struts/debug#setTargetHumidity">目标湿度(空调组)</a></li>
        <li><a href="${basePath()}/struts/debug#setTargetTemperature">目标温度(空调组)</a></li>
        <li><a href="${basePath()}/struts/debug#setSwitchState">开关状态(空调组)</a></li>
        <li><a href="${basePath()}/struts/debug#reboot">重启设备</a></li>
    </ul>
</div>

<div id="debug-panel" class="col-md-8">

<div id="modifyInterval">
    <form action="struts/devices/modify_interval" method="get">
        <input type="hidden" name="method" value="modifyInterval"/>

        <div class="form-group">
            <label>设备ID</label>
            <input ng-model="deviceId" type="text" name="deviceId" class="form-control"/>
        </div>

        <div class="form-group">
            <label>工作周期</label>
            <input type="text" name="interval" class="form-control"/>
        </div>

        <button class="btn btn-default" type="submit">修改工作周期</button>
    </form>
</div>

<hr/>

<div id="patrol-check-start">
    <form action="_url" method="get">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/patrol-check"/>
        <input type="hidden" name="method" value="start"/>

        <div class="form-group">
            <label>网关ID</label>
            <input ng-model="deviceId" type="text" class="form-control"/>
        </div>

        <div class="form-group">
            <label>工作周期</label>
            <input type="text" name="interval" class="form-control"/>
        </div>

        <button class="btn btn-default" type="submit">开启巡检</button>
    </form>
</div>

<hr/>

<div id="patrol-check-end">
    <form action="_url" method="get">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/patrol-check"/>
        <input type="hidden" name="method" value="end"/>

        <div class="form-group">
            <label>网关ID</label>
            <input ng-model="deviceId" type="text" class="form-control"/>
        </div>

        <button class="btn btn-default" type="submit">关闭巡检</button>
    </form>
</div>

<hr/>

<div id="set-default-parent">
    <form action="exists" method="get">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/default-parent/{{ parentId }}"/>

        <div class="form-group">
            <label>设备ID</label>
            <input ng-model="deviceId" type="text" class="form-control"/>
        </div>

        <div class="form-group">
            <label>默认父节点</label>
            <input class="form-control" ng-model="parentId" type="text" name="parentId"/>

            <p class="help-inline">无默认父节点为：65535 (0xFFFF)</p>
        </div>

        <button class="btn btn-default" type="submit">指定默认父节点</button>
    </form>
</div>

<hr/>

<div id="relay-suspend">
    <form action="exists" method="get">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/suspend"/>

        <div class="form-group">
            <label>设备ID</label>
            <input ng-model="deviceId" type="text" class="form-control"/>
        </div>

        <div class="form-group">
            <label class="radio-inline"><input type="radio" name="inOrOut" value="true"/>进入</label>
            <label class="radio-inline"><input type="radio" name="inOrOut" value="false"/>退出</label>
        </div>

        <button class="btn btn-default" type="submit">中继待机</button>
    </form>
</div>

<hr/>

<div id="available-parents">
    <form action="exists" method="get">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/available-parents"/>

        <div class="form-group">
            <label>设备ID</label>
            <input ng-model="deviceId" type="text" class="form-control"/>
        </div>

        <button class="btn btn-default" type="submit">查询可选父节点</button>
    </form>
</div>

<hr/>

<div id="turn-switch">
    <form action="exists" method="get">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/turnSwitch"/>

        <div class="form-group">
            <label>设备ID</label>
            <input ng-model="deviceId" type="text" class="form-control"/>
        </div>

        <div class="form-group">
            <label>路数</label>
            <select name="route" class="form-control">
                <option value="1">1 路</option>
                <option value="2">2 路</option>
                <option value="3">3 路</option>
                <option value="4">4 路</option>
                <option value="5">5 路</option>
                <option value="6">6 路</option>
            </select>
        </div>

        <div class="form-group">
            <label class="checkbox-inline">
                <input type="radio" name="onOrOff" value="true" checked="checked"> 开
            </label>
            <label class="checkbox-inline">
                <input type="radio" name="onOrOff" value="false"> 关
            </label>
        </div>

        <button class="btn btn-default" type="submit">开关</button>
    </form>
</div>
<hr/>

<div id="condition-refl">
    <form action="exists" method="get">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/condition-refl"/>

        <div class="form-group">
            <label>设备ID</label>
            <input ng-model="deviceId" type="text" class="form-control"/>
        </div>

        <div class="form-group">
            <label>路数</label>
            <select name="route" class="form-control">
                <option value="1">1 路</option>
                <option value="2">2 路</option>
                <option value="3">3 路</option>
                <option value="4">4 路</option>
                <option value="5">5 路</option>
                <option value="6">6 路</option>
            </select>
        </div>

        <div class="form-group">
            <label>子设备ID</label>
            <input name="subNodeId" type="text" class="form-control"/>
        </div>

        <div class="form-group">
            <label>监测指标ID</label>
            <input name="sensorId" type="text" class="form-control"/>
        </div>


        <div class="form-group">
            <label>低阈值</label>
            <input type="text" name="low" class="form-control"/>
        </div>

        <div class="form-group">
            <label>高阈值</label>
            <input type="text" name="high" class="form-control"/>
        </div>

        <div class="form-group">
            <label>动作</label>
            <input type="text" name="switchAction" class="form-control"/>
        </div>

        <button class="btn btn-default" type="submit">设置条件反射</button>
    </form>
</div>

<hr/>

<div id="rf-alive">
    <form action="exists" method="post">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/rf-alive"/>

        <div class="form-group">
            <label>设备ID</label>
            <input ng-model="deviceId" type="text" class="form-control"/>
        </div>

        <div class="form-group">
            <label class="checkbox-inline">
                <input type="radio" name="enable" value="true" checked="checked"> 开
            </label>
            <label class="checkbox-inline">
                <input type="radio" name="enable" value="false"> 关
            </label>
        </div>

        <button class="btn btn-default" type="submit">节点RF不休眠</button>
    </form>
</div>

<div id="locate">
    <form action="exists" method="post">
        <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/locate"/>

        <div class="form-group">
            <label>设备ID</label>
            <input ng-model="deviceId" type="text" name="deviceId" class="form-control"/>
        </div>

        <div class="form-group">
            <label>蜂鸣时间</label>
            <input type="text" name="interval" class="form-control"/>
        </div>

        <button class="btn btn-default" type="submit">定位设备</button>
    </form>
</div>
    <div id="reboot">
        <form action="exists" method="post">
            <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/restart"/>

            <div class="form-group">
                <label>设备ID</label>
                <input ng-model="deviceId" type="text" name="deviceId" class="form-control"/>
            </div>
            <button class="btn btn-default" type="submit">重启设备</button>
        </form>
    </div>
    <div id="sensitivity">
        <form action="struts/devices/sensitivity" method="get">
            <input type="hidden" name="method" value="sensitivity"/>

            <div class="form-group">
                <label>设备ID</label>
                <input ng-model="deviceId" type="text" name="deviceId" class="form-control"/>
            </div>

            <div class="form-group">
                <label>灵敏度级别</label>
                <input type="text" name="level" class="form-control"/>
            </div>

            <button class="btn btn-default" type="submit">修改灵敏度级别</button>
        </form>
    </div>
    <#--空调开关-->
    <div id="setSwitchState">
        <form action="exists" method="post">
            <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/setAirConditionerSwitchState"/>

            <div class="form-group">
                <label>设备ID</label>
                <input ng-model="deviceId" type="text" class="form-control"/>
            </div>

            <div class="form-group">
                <label class="checkbox-inline">
                    <input type="radio" name="switchState" value="1" checked="checked"> 开
                </label>
                <label class="checkbox-inline">
                    <input type="radio" name="switchState" value="0"> 关
                </label>
            </div>

            <button class="btn btn-default" type="submit">空调开关状态</button>
        </form>
    </div>

<#--空调湿度-->
    <div id="setTargetHumidity">
        <form action="exists" method="post">
            <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/setAirConditionerHumidity"/>

            <div class="form-group">
                <label>设备ID</label>
                <input ng-model="deviceId" type="text" class="form-control"/>
            </div>

            <div class="form-group">
                <label>目标湿度</label>
                <input type="text" name="targetHumidity" class="form-control"/>
            </div>

            <button class="btn btn-default" type="submit">设置目标湿度</button>
        </form>
    </div>

<#--空调温度-->
    <div id="setTargetTemperature">
        <form action="exists" method="post">
            <input type="hidden" name="_url" value="struts/devices/{{ deviceId }}/setAirConditionerTemperature"/>

            <div class="form-group">
                <label>设备ID</label>
                <input ng-model="deviceId" type="text" class="form-control"/>
            </div>

            <div class="form-group">
                <label>目标温度</label>
                <input type="text" name="targetTemperature" class="form-control"/>
            </div>

            <button class="btn btn-default" type="submit">设置目标温度</button>
        </form>
    </div>

</div>

</div>
</body>
</html>
