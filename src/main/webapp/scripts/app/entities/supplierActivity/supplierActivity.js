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
            .state('supplierActivityDetail', {
                parent: 'entity',
                url: '/supplierActivity/:id',
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
                    }]
                }
            });
    });
