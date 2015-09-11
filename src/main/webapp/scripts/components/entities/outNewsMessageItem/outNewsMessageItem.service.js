'use strict';

angular.module('sandboxApp')
    .factory('OutNewsMessageItem', function ($resource, DateUtils) {
        return $resource('api/outNewsMessageItems/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {
                method:'PUT',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': {
                method:'POST',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('OutNewsMessageItems',function($resource){
        return $resource('api/outNewsMessageItem//q',{},{
            'query':{method:'GET'}
        });
    });
