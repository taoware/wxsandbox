'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('outNewsMessageItem', {
                parent: 'entity',
                url: '/outNewsMessageItem',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outNewsMessageItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outNewsMessageItem/outNewsMessageItems.html',
                        controller: 'OutNewsMessageItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outNewsMessageItem');
                        return $translate.refresh();
                    }]
                }
            })
            .state('outNewsMessageItemDetail', {
                parent: 'entity',
                url: '/outNewsMessageItem/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outNewsMessageItem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outNewsMessageItem/outNewsMessageItem-detail.html',
                        controller: 'OutNewsMessageItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outNewsMessageItem');
                        return $translate.refresh();
                    }]
                }
            });
    });
