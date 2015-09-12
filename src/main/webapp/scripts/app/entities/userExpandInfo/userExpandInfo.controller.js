'use strict';

angular.module('sandboxApp')
    .controller('UserExpandInfoController', function ($scope, UserExpandInfo, UserBasicInfo, ParseLinks) {
        $scope.userExpandInfos = [];
        $scope.userbasicinfos = UserBasicInfo.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            UserExpandInfo.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userExpandInfos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            UserExpandInfo.get({id: id}, function(result) {
                $scope.userExpandInfo = result;
                $('#saveUserExpandInfoModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.userExpandInfo.id != null) {
                UserExpandInfo.update($scope.userExpandInfo,
                    function () {
                        $scope.refresh();
                    });
            } else {
                UserExpandInfo.save($scope.userExpandInfo,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            UserExpandInfo.get({id: id}, function(result) {
                $scope.userExpandInfo = result;
                $('#deleteUserExpandInfoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserExpandInfo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserExpandInfoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveUserExpandInfoModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userExpandInfo = {key: null, value: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
