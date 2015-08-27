'use strict';

angular.module('sandboxApp')
    .controller('WCUserDetailController', function ($scope, $stateParams, WCUser) {
        $scope.wCUser = {};
        $scope.load = function (id) {
            WCUser.get({id: id}, function(result) {
              $scope.wCUser = result;
            });
        };
        $scope.load($stateParams.id);
    });
