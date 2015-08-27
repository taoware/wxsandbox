'use strict';

angular.module('sandboxApp')
    .controller('ActivityController', function ($scope, Activity, WCUser, ParseLinks) {
        $scope.activitys = [];
        $scope.wcusers = WCUser.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Activity.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.activitys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Activity.get({id: id}, function(result) {
                $scope.activity = result;
                $('#saveActivityModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.activity.id != null) {
                Activity.update($scope.activity,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Activity.save($scope.activity,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Activity.get({id: id}, function(result) {
                $scope.activity = result;
                $('#deleteActivityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Activity.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteActivityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveActivityModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.activity = {disable: null, name: null, indexName: null, folderName: null, type: null, description: null, url: null, startDate: null, endDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
