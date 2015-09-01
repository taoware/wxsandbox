'use strict';

angular.module('sandboxApp')
    .controller('SupplierController', function ($scope, Supplier,Suppliers,ParseLinks) {
        $scope.suppliers = [];
        $scope.page = 1;
        $scope.pageSize = 20;

        var columnDefs = [
            {
                headerName:"Action",
                cellRenderer:function(params){
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
                cellRenderer:function(params) {
                    return params.node.id + 1;
                },
                suppressSorting:true,
                suppressMenu:true
            },
            {headerName:"Id",field:"id"},//code: null, name: null, address: null, contact: null,
            {headeName:"Code",field:"code"},
            {headeName:"Name",field:"name"},
            {headeName:"Address",field:"address"},
            {headeName:"Contact",field:"contact"}
        ];

        $scope.gridOptions = {
            enableServerSideSorting:true,
            enableServerSideFilter:true,
            enableColResize:true,
            columnDefs:columnDefs,
            ready:function(api) {
                console.log('Callback ready: api =' + api);
                console.log(api);
            },
            angularCompileRows:true
        };

        $scope.loadAll = function() {
            Supplier.query({page:$scope.page,per_page:$scope.pageSize},function(){
                createNewDatasource();
            });
        };

        $scope.loadAll();

        $scope.view = function(id){
            $state.go('supplier.detail',{id:id});
        };

        $scope.edit = function(id) {
            $state.go('supplier.edit',{id:id});
        };

        $scope.delete = function(id) {
           Supplier.get({id:id},function(result){
               $scope.supplier = result;
               $('#deleteSupplierConfirmation').modal('show');
           });
        };

        $scope.confirmDelete = function(id) {
            Supplier.delete({id:id},function(){
                $scope.loadAll();
                $('#deleteSupplierConfirmation').modal('hide');
                $scope.clear();
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function(){
            $scope.supplier = {
                code: null, name: null, address: null, contact: null, id: null
            };
        };

        function createNewDatasource() {
            var dataSource = {
                pageSize:$scope.pageSize,
                getRows: function(params) {
                    var data = [];
                    var total = 0;
                    var page = Math.floor(params.startRow / $scope.pageSize) + 1;

                    Supplier.query({
                        page:page,
                        per_page:$scope.pageSize,
                        sort:params.filterModel
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

      /*  $scope.loadAll = function() {
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
        };*/
    });
