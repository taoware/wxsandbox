'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('order', {
                parent: 'entity',
                url: '/orders',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.order.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/order/orders.html',
                        controller: 'OrderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('order');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('order.detail', {
                parent: 'entity',
                url: '/order/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.order.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/order/order-detail.html',
                        controller: 'OrderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('order');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Order', function($stateParams, Order) {
                        return Order.get({id : $stateParams.id});
                    }]
                }
            })
            .state('order.new', {
                parent: 'order',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/order/order-dialog.html',
                        controller: 'OrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {myString: null, myInteger: null, myLong: null, myFloat: null, myDouble: null, myDecimal: null, myDate: null, myDateTime: null, myBoolean: null, myEnumeration: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('order', null, { reload: true });
                    }, function() {
                        $state.go('order');
                    })
                }]
            })
            .state('order.edit', {
                parent: 'order',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/order/order-dialog.html',
                        controller: 'OrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Order', function(Order) {
                                return Order.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('order', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
