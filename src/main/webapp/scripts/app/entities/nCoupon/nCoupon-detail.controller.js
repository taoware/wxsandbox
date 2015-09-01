'use strict';

angular.module('sandboxApp')
    .controller('NCouponDetailController', function ($scope,$rootScope, $stateParams, entity,NCoupon, CouponBatch) {
        $scope.nCoupon = entity;
        $scope.load = function (id) {
            NCoupon.get({id: id}, function(result) {
              $scope.nCoupon = result;
            });
        };
        $scope.load($stateParams.id);
        $rootScope.$on('sandboxApp:nCouponUpdate',function(event,result){
            $scope.nCoupon = result;
        });
    });
