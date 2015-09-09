'use strict';

/* App Module */

var storelistApp = angular.module('storelistApp', [
  'ngRoute',
  'storeAnimations',
  'storeControllers',
  'storeFilters',
  'storelistServices'
]);

storelistApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/stores', {
        templateUrl: 'partials/store-list.html',
        controller: 'StoreListCtrl'
      }).
      when('/store/:phoneId', {
        templateUrl: 'partials/phone-detail.html',
        controller: 'StoreDetailCtrl'
      }).
      otherwise({
        redirectTo: '/stores'
      });
  }]);
