'use strict';

angular.module('sandboxApp')
    .controller('UserBasicInfoDetailController', function ($scope, $stateParams, UserBasicInfo) {
        $scope.userBasicInfo = {};
        $scope.load = function (id) {
            UserBasicInfo.get({id: id}, function(result) {
              $scope.userBasicInfo = result;
            });
        };
        $scope.load($stateParams.id);
    });
