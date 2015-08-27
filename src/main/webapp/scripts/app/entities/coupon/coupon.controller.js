'use strict';

angular.module('sandboxApp')
    .controller('CouponController', function ($scope, Coupon, ParseLinks) {
        $scope.coupons = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Coupon.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.coupons = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Coupon.get({id: id}, function(result) {
                $scope.coupon = result;
                $('#saveCouponModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.coupon.id != null) {
                Coupon.update($scope.coupon,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Coupon.save($scope.coupon,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Coupon.get({id: id}, function(result) {
                $scope.coupon = result;
                $('#deleteCouponConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Coupon.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCouponConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveCouponModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.coupon = {code: null, password: null, category: null, status: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
