'use strict';

angular.module('sandboxApp')
    .controller('NCouponDetailController', function ($scope, $stateParams, NCoupon, CouponBatch) {
        $scope.nCoupon = {};
        $scope.load = function (id) {
            NCoupon.get({id: id}, function(result) {
              $scope.nCoupon = result;
            });
        };
        $scope.load($stateParams.id);
    });
