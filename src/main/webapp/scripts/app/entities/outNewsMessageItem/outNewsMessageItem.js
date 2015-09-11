'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('outNewsMessageItem', {
                parent: 'entity',
                url: '/outNewsMessageItems',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outNewsMessageItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outNewsMessageItem/outNewsMessageItems.html',
                        controller: 'OutNewsMessageItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outNewsMessageItem');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('outNewsMessageItem.detail', {
                parent: 'entity',
                url: '/outNewsMessageItem/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outNewsMessageItem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outNewsMessageItem/outNewsMessageItem-detail.html',
                        controller: 'OutNewsMessageItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outNewsMessageItem');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'OutNewsMessageItem', function ($stateParams, OutNewsMessageItem) {
                        return OutNewsMessageItem.get({id: $stateParams.id});
                    }]
                }
            })
            .state('outNewsMessageItem.new', {
                parent: 'outNewsMessageItem',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/outNewsMessageItem/outNewsMessageItem-dialog.html',
                        controller: 'OutNewsMessageItemDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    picUrl: null, url: null, content: null, id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('outNewsMessageItem', null, {reload: true});
                        }, function () {
                            $state.go('outNewsMessageItem');
                        })
                }]
            })
            .state('outNewsMessageItem.edit', {
                parent: 'outNewsMessageItem',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/outNewsMessageItem/outNewsMessageItem-dialog.html',
                        controllerr: 'OutNewsMessageItemDialogController',
                        size: 'md',
                        resolve: {
                            entity: ['OutNewsMessageItem', function (OutNewsMessageItem) {
                                return OutNewsMessageItem.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('outNewsMessageItem', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
