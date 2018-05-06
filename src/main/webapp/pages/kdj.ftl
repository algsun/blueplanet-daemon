<#--
@author gaohui
@date 2013-10-28
-->

<#include "common-tag.ftl">
<#include "common-head.ftl">

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="../assets/bootstrap/3.3.2/css/bootstrap.min.css"/>
<@head "kdj统计"/>
</head>
<body>


<div class="container">
<#--<#include "nav.ftl">-->

    <div class="row">
        <form class="form-inline" style="margin-top: 15px;" action="struts/mathKDJ">
            <div class="form-group">
                <label for="startDate">开始时间</label>
                <input id="startDate" type="text" class="form-control"
                       onclick="WdatePicker({maxDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})"
                       name="startDate"
                       value="${startDate?string('yyyy-MM-dd')}"/>
            </div>
            <div class="form-group">
                <label for="endDate">结束时间</label>
                <input id="endDate" type="text" class="form-control"
                       onclick="WdatePicker({maxDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})"
                       name="endDate"
                       value="${endDate?string('yyyy-MM-dd')}"/>
            </div>
            <button type="submit" class="btn btn-default" id="submit-btn">执行</button>
        </form>
    </div>
</div>
<script type="text/javascript" src="assets/my97-DatePicker/4.72/WdatePicker.js"></script>
<script type="text/javascript" src="assets/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="assets/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(function () {
        jQuery(function () {
            (function () {
                //验证开始时间和结束时间大小
                var $startDate = $("input[name='startDate']");
                var $endDate = $("input[name='endDate']");
                var $check = $startDate.popover({
                    title: "提示",
                    content: "开始时间不能大于结束时间",
                    placement: 'bottom',
                    trigger: 'manual'
                });

                $("#submit-btn").click(function () {
                    if ($startDate.val() > $endDate.val()) {
                        $check.popover('show');
                        return false;
                    }
                });

                //点击开始时间取消时间验证提示
                $startDate.click(function () {
                    $check.popover('hide');
                });

                //点击结束时间取消时间验证
                $endDate.click(function () {
                    $check.popover('hide');
                });
            })();
        });
    });
</script>
</body>
</html>