'use strict';

/* Services */

var storeServices = angular.module('storelistServices', ['ngResource']);

storeServices.factory('Store', ['$resource',
  function($resource){
    return $resource('stores/:storeId.json', {}, {
      query: {method:'GET', params:{storeId:'stores'}, isArray:true}
    });
  }]);
