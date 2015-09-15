'use strict';

angular.module('sandboxApp')
    .controller('SupplierActivityDialogController', ['$scope', '$stateParams', '$modalInstance', 'entity', 'SupplierActivity','Supplier','CouponBatch',
        function ($scope, $stateParams, $modalInstance, entity, SupplierActivity,Supplier,CouponBatch) {
            $scope.supplierActivity = entity;
            $scope.suppliers = Supplier.query();
            $scope.couponBatchs = CouponBatch.query();
            $scope.load = function (id) {
                SupplierActivity.get({id: id}, function (result) {
                    $scope.supplierActivity = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('sandboxApp:supplierActivityUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.supplierActivity.id != null) {
                    SupplierActivity.update($scope.supplierActivity, onSaveFinished);
                } else {
                    SupplierActivity.save($scope.supplierActivity, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
