'use strict';

angular.module('sandboxApp')
    .factory('SupplierActivity', function ($resource, DateUtils) {
        return $resource('api/supplierActivitys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.beginDate = DateUtils.convertDateTimeFromServer(data.beginDate);
                    data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    data.createdTime = DateUtils.convertDateTimeFromServer(data.createdTime);
                    data.modifiedTime = DateUtils.convertDateTimeFromServer(data.modifiedTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
