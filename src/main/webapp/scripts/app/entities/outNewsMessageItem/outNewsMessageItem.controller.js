'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageItemController', function ($scope, OutNewsMessageItem, UserInfo, ParseLinks) {
        $scope.outNewsMessageItems = [];
        $scope.userinfos = UserInfo.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            OutNewsMessageItem.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.outNewsMessageItems = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            OutNewsMessageItem.get({id: id}, function(result) {
                $scope.outNewsMessageItem = result;
                $('#saveOutNewsMessageItemModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.outNewsMessageItem.id != null) {
                OutNewsMessageItem.update($scope.outNewsMessageItem,
                    function () {
                        $scope.refresh();
                    });
            } else {
                OutNewsMessageItem.save($scope.outNewsMessageItem,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            OutNewsMessageItem.get({id: id}, function(result) {
                $scope.outNewsMessageItem = result;
                $('#deleteOutNewsMessageItemConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OutNewsMessageItem.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOutNewsMessageItemConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveOutNewsMessageItemModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.outNewsMessageItem = {picUrl: null, url: null, content: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
