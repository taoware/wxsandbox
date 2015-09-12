'use strict';

angular.module('sandboxApp')
    .controller('UserExpandInfoDetailController', function ($scope, $stateParams, UserExpandInfo, UserBasicInfo) {
        $scope.userExpandInfo = {};
        $scope.load = function (id) {
            UserExpandInfo.get({id: id}, function(result) {
              $scope.userExpandInfo = result;
            });
        };
        $scope.load($stateParams.id);
    });
