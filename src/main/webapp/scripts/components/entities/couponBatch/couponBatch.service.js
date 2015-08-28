'use strict';

angular.module('sandboxApp')
    .factory('CouponBatch', function ($resource, DateUtils) {
        return $resource('api/couponBatchs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.beginDate = DateUtils.convertDateTimeFromServer(data.beginDate);
                    data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    return data;
                }
            },
            'update': {
                method:'PUT',
                transformRequest:function (data) {
                    data.beginDate = DateUtils.convertLocaleDateToServer(data.beginDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method:'POST',
                transformRequest:function (data) {
                    data.beginDate = DateUtils.convertLocaleDateToServer(data.beginDate);
                    return angular.toJson(data);
                }
            }
        });
    })

    .factory('CouponBatch',function($resource){
        return $resource('api/orders/q',{},{
            'query':{method:'GET'}
        });
    });
