'use strict';

angular.module('sandboxApp')
    .factory('UserInfo', function ($resource, DateUtils) {
        return $resource('api/userInfos/:id', {}, {
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
