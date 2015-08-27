'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('wCUser', {
                parent: 'entity',
                url: '/wCUser',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.wCUser.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/wCUser/wCUsers.html',
                        controller: 'WCUserController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('wCUser');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('wCUserDetail', {
                parent: 'entity',
                url: '/wCUser/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.wCUser.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/wCUser/wCUser-detail.html',
                        controller: 'WCUserDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('wCUser');
                        return $translate.refresh();
                    }]
                }
            });
    });
