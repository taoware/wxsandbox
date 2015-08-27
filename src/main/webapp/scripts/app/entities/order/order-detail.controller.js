'use strict';

angular.module('sandboxApp')
    .controller('OrderDetailController', function ($scope, $rootScope, $stateParams, entity, Order) {
        $scope.order = entity;
        $scope.load = function (id) {
            Order.get({id: id}, function(result) {
                $scope.order = result;
            });
        };
        //add information modal
       /* $scope.clickinfo = function(id){
            Order.get({id:id},function(result){
                $scope.order = result;
                $('#information').modal('show');
            });
        };*/
        $rootScope.$on('sandboxApp:orderUpdate', function(event, result) {
            $scope.order = result;
        });
    });
