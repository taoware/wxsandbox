'use strict';

angular.module('sandboxApp')
    .factory('Coupon', function ($resource, DateUtils) {
        return $resource('api/coupons/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
