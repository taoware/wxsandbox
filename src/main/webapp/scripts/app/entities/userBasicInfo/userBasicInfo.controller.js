'use strict';

angular.module('sandboxApp')
    .controller('UserBasicInfoController', function ($scope, UserBasicInfo, ParseLinks) {
        $scope.userBasicInfos = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            UserBasicInfo.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userBasicInfos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            UserBasicInfo.get({id: id}, function(result) {
                $scope.userBasicInfo = result;
                $('#saveUserBasicInfoModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.userBasicInfo.id != null) {
                UserBasicInfo.update($scope.userBasicInfo,
                    function () {
                        $scope.refresh();
                    });
            } else {
                UserBasicInfo.save($scope.userBasicInfo,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            UserBasicInfo.get({id: id}, function(result) {
                $scope.userBasicInfo = result;
                $('#deleteUserBasicInfoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserBasicInfo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserBasicInfoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveUserBasicInfoModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userBasicInfo = {openId: null, mobile: null, status: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
