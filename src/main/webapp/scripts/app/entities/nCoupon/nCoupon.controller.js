'use strict';

angular.module('sandboxApp')
    .controller('NCouponController', function ($scope, NCoupon, CouponBatch, ParseLinks) {
        $scope.nCoupons = [];
        $scope.couponbatchs = CouponBatch.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            NCoupon.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.nCoupons = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            NCoupon.get({id: id}, function(result) {
                $scope.nCoupon = result;
                $('#saveNCouponModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.nCoupon.id != null) {
                NCoupon.update($scope.nCoupon,
                    function () {
                        $scope.refresh();
                    });
            } else {
                NCoupon.save($scope.nCoupon,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            NCoupon.get({id: id}, function(result) {
                $scope.nCoupon = result;
                $('#deleteNCouponConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            NCoupon.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNCouponConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveNCouponModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.nCoupon = {code: null, status: null, createdTime: null, modifedTime: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
