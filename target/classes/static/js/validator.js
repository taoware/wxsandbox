
$(document).ready(function() {
    $('#ads-form').bootstrapValidator({
        message: '内容为空',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '标题不能为空'
                    }
                }
            },
            description: {
                validators: {
                    notEmpty: {
                        message: '描述不能为空'
                    }
                }
            },
            file: {
                validators: {
                    notEmpty: {
                        message: '文件不能为空'
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


$(document).ready(function() {
    $('#ads-form').bootstrapValidator({
        message: '内容为空',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '标题不能为空'
                    }
                }
            },
            description: {
                validators: {
                    notEmpty: {
                        message: '描述不能为空'
                    }
                }
            },
            file: {
                validators: {
                    notEmpty: {
                        message: '文件不能为空'
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

$(document).ready(function() {
    $('#ads-form').bootstrapValidator({
        message: '内容为空',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '标题不能为空'
                    }
                }
            },
            description: {
                validators: {
                    notEmpty: {
                        message: '描述不能为空'
                    }
                }
            },
            file: {
                validators: {
                    notEmpty: {
                        message: '文件不能为空'
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

$(document).ready(function() {
    $('#text-form').bootstrapValidator({
        message: '内容为空',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            content: {
                validators: {
                    notEmpty: {
                        message: '文本内容不能为空'
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
