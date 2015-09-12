'use strict';

angular.module('sandboxApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('goods', {
                parent: 'entity',
                url: '/goods',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.goods.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/goods/goodss.html',
                        controller: 'GoodsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('goods');
                        return $translate.refresh();
                    }]
                }
            })
            .state('goodsDetail', {
                parent: 'entity',
                url: '/goods/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'sandboxApp.goods.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/goods/goods-detail.html',
                        controller: 'GoodsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('goods');
                        return $translate.refresh();
                    }]
                }
            });
    });
