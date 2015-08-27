'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('coupon', {
                parent: 'entity',
                url: '/coupon',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.coupon.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coupon/coupons.html',
                        controller: 'CouponController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coupon');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('couponDetail', {
                parent: 'entity',
                url: '/coupon/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.coupon.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coupon/coupon-detail.html',
                        controller: 'CouponDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coupon');
                        return $translate.refresh();
                    }]
                }
            });
    });
