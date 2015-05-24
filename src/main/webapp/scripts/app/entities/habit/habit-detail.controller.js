'use strict';

angular.module('awarenessApp')
    .controller('HabitDetailController', function ($scope, $stateParams, Habit, Frequency, Type, Measurement, User) {
        $scope.habit = {};
        $scope.load = function (id) {
            Habit.get({id: id}, function(result) {
              $scope.habit = result;
            });
        };
        $scope.load($stateParams.id);
    });
