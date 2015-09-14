// jshint devel:true
console.log('artisan & bigezhang');


$(document).ready(function () {

    $(".sb-btn").addClass("disabled"); //绑定按钮禁止状态

    $("#clear-phone").click(function () {
        $("#phone").val('');
        $(this).removeClass("icon-error");
    });
    $("#clear-code").click(function () {
        $("#code").val('');
        $(this).removeClass("icon-error");
    });


});

// Firefox, Google Chrome, Opera, Safari, Internet Explorer from version 9
function OnInput(event) {

    var code_length = event.target.value.length;

    if (code_length == "6") {
        $(".sb-btn").removeClass("disabled"); //绑定按钮激活
        $("#clear-code").removeClass("icon-error");
    } else if (code_length > "6") {
        $("#clear-code").addClass("icon-error");
    }

    console.log(event.target.value.length);

}
// Internet Explorer
function OnPropChanged(event) {
    if (event.propertyName.toLowerCase() == "value") {
        var code_length = event.target.value.length;

        if (code_length == "6") {
            $(".sb-btn").removeClass("disabled");
        }
    }
}


//获取验证码
var InterValObj; //timer,控制时间
var count = 30; //获取验证码间隔时间
var curCount;//当前剩余秒数
var code = "";//验证码
var codeLength = 6;//验证码长度

$("#getcode").click(function () {

    curCount = count;
    var phone = $("#phone").val();
    var re = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
    if (!re.test(phone)) {
        $("#clear-phone").addClass("icon-error");
        console.log("error phone");
    }
    else {
        //产生提货码
        for (var i = 0; i < codeLength; i++) {
            code += parseInt(Math.random() * 9 + 1).toString();
        }
        console.log("right phone");

        $("#getcode").attr("disabled", "true");
        InterValObj = window.setInterval(SetRemainTime, 1000);//启动计时器，1s 执行一次

        //ajax
        $.ajax({
            headers: {
                "Accept": "text/plain;charset=utf-8",
                "Content-Type": "text/plain;charset=utf-8"
            },
            type: "GET",
            url: "http://bovps1.taoware.com/notify",
            dataType: "text",
            data: "mobile=" + phone + "&message=" + code,
            success: function (data) {

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {

            }
        });
    }
});

//timer 处理
function SetRemainTime() {
    if (curCount == 0) {
        window.clearInterval(InterValObj);//停止计时器
        $("#getcode").removeAttr("disabled");
        $("#getcode").text("重新发送");
        code = "";//清除验证码，不清楚，过时间后，输入收到的验证码依然有效
    }
    else {
        curCount--;
        $("#getcode").text("剩余" + curCount + "秒");
    }
}

$("#submit-btn").click(function () {
    var code_name = $("#code").val();
    if (code_name == code) {
        window.open("index.html");
    } else {
        window.open("404.html");
    }
});


//recharge

$("#recharge-btn").click(function () {
    var product_code = $("#product_code").val();
    $.ajax({
        type: "POST",
        url: "?",
        data: "product_code",
        dataType: "text",
        success: function (data) {

        },
        error: function (xml) {

        }

    })
});

//storelist
$('.ui-searchbar').tap(function () {
    $('.ui-searchbar-wrap').addClass('focus');
    $('.ui-searchbar-input input').focus();
});
$('.ui-searchbar-cancel').tap(function () {
    $('.ui-searchbar-wrap').removeClass('focus');
});