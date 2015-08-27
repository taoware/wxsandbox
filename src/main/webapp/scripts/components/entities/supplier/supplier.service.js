'use strict';

angular.module('sandboxApp')
    .factory('Supplier', function ($resource, DateUtils) {
        return $resource('api/suppliers/:id', {}, {
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
