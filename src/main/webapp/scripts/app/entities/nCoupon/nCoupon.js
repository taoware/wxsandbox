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
            .state('nCoupon.detail', {
                parent: 'entity',
                url: '/nCoupon/{id}',
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
                    }],
                    entity: ['$stateParams', 'NCoupon', function ($stateParams, NCoupon) {
                        return NCoupon.get({id: $stateParams.id});
                    }]
                }
            })
            .state('nCoupon.new', {
                parent: 'nCoupon',
                utl: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/nCoupon/nCoupon-dialog.html',
                        controller: 'NCouponDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    code: null, status: null, createdTime: null, modifedTime: null, id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('nCoupon', null, {reload: true});
                        }, function () {
                            $state.go('nCoupon');
                        })
                }]
            })
            .state('nCoupon.edit', {
                parent: 'nCoupon',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/nCoupon/nCoupon-dialog.html',
                        controller: 'NCouponDialogController',
                        size: 'md',
                        resolve: {
                            entity: ['NCoupon', function (NCoupon) {
                                return NCoupon.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('nCoupon', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
