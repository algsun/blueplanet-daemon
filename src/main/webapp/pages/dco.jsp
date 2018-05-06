<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">

    <meta charset="utf-8">
    <title>下行命令测试</title>
    <link rel="stylesheet" href="assets/bootstrap/3.0.2/css/bootstrap.min.css"/>
    <script type="text/javascript" src="assets/jquery/jquery-1.6.4.min.js"></script>
    <script type="text/javascript">
        function modifyInterval(ele) {
            var $this = $(ele);
            var $output = $this.parents('form').siblings('.output');
            $output.empty();
            var param = $this.parents('form').serialize();
            $.get('servlet/DcoServlet?method=modifyInterval&' + param, function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });
            return false;
        }
        function pollingOpen(ele) {
            var $this = $(ele);
            var $output = $this.parent().siblings('.output');
            $output.empty();
            var $form = $this.parent();
            var deviceId = $form.find("[name='deviceId']").val();
            var interval = $form.find("[name='interval']").val();
            $.get('struts/devices/' + deviceId + '/patrol-check', {method: 'start', interval: interval}, function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });
            return false;
        }
        function pollingClose(ele) {
            var $this = $(ele);
            var $output = $this.parent().siblings('.output');
            $output.empty();
            var $form = $this.parent();
            var deviceId = $form.find("[name='deviceId']").val();
            $.get('struts/devices/' + deviceId + '/patrol-check', {method: 'end'}, function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });
            return false;
        }

        function setDefaultParent(ele) {
            var $this = $(ele);
            var $output = $this.parent().siblings('.output');
            $output.empty();
            var $form = $this.parent();
            var deviceId = $form.find("[name='deviceId']").val();
            var parentId = $form.find("[name='parentId']").val();

            $.get('struts/devices/' + deviceId + '/default-parent/' + parentId, function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });
            return false;
        }

        function queryAvailableParents(ele) {
            var $this = $(ele);
            var $output = $this.parent().siblings('.output');
            $output.empty();
            var $form = $this.parent();
            var deviceId = $form.find("[name='deviceId']").val();
            var parentId = $form.find("[name='parentId']").val();

            $.get('struts/devices/' + deviceId + '/available-parents', function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });
            return false;
        }

        function setConditionRefl(event, ele){
            var $this = $(ele);
            var $output = $this.parent().siblings('.output');
            $output.empty();
            var $form = $this.parents('form');
            var deviceId = $form.find("[name='deviceId']").val();
            var params = $form.serialize();

            $.get('struts/devices/' + deviceId + '/condition-refl?' +params , function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });

            event.preventDefault();
            return false;
        }

        function turnSwitch(ele) {
            var $this = $(ele);
            var $output = $this.parent().siblings('.output');
            $output.empty();
            var $form = $this.parent();
            var deviceId = $form.find("[name='deviceId']").val();
            var route = $form.find("[name='route']").val();
            var params = $form.serialize();

            $.get('struts/devices/' + deviceId + '/turnSwitch?' + params, function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });
            return false;
        }

        function restart(ele) {
            var $this = $(ele);
            var $output = $this.parent().siblings('.output');
            $output.empty();
            var $form = $this.parent();
            var deviceId = $form.find("[name='deviceId']").val();

            $.get('struts/devices/' + deviceId + '/restart', function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });
            return false;
        }

        function suspend(ele, inOrOut) {
            var $this = $(ele);
            var $output = $this.parent().siblings('.output');
            $output.empty();
            var $form = $this.parent();
            var deviceId = $form.find("[name='deviceId']").val();

            $.get('struts/devices/' + deviceId + '/suspend', {inOrOut: inOrOut}, function (result) {
                $output.append(result.success ? "发送成功<br>" : "发送失败<br>")
                        .append(result.msg);
            });
            return false;
        }
    </script>

</head>

<body>
<div class="container">
<div class="row">
    <div class="col-md-6">
        <input id="intervalURL" type="text"
               value="servlet/DcoServlet?method=modifyInterval&deviceId=3101010100256&interval=600"
               class="form-control" disabled="disabled"/>

        <form class="form-horizontal" style="margin-top: 10px;">
            <div class="form-group">
                <label class="col-md-2 control-label">设备ID</label>

                <div class="col-md-10">
                    <input type="text" name="deviceId" class="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">工作周期</label>

                <div class="col-md-10">
                    <input type="text" name="interval" class="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-2"></div>
                <div class="col-md-10">
                    <button id="btn1" class="btn btn-default" type="button" onclick="modifyInterval(this);">修改工作周期
                    </button>
                </div>
            </div>
        </form>

        <p class="output"></p>
    </div>

    <div class="col-md-6">

        <input id="pollingOpenURL" type="text"
               value="struts/devices/3101010100001/patrol-check?method=start&interval=60"
               class="form-control" disabled="disabled"/>

        <form class="form-inline" style="margin-top: 10px;">
            <div class="form-group">
                <label>设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="deviceId" class="form-control"/>
            </div>
            <div class="form-group">
                <label>工作周期</label>
            </div>
            <div class="form-group">
                <input type="text" name="interval" class="form-control"/>
            </div>

            <button id="btn2" class="btn btn-default" type="button" onclick="pollingOpen(this);">开启全网巡检</button>
        </form>

        <p class="output"></p>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <input id="pollingCloseURL" type="text"
               value="struts/devices/3101010100001/patrol-check?method=end"
               class="form-control" disabled="disabled"/>

        <form class="form-inline" style="margin-top: 10px;">
            <div class="form-group">
                <label>设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="deviceId" class="form-control"/>
            </div>

            <button id="btn3" class="btn btn-default" type="button" onclick="pollingClose(this);">退出全网巡检</button>
        </form>

        <p class="output"></p>
    </div>
    <div class="col-md-6">
        <input type="text" value="struts/devices/6101150100401/default-parent/401"
               class="form-control" disabled="disabled"/>

        <form class="form-inline" style="margin-top: 10px;">
            <div class="form-group">
                <label>设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="deviceId" class="form-control"/>
            </div>
            <div class="form-group">
                <label>默认父节点</label>
            </div>
            <div class="form-group">
                <input type="text" name="parentId" class="form-control"/>
            </div>
            <div class="form-group">
                <span class="help-inline">无默认父节点为：65535 (0xFFFF)</span>
            </div>

            <button class="btn btn-default" type="button" onclick="setDefaultParent(this);">设置默认父节点</button>
        </form>

        <p class="output"></p>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <input type="text" value="struts/devices/6101150100401/restart" class="form-control" disabled="disabled"/>


        <form class="form-inline" style="margin-top: 10px;">
            <div class="form-group">
                <label>设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="deviceId" class="form-control"/>
            </div>

            <button class="btn btn-default" type="button" onclick="restart(this);">重启</button>
        </form>
        <p class="output"></p>
    </div>
    <div class="col-md-6">
        <input type="text" value="struts/devices/6101150100401/suspend?inOrOut=true" class="form-control"
               disabled="disabled"/>


        <form class="form-inline" style="margin-top: 10px;">
            <div class="form-group">
                <label>设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="deviceId" class="form-control"/>
            </div>

            中继
            <button class="btn btn-default" type="button" onclick="suspend(this, true);">进入待机</button>
            <button class="btn btn-default" type="button" onclick="suspend(this, false);">退出待机</button>
        </form>
        <p class="output"></p>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <input type="text" value="struts/devices/6101150100401/available-parents" class="form-control"
               disabled="disabled"/>


        <form class="form-inline" style="margin-top: 10px;">
            <div class="form-group">
                <label>设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="deviceId" class="form-control"/>
            </div>

            <button class="btn btn-default" type="button" onclick="queryAvailableParents(this);">查询可选父节点</button>
        </form>
        <p class="output"></p>
    </div>
    <div class="col-md-6">
        <input type="text" value="struts/devices/6101150100401/turnSwitch" class="form-control" disabled="disabled"/>


        <form class="form-inline" style="margin-top: 10px;">
            <div class="form-group">
                <label>设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="deviceId" class="form-control"/>
            </div>

            <div class="form-group">
                <label>路数</label>
            </div>
            <div class="form-group">
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

            <button class="btn btn-default" type="button" onclick="turnSwitch(this);">开关</button>
        </form>
        <p class="output"></p>
    </div>
</div>

<div class="row">
    <form class="col-md-6">
        <input type="text" class="form-control" disabled="disabled"/>

        <div class="form-inline">

            <div class="form-group">
                <label>设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="deviceId" class="form-control"/>
            </div>

            <div class="form-group">
                <label>路数</label>
            </div>
            <div class="form-group">
                <select name="route" class="form-control">
                    <option value="1">1 路</option>
                    <option value="2">2 路</option>
                    <option value="3">3 路</option>
                    <option value="4">4 路</option>
                    <option value="5">5 路</option>
                    <option value="6">6 路</option>
                </select>
            </div>
        </div>

        <div class="form-inline">
            <div class="form-group">
                <label>子设备ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="subNodeId" class="form-control"/>
            </div>

            <div class="form-group">
                <label>监测指标ID</label>
            </div>
            <div class="form-group">
                <input type="text" name="sensorId" class="form-control"/>
            </div>
        </div>

        <div class="form-inline">
            <div class="form-group">
                <label>低阈值</label>
            </div>
            <div class="form-group">
                <input type="text" name="low" class="form-control"/>
            </div>

            <div class="form-group">
                <label>高阈值</label>
            </div>
            <div class="form-group">
                <input type="text" name="high" class="form-control"/>
            </div>

        </div>

        <div class="form-inline">
            <div class="form-group">
                <label>动作</label>
            </div>
            <div class="form-group">
                <input type="text" name="switchAction" class="form-control"/>
            </div>
            <button class="btn btn-default" type="submit" onclick="setConditionRefl(arguments[0], this);">设置条件反射</button>
        </div>

        <p class="output"></p>

    </form>

    <div class="col-md-6"></div>

</div>
</div>
</body>
</html>
