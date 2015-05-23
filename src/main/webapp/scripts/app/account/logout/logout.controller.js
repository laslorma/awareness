'use strict';

angular.module('awarenessApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
