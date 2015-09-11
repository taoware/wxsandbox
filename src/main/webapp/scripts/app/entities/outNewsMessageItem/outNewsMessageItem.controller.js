'use strict';

angular.module('sandboxApp')
    .controller('OutNewsMessageItemController', function ($scope, $state,OutNewsMessageItem,OutNewsMessageItems, UserInfo, ParseLinks) {
        $scope.outNewsMessageItems = [];
        $scope.userinfos = UserInfo.query();
        $scope.page = 1;
        $scope.pageSize = 20;

        var columnDefs = [
            {
                headerName:"Action",
                cellRenderer:function(params) {
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
            {headerName:"Id",filter:'id'},
            {headerName:"PicUrl",filter:'picUrl'},
            {headerName:"Url",filter:'url'},
            {headerName:"Content",filter:'content'}
        ];

        $scope.gridOptions = {
            enbaleServerSideSorting:true,
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
            OutNewsMessageItem.query({
                page: $scope.page, per_page: $scope.pageSize
            }, function () {
                createNewDatasource();
            });
        };
        $scope.loadAll();
        $scope.view = function(id) {
            $state.go('outNewsMessageItem.detail',{id:id});
        };

        $scope.edit = function(id) {
            $state.go('outNewsMessageItem.edit',{id:id});
        };

        $scope.delete = function(id) {
            OutNewsMessageItem.get({id:id},function(result){
                $scope.couponBatch = result;
                $('#deleteOutNewsMessageItemConfirmation').modal('show');
            });
        };
        $scope.confirmDelete = function(id) {
            OutNewsMessageItem.delete({id:id},
                function(){
                    $scope.loadAll();
                    $('#deleteOutNewsMessageItemConfirmation').modal('hide');
                });
        };
        $scope.refresh = function(){
            $scope.loadAll();
            $scope.clear();
        };
        $scope.clear = function(){
            $scope.outNewsMessageItem = {
                picUrl: null, url: null, content: null, id: null
            };
        };

        function createNewDatasource() {
            var dataSource = {
                pageSize:$scope.pageSize,
                getRows:function(params){
                    var data = [];
                    var total = 0;
                    var page = Math.floor(params.startRow / $scope.pageSize) + 1;
                    console.log(params);
                    console.log('asking for' + params.startRow + 'to' + params.endRow);
                    OutNewsMessageItems.query({
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

       /* $scope.loadAll = function() {
            OutNewsMessageItem.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.outNewsMessageItems = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            OutNewsMessageItem.get({id: id}, function(result) {
                $scope.outNewsMessageItem = result;
                $('#saveOutNewsMessageItemModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.outNewsMessageItem.id != null) {
                OutNewsMessageItem.update($scope.outNewsMessageItem,
                    function () {
                        $scope.refresh();
                    });
            } else {
                OutNewsMessageItem.save($scope.outNewsMessageItem,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            OutNewsMessageItem.get({id: id}, function(result) {
                $scope.outNewsMessageItem = result;
                $('#deleteOutNewsMessageItemConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OutNewsMessageItem.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOutNewsMessageItemConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveOutNewsMessageItemModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.outNewsMessageItem = {picUrl: null, url: null, content: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };*/
    });
