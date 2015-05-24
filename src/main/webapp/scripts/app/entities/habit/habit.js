'use strict';

angular.module('awarenessApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('habit', {
                parent: 'entity',
                url: '/habit',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.habit.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/habit/habits.html',
                        controller: 'HabitController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('habit');
                        return $translate.refresh();
                    }]
                }
            })
            .state('habitDetail', {
                parent: 'entity',
                url: '/habit/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.habit.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/habit/habit-detail.html',
                        controller: 'HabitDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('habit');
                        return $translate.refresh();
                    }]
                }
            });
    });
