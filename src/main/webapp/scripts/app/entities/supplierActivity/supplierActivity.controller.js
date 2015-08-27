'use strict';

angular.module('sandboxApp')
    .controller('SupplierActivityController', function ($scope, SupplierActivity, Supplier, ParseLinks) {
        $scope.supplierActivitys = [];
        $scope.suppliers = Supplier.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            SupplierActivity.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.supplierActivitys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            SupplierActivity.get({id: id}, function(result) {
                $scope.supplierActivity = result;
                $('#saveSupplierActivityModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.supplierActivity.id != null) {
                SupplierActivity.update($scope.supplierActivity,
                    function () {
                        $scope.refresh();
                    });
            } else {
                SupplierActivity.save($scope.supplierActivity,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            SupplierActivity.get({id: id}, function(result) {
                $scope.supplierActivity = result;
                $('#deleteSupplierActivityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SupplierActivity.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSupplierActivityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveSupplierActivityModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.supplierActivity = {code: null, name: null, description: null, beginDate: null, endDate: null, enabled: null, createdTime: null, modifiedTime: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
