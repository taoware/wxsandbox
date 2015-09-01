'use strict';

angular.module('sandboxApp')
    .factory('NCoupon', function ($resource, DateUtils) {
        return $resource('api/nCoupons/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdTime = DateUtils.convertDateTimeFromServer(data.createdTime);
                    data.modifedTime = DateUtils.convertDateTimeFromServer(data.modifedTime);
                    return data;
                }
            },
            'update': {
                method:'PUT' ,
                transformRequest: function(data) {
                    data.createdTime = DateUtils.convertDateTimeFromServer(data.createdTime);
                    data.modifedTime = DateUtils.convertDateTimeFromServer(data.modifedTime);
                    return angular.toJson(data);
                }
            },
            'save':{
                method:'POST',
                transformRequest:function(data) {
                    data.createdTime = DateUtils.convertDateTimeFromServer(data.createdTime);
                    data.modifedTime = DateUtils.convertDateTimeFromServer(data.modifedTime);
                    return angular.toJson(data);
                }
            }
        });
    })

    .factory('NCoupons',function($resource){
        return $resource('api/orders/q',{},{
            'query':{method:'GET'}
        });
    });
