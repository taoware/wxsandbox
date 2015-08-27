'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('book', {
                parent: 'entity',
                url: '/books',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.book.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/book/books.html',
                        controller: 'BookController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('book');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('book.detail', {
                parent: 'entity',
                url: '/book/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.book.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/book/book-detail.html',
                        controller: 'BookDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('book');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Book', function($stateParams, Book) {
                        return Book.get({id : $stateParams.id});
                    }]
                }
            })
            .state('book.new', {
                parent: 'book',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/book/book-dialog.html',
                        controller: 'BookDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {title: null, description: null, publicationDate: null, price: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('book', null, { reload: true });
                    }, function() {
                        $state.go('book');
                    })
                }]
            })
            .state('book.edit', {
                parent: 'book',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/book/book-dialog.html',
                        controller: 'BookDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Book', function(Book) {
                                return Book.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('book', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
