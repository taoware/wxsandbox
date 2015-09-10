'use strict';

angular.module('sandboxApp')
    .controller('UserInfoController', function ($scope, UserInfo, ParseLinks) {
        $scope.userInfos = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            UserInfo.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userInfos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            UserInfo.get({id: id}, function(result) {
                $scope.userInfo = result;
                $('#saveUserInfoModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.userInfo.id != null) {
                UserInfo.update($scope.userInfo,
                    function () {
                        $scope.refresh();
                    });
            } else {
                UserInfo.save($scope.userInfo,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            UserInfo.get({id: id}, function(result) {
                $scope.userInfo = result;
                $('#deleteUserInfoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserInfo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserInfoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveUserInfoModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userInfo = {openId: null, nickname: null, country: null, city: null, province: null, mobile: null, name: null, college: null, specialty: null, th: null, gender: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
