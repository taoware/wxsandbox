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
            'update': {
                method:'PUT',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save':{
                method:'POST',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })

    .factory('Suppliers',function($resource){
        return $resource('api/suppliers/q',{},{
            'query':{method:'GET'}
        });
    });
