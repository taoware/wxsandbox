'use strict';

angular.module('sandboxApp')
    .controller('CUserDetailController', function ($scope, $stateParams, CUser, Coupon) {
        $scope.cUser = {};
        $scope.load = function (id) {
            CUser.get({id: id}, function(result) {
              $scope.cUser = result;
            });
        };
        $scope.load($stateParams.id);
    });
