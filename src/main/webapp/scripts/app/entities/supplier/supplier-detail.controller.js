'use strict';

angular.module('sandboxApp')
    .controller('SupplierDetailController', function ($scope, $stateParams, Supplier) {
        $scope.supplier = {};
        $scope.load = function (id) {
            Supplier.get({id: id}, function(result) {
              $scope.supplier = result;
            });
        };
        $scope.load($stateParams.id);
    });
