'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('nCoupon', {
                parent: 'entity',
                url: '/nCoupon',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.nCoupon.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nCoupon/nCoupons.html',
                        controller: 'NCouponController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nCoupon');
                        return $translate.refresh();
                    }]
                }
            })
            .state('nCouponDetail', {
                parent: 'entity',
                url: '/nCoupon/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.nCoupon.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nCoupon/nCoupon-detail.html',
                        controller: 'NCouponDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nCoupon');
                        return $translate.refresh();
                    }]
                }
            });
    });
