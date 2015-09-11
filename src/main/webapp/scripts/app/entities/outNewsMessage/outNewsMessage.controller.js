'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageController', function ($scope,$state, OutNewsMessage,OutNewsMessages, OutNewsMessageItem, ParseLinks) {
        $scope.outNewsMessages = [];
        $scope.outnewsmessageitems = OutNewsMessageItem.query();
        $scope.page = 1;
        $scope.pageSize = 20;

        var columnDefs = [
            {
                headerName:"Action",
                cellRenderer: function(params) {
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
            {headerName:"Id",field:"id"},
            {headerName:"MenuName",field:"menuName"},
            {headerName:"StartDate",field:"startDate"},
            {headerName:"EndDate",field:"endDate"}
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
            OutNewsMessage.query({page: $scope.page, per_page: $scope.pageSize}, function () {
                createNewDatasource();
            });
        };
        $scope.loadAll();

        $scope.view = function (id) {
            $state.go('outNewsMessage.detail', {id: id});
        };

        $scope.edit = function (id) {
            $state.go('outNewsMessage.edit', {id: id});
        };

        $scope.delete = function (id) {
            OutNewsMessage.get({id: id}, function (result) {
                $scope.outNewsMessage = result;
                $('#deleteOutNewsMessageConfirmation').modal('show');
            });
        };
        $scope.confirmDelete = function (id) {
            OutNewsMessage.delete({id: id}, function () {
                $scope.loadAll();
                $('#deleteOutNewsMessageConfirmation').modal('hide');
                $scope.clear();
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.nCoupon = {
                menuName: null, startDate: null, endDate: null, id: null
            };
        };

        function createNewDatasource() {
            var dataSource = {
                pageSize: $scope.pageSize,
                getRows: function (params) {
                    var data = [];
                    var total = 0;
                    var page = Math.floor(params.startRow / $scope.pageSize) + 1;
                    OutNewsMessages.query({
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

    });
