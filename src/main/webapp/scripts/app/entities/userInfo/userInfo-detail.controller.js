'use strict';

angular.module('sandboxApp')
    .controller('UserInfoDetailController', function ($scope, $stateParams, UserInfo) {
        $scope.userInfo = {};
        $scope.load = function (id) {
            UserInfo.get({id: id}, function(result) {
              $scope.userInfo = result;
            });
        };
        $scope.load($stateParams.id);
    });
