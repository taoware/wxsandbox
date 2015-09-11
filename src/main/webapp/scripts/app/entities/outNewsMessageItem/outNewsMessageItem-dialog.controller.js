'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageItemDialogController', ['$scope', '$stateParams', '$modalInstance', 'entity', 'OutNewsMessageItem', 'OutNewsMessage',
        function ($scope, $stateParams, $modalInstance, entity, OutNewsMessageItem, OutNewsMessage) {
            $scope.outNewsMessageItem = entity;
            $scope.outNewsMessages = OutNewsMessage.query();
            $scope.load = function (id) {
                OutNewsMessageItem.get({id: id}, function (result) {
                    $scope.outNewsMessageItem = result;
                });
            };
            var onSaveFinished = function (result) {
                $scope.$emit('sandboxApp:outNewsMessageItemUpdate', result);
                $modalInstance.close(result);
            };
            $scope.save = function () {
                if ($scope.outNewsMessageItem.id != null) {
                    OutNewsMessageItem.update($scope.outNewsMessageItem, onSaveFinished);
                } else {
                    OutNewsMessageItem.save($scope.outNewsMessageItem, onSaveFinished);
                }
            };
            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            }

        }]);
