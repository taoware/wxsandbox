'use strict';

angular.module('sandboxApp')
    .controller('SupplierActivityController', function ($scope, SupplierActivity, SupplierActivitys, Supplier) {
        $scope.supplierActivitys = [];
        $scope.page = 1;
        $scope.pageSize = 20;

        var columnDefs = [
            {
                headerName:"Action",
                cellRenderer: function(params){
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
                headerName:"#",width:50,
                cellRenderer:function(params){
                    return params.node.id + 1;
                },
                suppressSorting:true,
                suppressMenu:true
            },
            {headerName:"Id",field:"id"},
            {headerName:"Code",field:"code"},
            {headerName:"Name",field:"name"},
            {headerName:"BeginDate",field:"beginDate"},
            {headerName:"EndDate",field:"endDate"},
            {headerName:"Enabled",field:"enabled"},
            {headerName:"CreateTime",field:"createTime"},
            {headerName:"ModifiedTime",field:"modifiedTime"},
            {headerName:"Supplier",field:"supplier.name"}
        ];

        $scope.gridOptions = {
            enableServerSideSorting: true,
            enableServerSideFilter: true,
            enableColResize:true,
            columnDefs:columnDefs,
            ready:function(api) {
                console.log('Callback ready: api =' + api);
                console.log(api);
            },
            angularCompileRows:true
        };

        $scope.loadAll = function(){
            SupplierActivity.query({page:$scope.page,per_page:$scope.pageSize},function(){
                createNewDatasource();
            });
        };

        $scope.loadAll();

        $scope.view = function(id){
            $state.go('supplierActivity.detail',{id:id});
        };

        $scope.edit = function(id) {
            $state.go('supplierActivity.edit',{id:id});
        };

        $scope.delete = function(id) {
            SupplierActivity.get({id:id},function(result){
                $scope.supplierActivity = result;
                $('#deleteSupplierActivityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function(id) {
            SupplierActivity.delete({id:id},function(){
                $scope.loadAll();
                $('#deleteSupplierActivityConfirmation').modal('hide');
                $scope.clear();
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function() {
            $scope.supplierActivity = {
                code: null,
                name: null,
                description: null,
                beginDate: null,
                endDate: null,
                enabled: null,
                createdTime: null,
                modifiedTime: null,
                id: null
            };
        };

        function createNewDatasource(){
            var dataSource = {
                pageSize:$scope.pageSize,
                getRows:function(params) {
                    var data = [];
                    var total = 0;
                    var page = Math.floor(params.startRow / $scope.pageSize) + 1;

                    SupplierActivitys.query({
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

      /*  $scope.suppliers = Supplier.query();
        $scope.page = 1;
        $scope.loadAll = function () {
            SupplierActivity.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.supplierActivitys = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            SupplierActivity.get({id: id}, function (result) {
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
            SupplierActivity.get({id: id}, function (result) {
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
            $scope.supplierActivity = {
                code: null,
                name: null,
                description: null,
                beginDate: null,
                endDate: null,
                enabled: null,
                createdTime: null,
                modifiedTime: null,
                id: null
            };
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };*/
    });
