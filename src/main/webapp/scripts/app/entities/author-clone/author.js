'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('author', {
                parent: 'entity',
                url: '/authors',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.author.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/author/authors.html',
                        controller: 'AuthorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('author');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

            .state('author.detail', {
                parent: 'entity',
                url: '/author/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.author.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/author/author-detail.html',
                        controller: 'AuthorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('author');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Author', function($stateParams, Author) {
                        return Author.get({id : $stateParams.id});
                    }]
                }
            })

            .state('author.new',{
                parent:'entity',
                url:'/authors/new',
                data:{
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.author.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/author/author-dialog.html',
                        controller: 'AuthorNewController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('author');
                        return $translate.refresh();
                    }]
                }

            })


           /* .state('author.new', {
                parent: 'author',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/author/author-dialog.html',
                        controller: 'AuthorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, birthDate: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('author', null, { reload: true });
                    }, function() {
                        $state.go('author');
                    })
                }]
            })*/


            .state('author.edit', {
                parent: 'author',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/author/author-dialog.html',
                        controller: 'AuthorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Author', function(Author) {
                                return Author.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('author', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
