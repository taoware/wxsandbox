'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageItemDetailController', function ($scope, $stateParams, OutNewsMessageItem) {
        $scope.outNewsMessageItem = {};
        $scope.load = function (id) {
            OutNewsMessageItem.get({id: id}, function(result) {
              $scope.outNewsMessageItem = result;
            });
        };
        $scope.load($stateParams.id);
    });
