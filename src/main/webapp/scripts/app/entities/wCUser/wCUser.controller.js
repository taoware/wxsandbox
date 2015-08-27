'use strict';

angular.module('sandboxApp')
    .controller('WCUserController', function ($scope, WCUser, ParseLinks) {
        $scope.wCUsers = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            WCUser.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.wCUsers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            WCUser.get({id: id}, function(result) {
                $scope.wCUser = result;
                $('#saveWCUserModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.wCUser.id != null) {
                WCUser.update($scope.wCUser,
                    function () {
                        $scope.refresh();
                    });
            } else {
                WCUser.save($scope.wCUser,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            WCUser.get({id: id}, function(result) {
                $scope.wCUser = result;
                $('#deleteWCUserConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            WCUser.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteWCUserConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveWCUserModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.wCUser = {openId: null, nickname: null, sex: null, city: null, province: null, country: null, unionId: null, mobile: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
