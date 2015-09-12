'use strict';

angular.module('sandboxApp')
    .factory('Goods', function ($resource, DateUtils) {
        return $resource('api/goodss/:id', {}, {
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
