'use strict';

angular.module('awarenessApp')
    .factory('Frequency', function ($resource) {
        return $resource('api/frequencys/:id', {}, {
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
