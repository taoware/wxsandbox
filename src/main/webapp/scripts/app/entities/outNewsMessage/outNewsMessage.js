'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('outNewsMessage', {
                parent: 'entity',
                url: '/outNewsMessage',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outNewsMessage.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outNewsMessage/outNewsMessages.html',
                        controller: 'OutNewsMessageController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outNewsMessage');
                        return $translate.refresh();
                    }]
                }
            })
            .state('outNewsMessageDetail', {
                parent: 'entity',
                url: '/outNewsMessage/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outNewsMessage.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outNewsMessage/outNewsMessage-detail.html',
                        controller: 'OutNewsMessageDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outNewsMessage');
                        return $translate.refresh();
                    }]
                }
            });
    });
