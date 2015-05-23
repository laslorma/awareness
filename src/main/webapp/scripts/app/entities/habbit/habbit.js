'use strict';

angular.module('awarenessApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('habbit', {
                parent: 'entity',
                url: '/habbit',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.habbit.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/habbit/habbits.html',
                        controller: 'HabbitController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('habbit');
                        return $translate.refresh();
                    }]
                }
            })
            .state('habbitDetail', {
                parent: 'entity',
                url: '/habbit/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.habbit.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/habbit/habbit-detail.html',
                        controller: 'HabbitDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('habbit');
                        return $translate.refresh();
                    }]
                }
            });
    });
