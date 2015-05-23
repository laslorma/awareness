'use strict';

angular.module('awarenessApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('type', {
                parent: 'entity',
                url: '/type',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.type.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/type/types.html',
                        controller: 'TypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('type');
                        return $translate.refresh();
                    }]
                }
            })
            .state('typeDetail', {
                parent: 'entity',
                url: '/type/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.type.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/type/type-detail.html',
                        controller: 'TypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('type');
                        return $translate.refresh();
                    }]
                }
            });
    });
