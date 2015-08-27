'use strict';

angular.module('sandboxApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


