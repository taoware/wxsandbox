'use strict';

angular.module('sandboxApp')
    .factory('Order', function ($resource, DateUtils) {
        //http://vps1.taoware.com/activity?type=ad
        //api/orders/:id
        return $resource('api/orders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    console.log(data);
                    data = angular.fromJson(data);
                    data.myDate = DateUtils.convertLocaleDateFromServer(data.myDate);
                    data.myDateTime = DateUtils.convertDateTimeFromServer(data.myDateTime);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.myDate = DateUtils.convertLocaleDateToServer(data.myDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.myDate = DateUtils.convertLocaleDateToServer(data.myDate);
                    return angular.toJson(data);
                }
            }
        });
    })

    .factory('Orders', function ($resource) {
        //api/orders/q
        return $resource('api/orders/q', {}, {
            'query': { method: 'GET'}
        });
    });
