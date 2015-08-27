'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cUser', {
                parent: 'entity',
                url: '/cUser',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.cUser.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/cUser/cUsers.html',
                        controller: 'CUserController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cUser');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('cUserDetail', {
                parent: 'entity',
                url: '/cUser/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.cUser.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/cUser/cUser-detail.html',
                        controller: 'CUserDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cUser');
                        return $translate.refresh();
                    }]
                }
            });
    });
