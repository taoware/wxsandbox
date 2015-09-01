'use strict';

angular.module('sandboxApp')
    .controller('SupplierActivityDetailController', function ($scope, $stateParams, SupplierActivity,entity, Supplier) {
        $scope.supplierActivity = entity;
        $scope.load = function (id) {
            SupplierActivity.get({id: id}, function(result) {
              $scope.supplierActivity = result;
            });
        };
        $scope.load($stateParams.id);

        $rootScope.$on('sandboxApp:supplierActivityUpdate',function(event,result){
            $scope.supplierActivity = result;
        });
    });
