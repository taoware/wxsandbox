/* globals $ */
'use strict';

angular.module('sandboxApp')
    .directive('sandboxAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
