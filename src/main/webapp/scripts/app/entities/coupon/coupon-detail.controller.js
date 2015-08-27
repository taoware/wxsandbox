'use strict';

angular.module('sandboxApp')
    .controller('CouponDetailController', function ($scope, $stateParams, Coupon) {
        $scope.coupon = {};
        $scope.load = function (id) {
            Coupon.get({id: id}, function(result) {
              $scope.coupon = result;
            });
        };
        $scope.load($stateParams.id);
    });
