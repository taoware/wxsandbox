'use strict';

angular.module('sandboxApp')
    .controller('CouponBatchDetailController', function ($scope,$rootScope, $stateParams,entity, CouponBatch, NCoupon,SupplierActivity) {
        $scope.couponBatch = entity;
        $scope.load = function (id) {
            CouponBatch.get({id: id}, function(result) {
              $scope.couponBatch = result;
            });
        };
        $scope.load($stateParams.id);
        $rootScope.$on('sandboxApp:couponBatchUpdate',function(event,result){
            $scope.couponBatch = result;
        });
    });
