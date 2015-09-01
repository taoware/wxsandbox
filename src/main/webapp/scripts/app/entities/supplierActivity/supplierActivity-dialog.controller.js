'use strict';

angular.module('sandboxApp')
    .controller('SupplierActivityDialogController', ['$scope', '$stateParams', '$modalInstance', 'entity', 'SupplierActivity',
        function ($scope, $stateParams, $modalInstance, entity, SupplierActivity) {
            $scope.supplierActivity = entity;
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
