'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('supplierActivity', {
                parent: 'entity',
                url: '/supplierActivity',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.supplierActivity.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/supplierActivity/supplierActivitys.html',
                        controller: 'SupplierActivityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supplierActivity');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplierActivity.detail', {
                parent: 'entity',
                url: '/supplierActivity/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.supplierActivity.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/supplierActivity/supplierActivity-detail.html',
                        controller: 'SupplierActivityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supplierActivity');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SupplierActivity', function ($stateParams, SupplierActivity) {
                        return SupplierActivity.get({id: $stateParams.id});
                    }]
                }
            })
            .state('supplierActivity.new', {
                parent: 'supplierActivity',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/supplierActivity-dialog.html',
                        controller: 'SupplierActivityDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    code: null,
                                    name: null,
                                    description: null,
                                    beginDate: null,
                                    endDate: null,
                                    enabled: null,
                                    createdTime: null,
                                    modifiedTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('supplierActivity', null, {reload: true});
                        }, function () {
                            $state.go('supplierActivity');
                        })
                }]

            })
            .state('supplierActivity.edit', {
                parent: 'supplierActivity',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/supplierActivity/supplierActivity-dialog.html',
                        controller: 'SupplierActivityDialogController',
                        size: 'md',
                        resolve: {
                            entity: ['SupplierActivity', function (SupplierActivity) {
                                return SupplierActivity.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('supplierActivity', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
