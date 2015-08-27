'use strict';

angular.module('sandboxApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
