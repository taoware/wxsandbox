/** * Created by bigezhang on 9/12/15. *///获取url后的参数，参数值中有等于号的情况(=)var url = location.search; //获取url中"?"符后的字串var theRequest = new Object();if (url.indexOf("?") != -1) {    var str = url.substr(1);    if (str.indexOf("&") != -1) {       var strs = str.split("&");        for (var i = 0; i < strs.length; i++) {            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);        }    } else {        var key = str.substring(0,str.indexOf("="));        var value = str.substr(str.indexOf("=")+1);        theRequest[key] = decodeURI(value);    }}console.log(theRequest);console.log(value);//**$.ajax({    type: "GET",    url: "../data/schooldetail.json",    dataType: "json",    data: '',    success: function (data) {        console.log(data);        console.log(data[0]);        for (var i = 0; i < data.length; i++) {            var Id = data[i].id;            var Number = data[i].number;            var Address = data[i].address;            $("#schoollist-detail").append("<li class='ui-border-t'><div class='ui-avatar'><span style='background-image:url(../images/machine.png)'></span></div><div class='ui-list-info'><h4 class='ui-nowrap'>NO." + Number +"</h4><p class='ui-wrap'>"+ Address +"</p></a> </li>")        }    }});