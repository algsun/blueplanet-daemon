<#include "common-tag.ftl">
<#include "common-head.ftl">
<#setting number_format="0.####">

<!DOCTYPE html>
<html>
<head>
<@head "Galaxy - Blueplanet Daemon 环境监控中间件"/>
    <meta http-equiv="refresh" content="5; URL=${basePath()}/struts/index">
    <style type="text/css">
        .jqstooltip {
            box-sizing: content-box;
        }
    </style>
<@linkTag "assets/common-css/0.1.2/common.min.css"></@linkTag>
</head>
<body>
<div class="container">

    <div class="row">
        <div class="col-md-12">


            <h2>Galaxy - Blueplanet Daemon 环境监控中间件</h2>

            <div>
                <span class="text-muted">页面每 5 秒刷新一次</span>
            </div>
            <div>
                <span>系统时间：${.now?string("yyyy年MM月dd日 HH:mm:ss")}</span>
                <span class="text-muted">（毫秒数：${.now?long}）</span>
                <span class="text-muted">，启动时间：${Application["app.startTime"]?string("yyyy年MM月dd日 HH:mm:ss")}</span>
                <span class="text-muted">，版本号：<#if svnRevision == 0>未知<#else>${svnRevision}</#if></span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4">

            <h3>队列信息</h3>
            <ul>
                <li>数据队列：${workQueueSize}
                </li>
                <li><a href="struts/threads">更多</a></li>
            </ul>

            <h3>统计信息</h3>
            <ul>
                <li>数据包次数：${packetWrites}
                </li>
                <li>数据包处理速度：${packetWritesPerSecond} 包/秒 <span class="text-muted"><br>(10秒计算一次)</span>
                </li>
            </ul>
            <table class="table-bordered table">
                <thead>
                <tr>
                    <th>时间</th>
                    <th>1分钟</th>
                    <th>5分钟</th>
                    <th>15分钟</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><strong>包数</strong></td>
                    <td>${packetWrites1Minute}</td>
                    <td>${packetWrites5Minute}</td>
                    <td>${packetWrites15Minute}</td>
                </tr>
                <tr>
                    <td><strong>速度 (包/秒)</strong></td>
                    <td>${(packetWrites1Minute / 60.0)?string("0.#")}</td>
                    <td>${(packetWrites5Minute / 300.0)?string("0.#")}</td>
                    <td>${(packetWrites15Minute / 900.0)?string("0.#")}</td>
                </tr>
                </tbody>
            </table>

            <div style="margin-top: 10px;">
            <span id="speedChart"
                  values="<#list packetWrites15MinuteQueue as count><#if count_index != 0>,</#if>${count}</#list>"></span>
            </div>
        </div>

        <div class="col-md-4">

            <h3>在线网关</h3>
            <h4>UDP</h4>
            <ul>
            <#if udpChannelAttributes?size == 0>
                <li>暂无网关</li>
            </#if>

            <#list udpChannelAttributes.entrySet() as sessionEntry>
                <@channelItem sessionEntry, udpNetInfos, siteNames/>
            </#list>
            </ul>


            <h4>TCP</h4>
            <ul>
            <#if tcpChannelAttributes?size == 0>
                <li>暂无网关</li>
            </#if>
            <#list tcpChannelAttributes.entrySet() as sessionEntry>
                <@channelItem sessionEntry, tcpNetInfos, siteNames/>
            </#list>
            </ul>
        </div>

        <div class="col-md-4">
            <h3>监听端口</h3>
            <h4>UPD</h4>
            <ul>
            <#list udpNetInfos as netinfo>
                <li>
                ${netinfo.lport}
                    <#if netinfo.siteId??>
                        <span class="text-muted" style="font-size: 0.6em;">=> 站点ID：${netinfo.siteId}
                            名称：${siteNames[netinfo.siteId]}</span>
                    </#if>
                </li>
            </#list>
            </ul>

            <h4>TCP</h4>
            <ul>
            <#list tcpNetInfos as netinfo>
                <li>${netinfo.lport}</li>
            </#list>
            </ul>

            <ul>
                <li><a href="struts/networks">管理</a></li>
            </ul>
            <div class="well well-sm">
                温馨提示：
                <p>V1.3协议的网关监听端口必需要绑定站点ID，V3协议的网关无此限制。</p>

                <p>站点ID/接入点号必需为8位数字。</p>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <hr>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <h3>工具</h3>
            <ul>
                <li><a href="struts/logs">数据包日志</a></li>
                <li><a href="struts/logs/files">日志下载</a></li>
                <li><a href="struts/parse">数据包解析</a></li>
                <li><a href="struts/debug">下行命令测试</a></li>
                <li><a href="struts/sensors">监测指标/传感量</a></li>
                <li></li>
                <li><a href="struts/networks">监听端口</a></li>
                <li><a href="struts/caches">缓存</a></li>
                <li><a href="network-topo">网络拓扑</a></li>
                <li><a href="struts/jobs">后台任务</a></li>
                <li><a href="status.json">状态</a></li>
                <li><a href="struts/kdj">kdj统计</a></li>
                <li><a href="struts/statistics/view">数据统计</a></li>
                <#if !(osName?contains("Windows"))>
                    <li><a href="struts/logAnalysis">日志分析</a></li>
                </#if>
            </ul>
        </div>
        <div class="col-md-6">
            <h3><a href="struts/api-doc">http 接口</a></h3>

            <h3>webservice</h3>
            <ul>
                <li><strong>hardware</strong></li>
                <li>${basePath()}/services/general <a href="${basePath()}/services/general?wsdl"
                                                      target="_blank">wsdl</a></li>
                <li>${basePath()}/services/netLink <a href="${basePath()}/services/netLink?wsdl"
                                                      target="_blank">wsdl</a></li>
                <li>${basePath()}/services/coefficient <a href="${basePath()}/services/coefficient?wsdl"
                                                          target="_blank">wsdl</a>
                </li>
                <li><strong>proxy</strong></li>
                <li>${basePath()}/services/realtimeData <a href="${basePath()}/services/realtimeData?wsdl"
                                                           target="_blank">wsdl</a>
                </li>
            </ul>
        </div>
    </div>

    <div class="row m-t-20">
        <div class="col-md-12 t-a-c text-muted">@2013&nbsp;&nbsp;版权所有:西安元智系统技术有限责任公司</div>
        <div class="col-md-12 t-a-c text-muted">知识产权:馆藏文物保存环境实时监测系统接口中间件v1.0</div>
    </div>

</div>

<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="assets/sparkline/2.1.2/jquery.sparkline.min.js"></script>
<script type="text/javascript">
    $(function () {
        // 网关详细
        $('a[data-detail]').click(function () {
            $(this).next().toggle();
            return false;
        });

        $('#speedChart').sparkline('html', {width: 300, height: 50});
    });
</script>
</body>
</html>

<#-- 站点ID绑定是否存在 -->
<#function siteIdExistsFromPort netinfos port>
    <#list netinfos as netinfo>
        <#if netinfo.lport == port>
            <#if netinfo.siteId??>
                <#return true>
            </#if>
        </#if>
    </#list>
    <#return false>
</#function>

<#-- 返回对应端口绑定的站点 -->
<#function siteIdFromPort netinfos port>
    <#list netinfos as netinfo>
        <#if netinfo.lport == port>
            <#return netinfo.siteId>
        </#if>
    </#list>

    <#return "">
</#function>

<#-- 每个通道的属性详细 -->
<#macro channelItem sessionEntry, netinfos, siteNames>
    <#assign channel = sessionEntry.value>
    <#assign hasSiteId = false>
    <#if channel.siteId??>
        <#assign siteId = channel.siteId>
        <#assign hasSiteId = true>
    </#if>

<li
        title="地址：${channel.remoteAddress.hostName}
端口：${channel.remoteAddress.port}
协议版本：${channel.version}
最后接收时间：${channel.lastTimestamp?string("yyyy-MM-dd HH:mm:ss")}
网关/根节点ID：<#if channel.gatewayId == 0>未知<#else>${channel.gatewayId}</#if>
站点ID/接入点号：${siteId!"未知"}
<#if hasSiteId>站点名称：${siteNames[siteId]} </#if>">

${sessionEntry.key} => ${channel.localAddress.port} <a data-detail href="#">详细</a>
    <ul style="display: none;">
        <li>地址：${channel.remoteAddress.hostName}</li>
        <li>端口：${channel.remoteAddress.port}</li>
        <li>协议版本：${channel.version}</li>
        <li>最后接收时间：${channel.lastTimestamp?string("yyyy-MM-dd HH:mm:ss")}</li>
        <li>网关/根节点ID：<#if channel.gatewayId == 0>未知<#else>${channel.gatewayId}</#if></li>
        <#if channel.version == 1>
            <#if siteIdExistsFromPort(netinfos, channel.localAddress.port)>
                <#assign bindSiteId = siteIdFromPort(netinfos, channel.localAddress.port)>
                <li>站点ID/接入点号：${bindSiteId!"未知"}</li>
                <li>站点名称：${siteNames[bindSiteId]}</li>
            <#else>
                <li><span class="text-danger" style="font-weight: bold;">v1.3 协议网关监听端口需要绑定站点</span></li>
            </#if>
        <#else>
            <li>站点ID/接入点号：${siteId!"未知"}</li>
            <#if hasSiteId>
                <li>站点名称：${siteNames[siteId]}</li>
            </#if>
        </#if>
    </ul>
</li>
</#macro>