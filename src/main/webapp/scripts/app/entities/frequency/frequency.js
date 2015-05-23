'use strict';

angular.module('awarenessApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('frequency', {
                parent: 'entity',
                url: '/frequency',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.frequency.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/frequency/frequencys.html',
                        controller: 'FrequencyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('frequency');
                        return $translate.refresh();
                    }]
                }
            })
            .state('frequencyDetail', {
                parent: 'entity',
                url: '/frequency/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.frequency.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/frequency/frequency-detail.html',
                        controller: 'FrequencyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('frequency');
                        return $translate.refresh();
                    }]
                }
            });
    });
