'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('supplier', {
                parent: 'entity',
                url: '/supplier',
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
            .state('supplierDetail', {
                parent: 'entity',
                url: '/supplier/:id',
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
                    }]
                }
            });
    });
