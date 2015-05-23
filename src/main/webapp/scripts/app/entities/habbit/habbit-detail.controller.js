'use strict';

angular.module('awarenessApp')
    .controller('HabbitDetailController', function ($scope, $stateParams, Habbit, User) {
        $scope.habbit = {};
        $scope.load = function (id) {
            Habbit.get({id: id}, function(result) {
              $scope.habbit = result;
            });
        };
        $scope.load($stateParams.id);
    });
