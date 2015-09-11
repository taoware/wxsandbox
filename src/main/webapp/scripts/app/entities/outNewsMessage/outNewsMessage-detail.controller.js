'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageDetailController', function ($scope, $rootScope,$stateParams, entity,OutNewsMessage, OutNewsMessageItem) {
        $scope.outNewsMessage = entity;
        $scope.load = function (id) {
            OutNewsMessage.get({id: id}, function(result) {
              $scope.outNewsMessage = result;
            });
        };
        $scope.load($stateParams.id);
        $rootScope.$on('sandboxApp:outNewsMessageUpdate',function(event,result){
            $scope.outNewsMessage = result;
        });
    });
