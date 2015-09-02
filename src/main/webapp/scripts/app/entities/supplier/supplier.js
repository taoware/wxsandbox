'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('supplier', {
                parent: 'entity',
                url: '/suppliers',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.supplier.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/supplier/suppliers.html',
                        controller: 'SupplierController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supplier');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplier.detail', {
                parent: 'entity',
                url: '/supplier/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.supplier.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/supplier/supplier-detail.html',
                        controller: 'SupplierDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supplier');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Supplier', function ($stateParams, Supplier) {
                        return Supplier.get({id: $stateParams.id});
                    }]
                }
            })
            .state('supplier.new', {
                parent: 'supplier',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/supplier/supplier-dialog.html',
                        controller: 'SupplierDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    code: null, name: null, address: null, contact: null, id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('supplier', null, {reload: true});
                        }, function () {
                            $state.go('supplier');
                        })
                }]
            })
            .state('supplier.edit', {
                parent: 'supplier',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/supplier/supplier-dialog.html',
                        controller: 'SupplierDialogController',
                        size: 'md',
                        resolve: {
                            entity: ['Supplier', function (Supplier) {
                                return Supplier.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('supplier', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
