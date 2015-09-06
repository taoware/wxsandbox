'use strict';

angular.module('sandboxApp')
    .controller('CouponBatchController', function ($scope,$state, CouponBatch,CouponBatchs, SupplierActivity) {
        $scope.couponBatchs = [];
        $scope.supplieractivitys = SupplierActivity.query();
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
            {headerName: "Id", field: "id", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "Code", field: "code", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "Size", field: "size", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "Quantity", field: "quantity", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "BeginDate", field: "beginDate"},
            {headerName: "EndDate", field: "endDate"},
            {headerName: "Enable", field: "enable"},
            {headerName: "IsGenerated", field: "isgenerated"}
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
            CouponBatch.query({
                page: $scope.page, per_page: $scope.pageSize
            }, function () {
                createNewDatasource();
            });
        };
        $scope.loadAll();

        $scope.view = function(id) {
            $state.go('couponBatch.detail',{id:id});
        };

        $scope.edit = function(id) {
            $state.go('couponBatch.edit',{id:id});
        };

        $scope.delete = function(id) {
            CouponBatch.get({id:id},function(result){
                $scope.couponBatch = result;
                $('#deleteCouponBatchConfirmation').modal('show');
            });
        };
        $scope.confirmDelete = function(id) {
            CouponBatch.delete({id:id},
            function(){
                $scope.loadAll();
                $('#deleteCouponBatchConfirmation').modal('hide');
            });
        };
        $scope.refresh = function(){
            $scope.loadAll();
            $scope.clear();
        };
        $scope.clear = function(){
            $scope.couponBatch = {
                code: null,
                size: null,
                quantity: null,
                beginDate: null,
                endDate: null,
                enabled: null,
                isGenerated: null,
                id: null
            };
        };

        function createNewDatasource(){
            var dataSource = {
                pageSize:$scope.pageSize,
                getRows:function(params){
                    var data = [];
                    var total = 0;
                    var page = Math.floor(params.startRow / $scope.pageSize) + 1;
                    console.log(params);
                    console.log('asking for' + params.startRow + 'to' + params.endRow);
                    CouponBatchs.query({
                        page:page,
                        per_page:$scope.pageSize,
                        sort:params.sortModel,
                        filter:params.filterModel
                    },function(result){
                        data = result.content;
                        total = result.totalElements;
                    });
                    setTimeout(function(){
                        params.successCallback(data,total);
                    },500);
                }
            };
            $scope.gridOptions.api.setDatasource(dataSource);
        }


       /* $scope.loadAll = function () {
            CouponBatch.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.couponBatchs = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            CouponBatch.get({id: id}, function (result) {
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
            CouponBatch.get({id: id}, function (result) {
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
            $scope.couponBatch = {
                code: null,
                size: null,
                quantity: null,
                beginDate: null,
                endDate: null,
                enabled: null,
                isGenerated: null,
                id: null
            };
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };*/
    });
