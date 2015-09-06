'use strict';

angular.module('sandboxApp').controller('NCouponDialogController',
    ['$scope','$stateParams','$modalInstance','entity','NCoupon','CouponBatch',
        function($scope,$stateParams,$modalInstance,entity,NCoupon,CouponBatch) {
            $scope.nCoupon = entity;
            $scope.couponBatchs = CouponBatch.query();
            $scope.load = function(id) {
                NCoupon.get({id:id},function(result){
                    $scope.nCoupon = result;
                });
            };
            var onSaveFinished = function (result) {
                $scope.$emit('sandboxApp:nCoupon',result);
                $modalInstance.close(result);
            };
            $scope.save = function(){
                if ($scope.nCoupon.id != null){
                    NCoupon.update($scope.nCoupon,onSaveFinished);
                } else {
                    NCoupon.save($scope.nCoupon,onSaveFinished);
                }
            };
            $scope.clear = function(){
                $modalInstance.dismiss('cancel');
            }
        }]);
