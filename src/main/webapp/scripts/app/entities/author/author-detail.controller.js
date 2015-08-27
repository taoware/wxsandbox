'use strict';

angular.module('sandboxApp')
    .controller('AuthorDetailController', function ($scope, $rootScope, $stateParams, entity, Author, Book) {
        $scope.author = entity;
        $scope.load = function (id) {
            Author.get({id: id}, function (result) {
                $scope.author = result;
            });
        };
        $rootScope.$on('sandboxApp:authorUpdate', function (event, result) {
            $scope.author = result;
        });
    });


