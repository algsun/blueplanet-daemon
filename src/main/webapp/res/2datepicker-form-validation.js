jQuery(function () {
    (function () {
        //验证开始时间和结束时间大小
        var $startDate = $("input[name='startDate']");
        var $endDate = $("input[name='endDate']");
        var $check = $startDate.popover({
            title:"提示",
            content:"开始时间不能大于结束时间",
            placement:'bottom',
            trigger:'manual'
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