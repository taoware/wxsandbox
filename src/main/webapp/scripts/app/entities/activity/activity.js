'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('activity', {
                parent: 'entity',
                url: '/activity',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.activity.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/activity/activitys.html',
                        controller: 'ActivityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activity');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('activityDetail', {
                parent: 'entity',
                url: '/activity/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.activity.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/activity/activity-detail.html',
                        controller: 'ActivityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activity');
                        return $translate.refresh();
                    }]
                }
            });
    });
