'use strict';

/* Controllers */

var storeControllers = angular.module('storeControllers', []);

storeControllers.controller('StoreListCtrl', ['$scope', 'Store',
  function($scope, Store) {
    $scope.stores = Store.query();
    $scope.orderProp = 'age';
  }]);

storeControllers.controller('StoreDetailCtrl', ['$scope', '$routeParams', 'Store',
  function($scope, $routeParams, Store) {
    $scope.store = Store.get({storeId: $routeParams.storeId}, function(store) {
      $scope.mainImageUrl = store.images[0];
    });

    $scope.setImage = function(imageUrl) {
      $scope.mainImageUrl = imageUrl;
    };
  }]);
