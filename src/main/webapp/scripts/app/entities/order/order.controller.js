'use strict';

angular.module('sandboxApp')
    .controller('OrderController', function ($scope, $state, Order, Orders) {
        $scope.orders = [];
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
                cellRenderer: function(params) {
                    return params.node.id + 1;
                },
                // we don't want to sort by the row index, this doesn't make sense as the point
                // of the row index is to know the row index in what came back from the server
                suppressSorting: true,
                suppressMenu: true
            },
            {headerName: "Id", field: "id", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "MyString", field: "myString", filter: 'text', filterParams: {newRowsAction: 'keep'}},
            {headerName: "MyInteger", field: "myInteger", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "MyLong", field: "myLong", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "MyFloat", field: "myFloat", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "MyDouble", field: "myDouble", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "MyDecimal", field: "myDecimal", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "MyDate", field: "myDate", filter: 'number', filterParams: {newRowsAction: 'keep'}},
            {headerName: "MyDateTime", field: "myDateTime"},
            {headerName: "MyBoolean", field: "myBoolean"},
            {headerName: "MyEnumeration", field: "myEnumeration"}
        ];

        $scope.gridOptions = {
            enableServerSideSorting: true,
            enableServerSideFilter: true,
            enableColResize: true,
            columnDefs: columnDefs,
            ready: function(api) {
                console.log('Callback ready: api = ' + api);
                console.log(api);
            },
            angularCompileRows: true
        };

        $scope.loadAll = function() {
            Order.query({page: $scope.page, per_page: $scope.pageSize}, function() {
                createNewDatasource();
            });
        };

        $scope.loadAll();

        $scope.view = function(id) {
            $state.go('order.detail', {id: id});
        }

        $scope.edit = function(id) {
            $state.go('order.edit', {id: id});
        };

        $scope.delete = function (id) {
            Order.get({id: id}, function(result) {
                $scope.order = result;
                $('#deleteOrderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Order.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOrderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.order = {myString: null, myInteger: null, myLong: null, myFloat: null, myDouble: null, myDecimal: null, myDate: null, myDateTime: null, myBoolean: null, myEnumeration: null, id: null};
        };

        function createNewDatasource() {
            //if (!allOfTheData) {
            //    // in case user selected 'onPageSizeChanged()' before the json was loaded
            //    return;
            //}
            var dataSource = {
                pageSize: $scope.pageSize,
                getRows: function (params) {
                    // this code should contact the server for rows. however for the purposes of the demo,
                    // the data is generated locally, a timer is used to give the experience of
                    // an asynchronous call
                    var data = [];
                    var total = 0;
                    var page = Math.floor(params.startRow / $scope.pageSize) + 1;

                    console.log(params);
                    console.log('asking for ' + params.startRow + ' to ' + params.endRow);

                    Orders.query({page: page, per_page: $scope.pageSize, sort: params.sortModel, filter: params.filterModel}, function(result) {
                        data = result.content;
                        total = result.totalElements;
                    });

                    setTimeout( function() {
                        params.successCallback(data, total);
                    }, 500);
                }
            };

            $scope.gridOptions.api.setDatasource(dataSource);
        }
    });
