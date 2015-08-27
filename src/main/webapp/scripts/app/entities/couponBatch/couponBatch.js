'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('couponBatch', {
                parent: 'entity',
                url: '/couponBatch',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.couponBatch.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/couponBatch/couponBatchs.html',
                        controller: 'CouponBatchController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('couponBatch');
                        return $translate.refresh();
                    }]
                }
            })
            .state('couponBatchDetail', {
                parent: 'entity',
                url: '/couponBatch/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.couponBatch.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/couponBatch/couponBatch-detail.html',
                        controller: 'CouponBatchDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('couponBatch');
                        return $translate.refresh();
                    }]
                }
            });
    });
