'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('outNewsMessage', {
                parent: 'entity',
                url: '/outNewsMessages',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outNewsMessage.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outNewsMessage/outNewsMessages.html',
                        controller: 'OutNewsMessageController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outNewsMessage');
                        return $translate.refresh();
                    }]
                }
            })
            .state('outNewsMessage.detail', {
                parent: 'entity',
                url: '/outNewsMessage/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.outNewsMessage.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outNewsMessage/outNewsMessage-detail.html',
                        controller: 'OutNewsMessageDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outNewsMessage');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'OutNewsMessage', function ($stateParams, OutNewsMessage) {
                        return OutNewsMessage.get({id: $stateParams.id});
                    }]
                }
            })
            .state('outNewsMessage.new',{
                parent: 'outNewsMessage',
                utl: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/outNewsMessage/outNewsMessage-dialog.html',
                        controller: 'OutNewsMessageDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    menuName: null, startDate: null, endDate: null, id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('outNewsMessage', null, {reload: true});
                        }, function () {
                            $state.go('outNewsMessage');
                        })
                }]

            })

            .state('outNewsMessage.edit', {
                parent: 'outNewsMessage',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/outNewsMessage/outNewsMessage-dialog.html',
                        controller: 'OutNewsMessageDialogController',
                        size: 'md',
                        resolve: {
                            entity: ['OutNewsMessage', function (OutNewsMessage) {
                                return OutNewsMessage.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('outNewsMessage', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
