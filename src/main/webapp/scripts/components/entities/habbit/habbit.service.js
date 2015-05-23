'use strict';

angular.module('awarenessApp')
    .factory('Habbit', function ($resource) {
        return $resource('api/habbits/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
