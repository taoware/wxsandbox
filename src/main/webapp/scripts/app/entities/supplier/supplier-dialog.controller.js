'use strict';

angular.module('sandboxApp')
    .controller('SupplierDialogController',['$scope','$stateParams','$modalInstance','entity','Supplier',
    function($scope,$stateParams,$modalInstance,entity,Supplier){

        $scope.supplier = entity;
        $scope.load = function(id) {
            Supplier.get({id:id},function(result){
                $scope.supplier = result;
            });
        };

        var onSaveFinished = function(result) {
            $scope.$emit('sandboxApp:supplierUpdate',result);
            $modalInstance.close(result);
        };

        $scope.save = function(){
            if($scope.supplier.id != null){
                Supplier.update($scope.supplier,onSaveFinished);
            } else {
                Supplier.save($scope.supplier,onSaveFinished);
            }
        };

        $scope.clear = function(){
            $modalInstance.dismiss('cancel');
        }
}]);
