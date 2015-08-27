'use strict';

angular.module('sandboxApp')
    .controller('OutMessageDetailController', function ($scope, $stateParams, OutMessage, WCUser) {
        $scope.outMessage = {};
        $scope.load = function (id) {
            OutMessage.get({id: id}, function(result) {
              $scope.outMessage = result;
            });
        };
        $scope.load($stateParams.id);
    });
