'use strict';

angular.module('sandboxApp')
    .controller('CouponBatchDialogController', ['$scope', '$stateParams', '$modalInstance', 'entity', 'CouponBatch','SupplierActivity',
        function ($scope, $stateParams, $modalInstance, entity, CouponBatch,SupplierActivity) {
            $scope.couponBatch = entity;
            $scope.SupplierActivity = SupplierActivity.query();
            $scope.load = function (id) {
                CouponBatch.get({id: id}, function (result) {
                    $scope.couponBatch = result;
                });
            };
            var onSaveFinished = function (result) {
                $scope.$emit('sandboxApp:couponBatchUpdate', result);
                $modalInstance.close(result);
            };
            $scope.save = function () {
                if ($scope.couponBatch.id != null) {
                    CouponBatch.update($scope.couponBatch, onSaveFinished);
                } else {
                    CouponBatch.save($scope.couponBatch, onSaveFinished);
                }
            };
            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            }

        }]);
