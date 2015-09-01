'use strict';

angular.module('sandboxApp')
    .controller('SupplierDetailController', function ($scope,$rootScope, $stateParams, entity,Supplier) {
        $scope.supplier = entity;
        $scope.load = function (id) {
            Supplier.get({id: id}, function(result) {
              $scope.supplier = result;
            });
        };
        $scope.load($stateParams.id);

        $rootScope.$on('sandboxApp:supplierupdate',function(event,result){
            $scope.supplier = result;
        });
    });
