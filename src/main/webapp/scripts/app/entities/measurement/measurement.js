'use strict';

angular.module('awarenessApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('measurement', {
                parent: 'entity',
                url: '/measurement',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.measurement.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/measurement/measurements.html',
                        controller: 'MeasurementController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('measurement');
                        return $translate.refresh();
                    }]
                }
            })
            .state('measurementDetail', {
                parent: 'entity',
                url: '/measurement/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'awarenessApp.measurement.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/measurement/measurement-detail.html',
                        controller: 'MeasurementDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('measurement');
                        return $translate.refresh();
                    }]
                }
            });
    });
