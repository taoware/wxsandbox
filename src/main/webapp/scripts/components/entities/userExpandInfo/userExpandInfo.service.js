'use strict';

angular.module('sandboxApp')
    .factory('UserExpandInfo', function ($resource, DateUtils) {
        return $resource('api/userExpandInfos/:id', {}, {
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
