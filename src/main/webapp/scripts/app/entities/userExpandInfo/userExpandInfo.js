'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userExpandInfo', {
                parent: 'entity',
                url: '/userExpandInfo',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.userExpandInfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userExpandInfo/userExpandInfos.html',
                        controller: 'UserExpandInfoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userExpandInfo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userExpandInfoDetail', {
                parent: 'entity',
                url: '/userExpandInfo/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.userExpandInfo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userExpandInfo/userExpandInfo-detail.html',
                        controller: 'UserExpandInfoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userExpandInfo');
                        return $translate.refresh();
                    }]
                }
            });
    });
