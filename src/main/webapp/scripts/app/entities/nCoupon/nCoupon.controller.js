'use strict';

angular.module('sandboxApp')
    .controller('NCouponController', function ($scope, NCoupon,NCoupons, CouponBatch, ParseLinks) {
        $scope.nCoupons = [];
        $scope.page = 1;
        $scope.pageSize = 20;

        var columnDefs = [
            {
                headerName: "Action",
                cellRenderer: function (params) {
                    return '<button type="submit"' +
                        ' ng-click=view(' + params.data.id + ')' +
                        ' class="btn btn-primary btn-xs btn-entity">' +
                        '    <span class="glyphicon glyphicon-eye-open"></span>&nbsp;<span translate="entity.action.view"> View</span>' +
                        '</button>' +
                        '<button type="submit"' +
                        ' ng-click=edit(' + params.data.id + ')' +
                        ' class="btn btn-info btn-xs btn-entity">' +
                        '    <span class="glyphicon glyphicon-pencil"></span>&nbsp;<span translate="entity.action.edit"> Edit</span>' +
                        '</button>' +
                        '<button type="submit"' +
                        ' ng-click=delete(' + params.data.id + ')' +
                        ' class="btn btn-danger btn-xs btn-entity">' +
                        '    <span class="glyphicon glyphicon-remove-circle"></span>&nbsp;<span translate="entity.action.delete"> Delete</span>' +
                        '</button>';
                }
            },
            {
                headerName: "#", width: 50,
                cellRenderer: function (params) {
                    return params.node.id + 1;
                },
                suppressSorting: true,
                suppressMenu: true
            },
            {headerName: "Id", field: "id"},
            {headerName: "Code", field: "code"},
            {headerName: "Status", field: "status"},
            {headerName: "CreateTime", field: "createTime"},
            {headerName: "ModifedTime", field: "modifedTime"}
        ];

        $scope.gridOptions = {
            enableServerSideSorting: true,
            enableServerSideFilter: true,
            enableColResize: true,
            columnDefs: columnDefs,
            ready: function (api) {
                console.log('Callback ready:api = ' + api);
                console.log(api);
            },
            angularCompileRows: true
        };

        $scope.loadAll = function () {
            NCoupon.query({page: $scope.page, per_page: $scope.pageSize}, function () {
                createNewDatasource();
            });
        };

        $scope.loadAll();

        $scope.view = function (id) {
            $state.go('nCoupon.detail', {id: id});
        };

        $scope.edit = function (id) {
            $state.go('nCoupon.edit', {id: id});
        };

        $scope.delete = function (id) {
            NCoupon.get({id: id}, function (result) {
                $scope.nCoupon = result;
                $('#deleteNCouponConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            NCoupon.delete({id: id}, function () {
                $scope.loadAll();
                $('#deleteNCouponConfirmation').modal('hide');
                $scope.clear();
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };
        $scope.clear = function () {
            $scope.nCoupon = {
                code: null, status: null, createdTime: null, modifedTime: null, id: null
            };
        };

        function createNewDatasource() {
            var dataSource = {
                pageSize: $scope.pageSize,
                getRows: function (params) {
                    var data = [];
                    var total = 0;
                    var page = Math.floor(params.startRow / $scope.pageSize) + 1;
                    NCoupons.query({
                        page: page,
                        per_page: $scope.pageSize,
                        sort: params.sortModel,
                        filter: params.filterModel
                    }, function (result) {
                        data = result.content;
                        total = result.totalElements;
                    });

                    setTimeout(function () {
                        params.successCallback(data, total);
                    }, 500);
                }
            };
            $scope.gridOptions.api.setDatasource(dataSource);
        }


        /* $scope.couponbatchs = CouponBatch.query();
         $scope.page = 1;
         $scope.loadAll = function () {
         NCoupon.query({page: $scope.page, per_page: 20}, function (result, headers) {
         $scope.links = ParseLinks.parse(headers('link'));
         $scope.nCoupons = result;
         });
         };
         $scope.loadPage = function (page) {
         $scope.page = page;
         $scope.loadAll();
         };
         $scope.loadAll();

         $scope.showUpdate = function (id) {
         NCoupon.get({id: id}, function (result) {
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
         NCoupon.get({id: id}, function (result) {
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
         };*/
    });
