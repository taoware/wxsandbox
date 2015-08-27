'use strict';

angular.module('sandboxApp')
    .controller('CUserController', function ($scope, CUser, Coupon, ParseLinks) {
        $scope.cUsers = [];
        $scope.coupons = Coupon.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            CUser.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.cUsers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            CUser.get({id: id}, function(result) {
                $scope.cUser = result;
                $('#saveCUserModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.cUser.id != null) {
                CUser.update($scope.cUser,
                    function () {
                        $scope.refresh();
                    });
            } else {
                CUser.save($scope.cUser,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            CUser.get({id: id}, function(result) {
                $scope.cUser = result;
                $('#deleteCUserConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CUser.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCUserConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveCUserModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.cUser = {mobile: null, openId: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
