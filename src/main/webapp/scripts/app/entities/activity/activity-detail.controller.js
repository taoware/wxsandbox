'use strict';

angular.module('sandboxApp')
    .controller('ActivityDetailController', function ($scope, $stateParams, Activity, WCUser) {
        $scope.activity = {};
        $scope.load = function (id) {
            Activity.get({id: id}, function(result) {
              $scope.activity = result;
            });
        };
        $scope.load($stateParams.id);
    });
