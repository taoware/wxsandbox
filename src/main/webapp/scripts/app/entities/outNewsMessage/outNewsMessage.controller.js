'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageController', function ($scope, OutNewsMessage, OutNewsMessageItem, ParseLinks) {
        $scope.outNewsMessages = [];
        $scope.outnewsmessageitems = OutNewsMessageItem.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            OutNewsMessage.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.outNewsMessages = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            OutNewsMessage.get({id: id}, function(result) {
                $scope.outNewsMessage = result;
                $('#saveOutNewsMessageModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.outNewsMessage.id != null) {
                OutNewsMessage.update($scope.outNewsMessage,
                    function () {
                        $scope.refresh();
                    });
            } else {
                OutNewsMessage.save($scope.outNewsMessage,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            OutNewsMessage.get({id: id}, function(result) {
                $scope.outNewsMessage = result;
                $('#deleteOutNewsMessageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OutNewsMessage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOutNewsMessageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveOutNewsMessageModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.outNewsMessage = {menuName: null, startDate: null, endDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
