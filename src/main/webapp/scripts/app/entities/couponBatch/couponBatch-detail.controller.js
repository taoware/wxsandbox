'use strict';

angular.module('sandboxApp')
    .controller('CouponBatchDetailController', function ($scope, $stateParams, CouponBatch, SupplierActivity) {
        $scope.couponBatch = {};
        $scope.load = function (id) {
            CouponBatch.get({id: id}, function(result) {
              $scope.couponBatch = result;
            });
        };
        $scope.load($stateParams.id);
    });
