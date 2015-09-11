'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageItemDetailController', function ($scope,$rootScope, $stateParams, entity,OutNewsMessageItem,OutNewsMessage, UserInfo) {
        $scope.outNewsMessageItem = entity;
        $scope.load = function (id) {
            OutNewsMessageItem.get({id: id}, function(result) {
              $scope.outNewsMessageItem = result;
            });
        };
        $scope.load($stateParams.id);
        $rootScope.$on('sandboxApp:outNewsMessageItemUpdate',function(event,result){
            $scope.outNewsMessageItem = result;
        });
    });
