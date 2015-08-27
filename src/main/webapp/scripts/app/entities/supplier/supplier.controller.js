'use strict';

angular.module('sandboxApp')
    .controller('SupplierController', function ($scope, Supplier, ParseLinks) {
        $scope.suppliers = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Supplier.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.suppliers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Supplier.get({id: id}, function(result) {
                $scope.supplier = result;
                $('#saveSupplierModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.supplier.id != null) {
                Supplier.update($scope.supplier,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Supplier.save($scope.supplier,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Supplier.get({id: id}, function(result) {
                $scope.supplier = result;
                $('#deleteSupplierConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Supplier.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSupplierConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveSupplierModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.supplier = {code: null, name: null, address: null, contact: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
