
    $('input[id=file]').change(function() {
        var path = $(this).val();
        path = path.split('\\');
        path = path[path.length-1];
        $('#uploadurl').val(path);
    });

    $(document).ready(function(){
        $(".main_side > ul > li > a").click(function() {
            $(this).addClass("selected").parents().siblings().find("a").removeClass("selected");
        });

    })


    $(document).ready(function() {
        $('#imgtext-form').bootstrapValidator({
            message: '内容为空',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                title: {
                    validators: {
                        notEmpty: {
                            message: '标题不能为空'
                        }
                    }
                },
                content: {
                    validators: {
                        notEmpty: {
                            message: '摘要不能为空'
                        }
                    }
                },
                url: {
                    validators: {
                        notEmpty: {
                            message: '内容链接不能为空'
                        }
                    }
                },
                picUrl: {
                    validators: {
                        notEmpty: {
                            message: '题图链接不能为空'
                        }
                    }
                },
                menuName: {
                    validators: {
                        notEmpty: {
                            message: '微信菜单不能为空'
                        }
                    }
                },
                startDate: {
                    validators: {
                        notEmpty: {
                            message: '文章内容不能为空'
                        }
                    }
                },

            }
        });
    });
