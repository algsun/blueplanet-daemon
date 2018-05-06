<#--author: chenyaofei-->
<#--date :2016-07-04-->
<#include "common-tag.ftl">
<#include "common-head.ftl">
<!DOCTYPE html>
<html>
<head>
<@head "日志分析"/>
    <link rel="stylesheet" href="assets/select2/3.3.1/select2.css">
    <style type="text/css">
        .input-size {
            width: 300px;
        }

        .input-device-size {
            width: 150px;
        }
    </style>
</head>
<body>
<div class="container">
<#include "nav.ftl">
    <div class="row">
        <div class="col-md-12">
            <form class="form-horizontal" id="formData" style="margin-top: 50px;" action="struts/doLogAnalysis">
                <div class="form-group">
                    <label class="col-sm-2 control-label">开始时间</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control input-size" id="startTime" name="startTime"
                               onclick="WdatePicker({maxDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm:00'})">
                        <span id="dateErrorHelper" class="text-danger"><i class='glyphicon glyphicon-remove'></i>开始时间不能大于结束时间</span>
                    </div>

                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">结束时间</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control input-size" id="endTime" name="endTime"
                               onclick="WdatePicker({maxDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd HH:mm:00'})">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">站点</label>

                    <div class="col-sm-10">
                        <select class="js-example-basic-single form-control input-size" id="siteId" name="siteId">
                        <#if logicGroupList??>
                            <option value="">请选择</option>
                            <#list logicGroupList as logicGroup>
                                <option value="${logicGroup.siteId}">${logicGroup.logicGroupName}</option>
                            </#list>
                        <#else>
                            <option value="">无数据</option>
                        </#if>
                        </select>
                        <span id="siteErrorHelper" class="text-danger"><i class='glyphicon glyphicon-remove'></i>站点不能为空</span>
                    </div>

                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">设备号录入方式</label>

                    <div class="col-sm-10">
                        <input name="chooseType" type="radio" id="interval" value="interval" checked="checked"/>手动录入
                        <input name="chooseType" type="radio" id="continuity" value="continuity"
                               style="margin-left: 30px"/>范围录入
                    </div>
                </div>
                <div class="form-group" id="deviceIdsDiv">
                    <label class="col-sm-2 control-label">设备</label>

                    <div class="col-sm-10">
                        <select class="js-example-basic-multiple js-states form-control" multiple='' id="deviceIds"
                                name="deviceIds" style="width: 300px;">
                        </select>
                    </div>
                    <span id="deviceErrorHelper" style="margin-left: 210px" class="text-danger"><i
                            class='glyphicon glyphicon-remove'></i>设备不能为空</span>
                </div>
                <div class="form-group" id="deviceStartDiv">
                    <label class="col-sm-2 control-label">初始设备号</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control input-device-size" name="deviceStart">
                        <span id="deviceStartSpan" class="text-danger"></span>
                    </div>
                </div>
                <div class="form-group" id="deviceEndDiv">
                    <label class="col-sm-2 control-label">结束设备号</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control input-device-size" name="deviceEnd">
                        <span id="deviceEndSpan" class="text-danger"></span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button id="begin" type="button" class="btn btn-default">开始解析</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="myModal" class="modal fade bs-example-modal-sm" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content" style="width: 400px">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title" id="gridSystemModalLabel">温馨提示：</h3>
            </div>
            <div class="modal-body">
                执行后将占用很多资源，确定要继续吗?
            </div>
            <div class="modal-footer">
                <button id="toDoLogAnalysis" type="button" class="btn btn-primary" data-dismiss="modal">继续</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript" src="assets/my97-DatePicker/4.72/WdatePicker.js"></script>
<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="assets/select2/3.3.1/select2.min.js"></script>
<script type="text/javascript" src="assets/bootstrap/3.0.2/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(function () {
        $("#deviceIds").select2({
            placeholder: "请选择"
        });
        $("#deviceIds").select2({
            formatNoMatches: function () {
                return "暂无数据";
            }
        });
        $("#dateErrorHelper").hide();
        $("#siteErrorHelper").hide();
        $("#deviceErrorHelper").hide();
        $("#deviceStartSpan").hide();
        $("#deviceEndSpan").hide();
        $("#deviceStartDiv").hide();
        $("#deviceEndDiv").hide();

        //设备填数据
        $("#siteId").change(function () {
            var $this = $(this);
            var siteId = $this.val();
            if (siteId != null && siteId.trim() != "") {
                $.get("struts/allDevice?siteId=" + siteId, function (data) {
                    $("#deviceIds").empty();
                    $("li[class='select2-search-choice'").remove();
                    $.each(data.nodeInfoList, function (i, nodeInfo) {
                        $("#deviceIds").append("<option value=" + nodeInfo.nodeid.substr(8, 5) + ">" + nodeInfo.nodeid.substr(8, 5) + "</option>");
                    });
                });
            } else {
                $("#deviceIds").val("");
                $("#deviceIds").empty();
                $("li[class='select2-search-choice'").remove();
            }
        });

        // 选择设备录入方式
        $("input[name='chooseType']").click(function () {
            var $this = $(this);
            if ("continuity" == $this.val()) {
                $("#deviceIdsDiv").hide();
                $("#deviceStartDiv").show();
                $("#deviceEndDiv").show();
            } else {
                $("#deviceIdsDiv").show();
                $("#deviceStartDiv").hide();
                $("#deviceEndDiv").hide();
            }
        });

        //提交验证
        $("#begin").click(function () {
            //表单验证
            var $startTime = $("input[name='startTime']");
            var $endTime = $("input[name='endTime']");
            if ($startTime.val() > $endTime.val()) {
                $startTime.next().show();
                return false;
            } else {
                $startTime.next().hide();
            }

            if ("" == $("#siteId").val().trim()) {
                $("#siteId").next().show();
                return false;
            } else {
                $("#siteId").next().hide();
            }

            var chooseType = $("input[type='radio']:checked").val();
            var $deviceStartSpan = $("#deviceStartSpan");
            var $deviceEndSpan = $("#deviceEndSpan");

            if (chooseType == "continuity") {
                if (null == $("input[name='deviceStart']").val().trim() || "" == $("input[name='deviceStart']").val().trim()) {
                    $deviceStartSpan.empty();
                    $deviceStartSpan.append("<i class='glyphicon glyphicon-remove'></i>" + "设备初始序号不能为空");
                    $deviceStartSpan.show();
                    return false;
                } else if ($("input[name='deviceStart']").val().trim() % 1 != 0) {
                    $deviceStartSpan.empty();
                    $deviceStartSpan.append("<i class='glyphicon glyphicon-remove'></i>" + "设备初始序号必须由自然数组成");
                    $deviceStartSpan.show();
                    return false;
                } else {
                    $deviceStartSpan.empty();
                    $deviceStartSpan.hide();
                }
                if (null == $("input[name='deviceEnd']").val().trim() || "" == $("input[name='deviceEnd']").val().trim()) {
                    $deviceEndSpan.empty();
                    $deviceEndSpan.append("<i class='glyphicon glyphicon-remove'></i>" + "设备结束序号不能为空");
                    $deviceEndSpan.show();
                    return false;
                } else if ($("input[name='deviceEnd']").val().trim() % 1 != 0) {
                    $deviceEndSpan.empty();
                    $deviceEndSpan.append("<i class='glyphicon glyphicon-remove'></i>" + "设备结束序号必须由自然数组成");
                    $deviceEndSpan.show();
                    return false;
                } else {
                    $deviceEndSpan.empty();
                    $deviceEndSpan.hide()
                }
                if ($("input[name='deviceStart']").val().trim() > $("input[name='deviceEnd']").val().trim()) {
                    $deviceStartSpan.append("<i class='glyphicon glyphicon-remove'></i>" + "设备初始序号不能大于结束序号");
                    $deviceStartSpan.show();
                    return false;
                } else {
                    $deviceStartSpan.empty();
                    $deviceStartSpan.hide();
                }
            } else {
                if (null == $("#deviceIds").val() || "" == $("#deviceIds").val()) {
                    $("#deviceErrorHelper").show();
                    return false;
                } else {
                    $("#deviceErrorHelper").hide();
                }

            }
            $('#myModal').modal('show');
        });

        $("#toDoLogAnalysis").click(function(){
            $("#formData").submit();
        });
    });
</script>
</body>
</html>