$('#list_tabs a').click(function (e) {
    e.preventDefault()
    $(this).tab('show')
})

$(document).ready(function () {
    $(".main_side > ul > li > a").click(function () {
        $(this).addClass("selected").parents().siblings().find("a").removeClass("selected");
    });
})

function getQueryString(activityId) {
    var reg = new RegExp("(^|&)" + activityId + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}
function getQueryString(activityName) {
    var reg = new RegExp("(^|&)" + activityName + "=([^&]*)(&|$)", "i");
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

        url: "http://vps1.taoware.com/activity/" + getQueryString("activity_id") + "/wcUsers?offset=" + event + "&limit=5",
        dataType: "json",
        success: function (data) {


            result = data;

            totalpage = data.totalpage;

            $("table").html("<thead><tr><td>微信昵称</td><td>手机号</td><td>城市</td><td>性别</td></tr></thead>");
            $("#pg_list").html("");
            for (n = 1; n <= totalpage; n++) {
                $("#pg_list").append(
                    $("<li onclick='list(" + n + ")'><a>" + n + "</a></li>")
                )
            }

            for (j = 0; j < data.list.length; j++) {
                var mobile = data.list[j].mobile;
                var nickname = data.list[j].nickname;
                var province = data.list[j].province;
                var sex = data.list[j].sex;
                var mobileinit = '';

                if(mobile == null ){
                    mobileinit = mobileinit;
                }else{
                    mobileinit = mobile;
                }

                $("table").append(
                    $("<tr class='tbody' id=" + getQueryString("activity_id") + "><td>" +
                    nickname
                    + "</td><td>" +
                    mobileinit
                    + "</td><td>" +
                    province
                    + "</td><td>" +
                    sex
                    + "</td></tr>")
                );


            }

            if (data.list.length < 5) {
                for (i = 0; i < (5 - data.list.length); i++) {
                    $("table").append("<tr class='tbody'><td height=35;></td><td></td><td></td><td></td>");
                }
            }

        }

    });

}
