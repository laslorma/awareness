'use strict';

angular.module('awarenessApp')
    .controller('FrequencyDetailController', function ($scope, $stateParams, Frequency, Habit) {
        $scope.frequency = {};
        $scope.load = function (id) {
            Frequency.get({id: id}, function(result) {
              $scope.frequency = result;
            });
        };
        $scope.load($stateParams.id);
    });
