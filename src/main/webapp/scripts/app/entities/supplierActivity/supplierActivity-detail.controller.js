'use strict';

angular.module('sandboxApp')
    .controller('SupplierActivityDetailController', function ($scope, $stateParams, SupplierActivity, Supplier) {
        $scope.supplierActivity = {};
        $scope.load = function (id) {
            SupplierActivity.get({id: id}, function(result) {
              $scope.supplierActivity = result;
            });
        };
        $scope.load($stateParams.id);
    });
