'use strict';

angular.module('sandboxApp')
    .controller('CouponBatchController', function ($scope, CouponBatch, SupplierActivity, ParseLinks) {
        $scope.couponBatchs = [];
        $scope.supplieractivitys = SupplierActivity.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            CouponBatch.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.couponBatchs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            CouponBatch.get({id: id}, function(result) {
                $scope.couponBatch = result;
                $('#saveCouponBatchModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.couponBatch.id != null) {
                CouponBatch.update($scope.couponBatch,
                    function () {
                        $scope.refresh();
                    });
            } else {
                CouponBatch.save($scope.couponBatch,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            CouponBatch.get({id: id}, function(result) {
                $scope.couponBatch = result;
                $('#deleteCouponBatchConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CouponBatch.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCouponBatchConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveCouponBatchModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.couponBatch = {code: null, size: null, quantity: null, beginDate: null, endDate: null, enabled: null, isGenerated: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
