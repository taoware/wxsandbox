'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageDetailController', function ($scope, $stateParams, OutNewsMessage, OutNewsMessageItem) {
        $scope.outNewsMessage = {};
        $scope.load = function (id) {
            OutNewsMessage.get({id: id}, function(result) {
              $scope.outNewsMessage = result;
            });
        };
        $scope.load($stateParams.id);
    });
