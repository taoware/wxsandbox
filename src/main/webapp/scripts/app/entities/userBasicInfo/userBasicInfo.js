'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userBasicInfo', {
                parent: 'entity',
                url: '/userBasicInfo',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.userBasicInfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userBasicInfo/userBasicInfos.html',
                        controller: 'UserBasicInfoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userBasicInfo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userBasicInfoDetail', {
                parent: 'entity',
                url: '/userBasicInfo/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.userBasicInfo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userBasicInfo/userBasicInfo-detail.html',
                        controller: 'UserBasicInfoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userBasicInfo');
                        return $translate.refresh();
                    }]
                }
            });
    });
