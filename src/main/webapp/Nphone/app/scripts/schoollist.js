/** * Created by bigezhang on 9/12/15. */$.ajax({    type: "GET",    url: "../data/school.json",    dataType: "json",    data: '',    success: function (data) {        console.log(data);        console.log(data[0]);        for (var i = 0; i < data.length; i++) {            var Id = data[i].id;            var School = data[i].school;            var Campus = data[i].campus;            $("#schoollist").append("<li class='ui-border-t'><a class='row' href='schoollist-detail.html?id="+Id+"' id='#schoolclick'><div class='ui-list-thumb'><span style='background-image:url(../images/machine.png)'></span></div><div class='ui-list-info school-info'><h4 class='ui-nowrap'>" + School + '('+Campus+')'+"</h4></div></a> </li>")        }    }});