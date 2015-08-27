'use strict';

angular.module('sandboxApp').controller('AuthorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Author', 'Book',
        function($scope, $stateParams, $modalInstance, entity, Author, Book) {

        $scope.author = entity;
        $scope.books = Book.query();
        $scope.load = function(id) {
            Author.get({id : id}, function(result) {
                $scope.author = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('sandboxApp:authorUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.author.id != null) {
                Author.update($scope.author, onSaveFinished);
            } else {
                Author.save($scope.author, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
