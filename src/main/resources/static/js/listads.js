$('#list_tabs a:first').click(function () {
    window.location.href = '/web/listads';
});
$('#list_tabs li:eq(1) a').click(function () {
    window.location.href = '/web/listdraw';
});
$('#list_tabs a:last').click(function () {
    window.location.href = '/web/listgame';
});

function getQueryString(activityId) {
    var reg = new RegExp("(^|&)" + activityId + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

window.onload = function () {
			 onload = list(1);
}

var result;
var totalpage;

var list = function (event) {
    $.ajax({
        headers: {
            Accept: "application/json; charset=utf-8"
        },
        type: "GET",
        url: "/activity?type=ad&offset=" + event + "&limit=5",
        dataType: "json",
        async: false,
        success: function (data) {

            result = data.list;

            totalpage = data.totalpage;

            $("table").html("<thead><tr><td>活动名称</td><td>开始/结束时间</td><td>参与人数</td><td>状态</td><td>操作</td><td>数据统计</td></tr></thead>");
            $("#pg_list").html("");
            for (n = 1; n <= totalpage; n++) {
                $("#pg_list").append(
                    $("<li onclick='list(" + n + ")'><a>" + n + "</a></li>")
                )
            }

            for (i = 0; i < data.list.length; i++) {
            	var disable = data.list[i].disable
                var activityId = data.list[i].id;
                var activityName = data.list[i].name;
                var activityCount = data.list[i].count;
                var startDate = data.list[i].startDate;
                var endDate = data.list[i].endDate;
                var statustext = data.list[i].statusText;
                var statusopen = '<a href="javascript:void(0);"  id="activityStatus" onclick="operate( ' + activityId +','+ event + ')">开启</a>';
                var statusclose = '<a href="javascript:void(0);"  id="activityStatus" onclick="operate( ' + activityId + ','+ event +')">关闭</a>';
                var statusinit = '';
                if(statustext=="已结束"){
                    statusinit = statusinit;
                }else{
                	  if (disable==true) {
		                    statusinit = statusopen;
		                }else{
		                    statusinit = statusclose;
		                }
                	}
                $("table").append(
                    $("<tr class='tbody' id=" + activityId + "><td>" +
                    activityName
                    + "</td><td>" +
                    startDate + '&nbsp;' + '&nbsp;' + endDate
                    + "</td><td>" +
                    activityCount
                    + "</td><td>" +
                    statustext
                    + "</td><td>" +
                    statusinit
                    + "</td><td>" +
                    '<a href="javascript:void(0);"  id="activityCount" onclick="check(' + activityId +')">查看统计</a>'
                    + "</td></tr> ")
                );

            }
            

            if (data.list.length < 5) {
                for (i = 0; i < (5 - data.list.length); i++) {
                    $("table").append("<tr class='tbody' id=" + activityId + "><td height=35;></td><td></td><td></td><td></td><td></td><td></td></tr>");
                }
            }

        }


    });
}

var operate = function (event,event2) {

    layer.config({
        extend: ['skin/seaning/style.css'],
        skin: 'layer-ext-seaning'
    });

    $("tr[id='" + event + "']").click(function () {
        layer.confirm('确定操作？', {
            btn: ['确认', '取消'], //按钮
            shade: false //不显示遮罩
        }, function () {
            $.ajax({
                url: '/activity/' + event + '/disable',
                type: 'GET',
                success: function () {
                    onload=list(event2);
                    
                }
            });
            layer.msg('已确定');
        }, function () {
            layer.msg('已取消', {shift: 0});
        });

    })
}

var check = function (event) {


    $("tr[id='" + event + "']").click(function () {
        $.ajax({
            url: '/activity/' + event + '/wcUsers',
            type: 'GET',
            success: function () {
                window.location.href = '/web/userads?activity_id=' + event;
            }

        });

    })
}




