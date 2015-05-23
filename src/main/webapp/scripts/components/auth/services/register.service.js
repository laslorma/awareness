'use strict';

angular.module('awarenessApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


