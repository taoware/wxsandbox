'use strict';

angular.module('sandboxApp').controller('OrderDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Order',
        function($scope, $stateParams, $modalInstance, entity, Order) {

        $scope.order = entity;
        $scope.load = function(id) {
            Order.get({id : id}, function(result) {
                $scope.order = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('sandboxApp:orderUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.order.id != null) {
                Order.update($scope.order, onSaveFinished);
            } else {
                Order.save($scope.order, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
