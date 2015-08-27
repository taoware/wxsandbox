'use strict';

angular.module('sandboxApp')
    .factory('WCUser', function ($resource, DateUtils) {
        return $resource('api/wCUsers/:id', {}, {
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
