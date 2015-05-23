'use strict';

angular.module('awarenessApp')
    .controller('TypeDetailController', function ($scope, $stateParams, Type, Habit) {
        $scope.type = {};
        $scope.load = function (id) {
            Type.get({id: id}, function(result) {
              $scope.type = result;
            });
        };
        $scope.load($stateParams.id);
    });
