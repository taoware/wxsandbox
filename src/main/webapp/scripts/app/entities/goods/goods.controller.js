'use strict';

angular.module('sandboxApp')
    .controller('GoodsController', function ($scope, Goods, CouponBatch, ParseLinks) {
        $scope.goodss = [];
        $scope.couponbatchs = CouponBatch.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Goods.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.goodss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Goods.get({id: id}, function(result) {
                $scope.goods = result;
                $('#saveGoodsModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.goods.id != null) {
                Goods.update($scope.goods,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Goods.save($scope.goods,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Goods.get({id: id}, function(result) {
                $scope.goods = result;
                $('#deleteGoodsConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Goods.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteGoodsConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveGoodsModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.goods = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
