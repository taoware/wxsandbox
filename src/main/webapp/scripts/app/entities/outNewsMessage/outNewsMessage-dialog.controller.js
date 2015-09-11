'use strict'

angular.module('sandboxApp').controller('OutNewsMessageDialogController',
    ['$scope','$stateParams','$modalInstance','entity','OutNewsMessage','OutNewsMessageItem',
        function($scope,$stateParams,$modalInstance,entity,OutNewsMessage,OutNewsMessageItem) {
            $scope.outNewsMessage = entity;
            $scope.outNewsMessageItems = OutNewsMessageItem.query();
            $scope.load = function(id) {
                OutNewsMessage.get({id:id},function(result){
                    $scope.outNewsMessage = result;
                });
            };
            var onSaveFinished = function (result) {
                $scope.$emit('sandboxApp:outNewsMessage',result);
                $modalInstance.close(result);
            };
            $scope.save = function(){
                if ($scope.nCoupon.id != null){
                    OutNewsMessage.update($scope.outNewsMessage,onSaveFinished);
                } else {
                    OutNewsMessage.save($scope.outNewsMessage,onSaveFinished);
                }
            };
            $scope.clear = function(){
                $modalInstance.dismiss('cancel');
            }
        }]);
