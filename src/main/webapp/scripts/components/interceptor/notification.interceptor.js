 'use strict';

angular.module('sandboxApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-sandboxApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-sandboxApp-params')});
                }
                return response;
            },
        };
    });