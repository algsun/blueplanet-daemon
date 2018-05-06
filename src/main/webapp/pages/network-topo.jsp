<%@ page import="com.microwise.msp.hardware.businessbean.DeviceBean" %>
<%@ page import="java.util.List" %>
<%--
  网络拓扑图
  User: gaohui
  Date: 13-9-16
  Time: 下午1:32
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>网络拓扑图</title>
    <link rel="stylesheet" href="assets/bootstrap/3.0.2/css/bootstrap.min.css"/>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h1>网络拓扑图</h1>

            <form class="form-inline" role="form" action="network-topo" method="post">
                <div class="form-group">
                    <label>网关</label>
                </div>
                <div class="form-group">
                    <% List<DeviceBean> gateways = (List<DeviceBean>) request.getAttribute("gateways"); %>
                    <select id="gateways-select" class="form-control" name="deviceId">
                        <% for (DeviceBean gateway : gateways) { %>
                        <option value="<%= gateway.deviceid%>"
                                <% if(gateway.deviceid.equals(request.getParameter("deviceId"))){ %>
                                selected="selected"
                                <% } %>
                                >
                            <%= gateway.siteId%>-<%= gateway.deviceid.substring(8) %>
                        </option>
                        <% } %>
                    </select>
                </div>

                <button class="btn btn-default">查询</button>
            </form>

            <canvas id="topo" width="900" height="600"></canvas>
        </div>
    </div>
</div>

<script type="text/javascript" src="assets/jquery/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="assets/stringy/2.0.1/springy.js"></script>
<script type="text/javascript" src="assets/stringy/2.0.1/springyui.js"></script>

<script type="text/javascript">
    $(function () {
        var reloadChart = function () {

            var gatewayDeviceId = $('#gateways-select').val();
            if (!gatewayDeviceId) {
                return;
            }

            $.post('network-topo.json', {deviceId: gatewayDeviceId}, function (result) {
                var devices = result.devices;
                var routes = result.routes;

                var graph = new Springy.Graph();
                var nodes = {};
                $.each(routes, function (i, route) {
                    if (!nodes[route.source]) {
                        var label = route.source.substring(8);
                        var node = graph.newNode({label: label});
                        nodes[route.source] = node;
                    }
                    if (!nodes[route.target]) {
                        var label = route.target.substring(8);
                        var node = graph.newNode({label: label});
                        nodes[route.target] = node;
                    }
                    var sourceNode = nodes[route.source];
                    var targetNode = nodes[route.target];

                    graph.newEdge(sourceNode, targetNode);
                });

                $('#topo').springy({
                    graph: graph
                });
            });
        };

        reloadChart();
    });
</script>

</body>
</html>