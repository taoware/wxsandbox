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
            'update': { method:'PUT' }
        });
    });
