'use strict';

angular.module('sandboxApp')
    .controller('GoodsDetailController', function ($scope, $stateParams, Goods, CouponBatch) {
        $scope.goods = {};
        $scope.load = function (id) {
            Goods.get({id: id}, function(result) {
              $scope.goods = result;
            });
        };
        $scope.load($stateParams.id);
    });
