'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userInfo', {
                parent: 'entity',
                url: '/userInfo',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.userInfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userInfo/userInfos.html',
                        controller: 'UserInfoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userInfo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userInfoDetail', {
                parent: 'entity',
                url: '/userInfo/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.userInfo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userInfo/userInfo-detail.html',
                        controller: 'UserInfoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userInfo');
                        return $translate.refresh();
                    }]
                }
            });
    });
