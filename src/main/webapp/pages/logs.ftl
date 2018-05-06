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
    <style type="text/css">
        #output {
            border: 2px black solid;
            overflow: auto;
            max-height: 600px;
            padding: 10px;
        }

        /* 关灯 */
        #output.lightOff{
            color: white;
            background-color: #777;
        }

        #output li {
            white-space: nowrap;
            list-style: none;
            font-size: 13px;
            /* 等宽字体 */
            font-family: Monaco,Menlo,Consolas,"Courier New",monospace;
        }

        #output li:hover{
            background-color: #ddd;
        }


        /* 日志级别 */

        /* error */
        #output li.level-200{
            color: red;
        }
    </style>
</head>
<body>


<div class="container">
<#include "nav.ftl">

    <div class="row">
        <div class="col-md-12">
            <h3>数据包日志</h3>

            <p id="output"></p>
            <div>

                <div class="form-inline" role="form">
                    <div class="form-group">
                        <label>网关</label>
                    </div>
                    <div class="form-group">
                        <select id="gateway" class="form-control">
                            <option value="-1">全部</option>
                            <#list gateways as gateway>
                                <option value="${gateway}">${gateway}</option>
                            </#list>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>设备</label>
                    </div>
                    <div class="form-group">
                        <select id="deviceId" class="form-control">
                            <option value="-1">全部</option>
                            <#list deviceIds as deviceId>
                                <option value="${deviceId}">${deviceId}</option>
                            </#list>
                        </select>
                    </div>


                    <button id="lightToggle" class="btn btn-default">
                        <i class="glyphicon glyphicon-eye-open"></i> 关灯
                    </button>

                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn btn-primary">
                            <i class="glyphicon glyphicon-pause"></i>
                            <input type="radio" name="pauseOrStart" id="pauseRefreshButton">
                            暂停
                        </label>
                        <label class="btn btn-primary">
                            <i class="glyphicon glyphicon-play"></i>
                            <input type="radio" name="pauseOrStart" checked="checked" id="startRefreshButton">
                            继续
                        </label>
                    </div>

                </div>
            </div>
            <p class="text-default">每5秒加载一次</p>
        </div>
    </div>
</div>

<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="assets/bootstrap/3.0.2/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(function () {
        (function(){

            /** 暂时注释 @gaohui
            var tooltipHook;
            $('body').on('mouseenter', '#output li', function(){
                var $this = $(this);
                clearTimeout(tooltipHook);
                tooltipHook = setTimeout(function(){
                    console.log('tooltip');
                    $this.popover({
                        title: 'hello',
                        content: 'world',
                        trigger: 'manual',
                        placement: 'left'
                    }).popover('show');
                }, 2000);
            });
            $('body').on('mouseleave', '#output li', function(){
                clearTimeout(tooltipHook);
            });
            **/



            var $output = $("#output");
            var fetchLogs = function () {
                var gateway = $('#gateway').val();
                var deviceId = $('#deviceId').val();

                $.get("struts/logs.json", function (result) {
                    $output.empty();
                    var lines = '';
                    $.each(result, function (_, log) {
                        if(gateway !== "-1"){
                            if(log.gateway !== gateway){
                               return;
                            }
                        }

                        if(deviceId !== "-1"){
                            if(log.deviceId !== deviceId){
                                return;
                            }
                        }

                        // 空格转义
                        var line = log.rawLog.replace(/ /g, '&nbsp;');
                        lines += '<li class="level-' + log.level + '" '
                                + 'data-gateway="' + log.gateway + '" '
                                + 'data-deviceId="' + log.deviceId + '" '
                                + 'data-packet="' + log.packet + '" '
                                + '>'
                                + line
                                + '</li>';
                    });
                    $output.append(lines);
                    $output.scrollTop($output[0].scrollHeight);
                });
            };
            fetchLogs();

            var hook = setInterval(fetchLogs, 5000);
            $('#pauseRefreshButton').change(function(){
                clearInterval(hook);
            });

            $('#startRefreshButton').change(function(){
                hook = setInterval(fetchLogs, 5000);
            });

            $('select[name="gateway"], select[name="deviceId"]').change(function(){
                fetchLogs();
            });
        })();

        (function(){
            $('#lightToggle').click(function(){
                $('#output').toggleClass('lightOff');
            });
        })();
    });
</script>
</body>
</html>