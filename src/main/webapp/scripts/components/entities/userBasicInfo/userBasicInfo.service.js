'use strict';

angular.module('sandboxApp')
    .factory('UserBasicInfo', function ($resource, DateUtils) {
        return $resource('api/userBasicInfos/:id', {}, {
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
