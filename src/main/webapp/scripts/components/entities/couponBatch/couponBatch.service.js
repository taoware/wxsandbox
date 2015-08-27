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
            'update': { method:'PUT' }
        });
    });
