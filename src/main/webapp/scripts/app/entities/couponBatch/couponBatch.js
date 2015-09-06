'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('couponBatch', {
                parent: 'entity',
                url: '/couponBatchs',
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
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('couponBatch.detail', {
                parent: 'entity',
                url: '/couponBatch/{id}',
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
                    }],
                    entity: ['$stateParams', 'CouponBatch', function ($stateParams, CouponBatch) {
                        return CouponBatch.get({id: $stateParams.id});
                    }]
                }
            })
            .state('couponBatch.new', {
                parent: 'couponBatch',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/couponBatch/couponBatch-dialog.html',
                        controller: 'CouponBatchDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    code: null,
                                    size: null,
                                    quantity: null,
                                    beginDate: null,
                                    endDate: null,
                                    enabled: null,
                                    isGenerated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('couponBatch', null, {reload: true});
                        }, function () {
                            $state.go('couponBatch');
                        })
                }]
            })
            .state('couponBatch.edit', {
                parent: 'couponBatch',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/couponBatch/couponBatch-dialog.html',
                        controller: 'CouponBatchDialogController',
                        size: 'md',
                        resolve: {
                            entity: ['CouponBatch', function (CouponBatch) {
                                return CouponBatch.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('couponBatch', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
