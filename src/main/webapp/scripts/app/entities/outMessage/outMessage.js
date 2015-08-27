'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('outMessage', {
                parent: 'entity',
                url: '/outMessage',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outMessage.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outMessage/outMessages.html',
                        controller: 'OutMessageController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outMessage');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('outMessageDetail', {
                parent: 'entity',
                url: '/outMessage/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outMessage.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outMessage/outMessage-detail.html',
                        controller: 'OutMessageDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outMessage');
                        return $translate.refresh();
                    }]
                }
            });
    });
