'use strict';

angular.module('sandboxApp')
    .controller('OutMessageController', function ($scope, OutMessage, WCUser, ParseLinks) {
        $scope.outMessages = [];
        $scope.wcusers = WCUser.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            OutMessage.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.outMessages = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            OutMessage.get({id: id}, function(result) {
                $scope.outMessage = result;
                $('#saveOutMessageModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.outMessage.id != null) {
                OutMessage.update($scope.outMessage,
                    function () {
                        $scope.refresh();
                    });
            } else {
                OutMessage.save($scope.outMessage,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            OutMessage.get({id: id}, function(result) {
                $scope.outMessage = result;
                $('#deleteOutMessageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OutMessage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOutMessageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveOutMessageModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.outMessage = {type: null, content: null, url: null, picUrl: null, title: null, menuName: null, startDate: null, endDate: null, disable: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
