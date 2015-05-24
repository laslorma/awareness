'use strict';

angular.module('awarenessApp')
    .controller('MeasurementDetailController', function ($scope, $stateParams, Measurement, Habit) {
        $scope.measurement = {};
        $scope.load = function (id) {
            Measurement.get({id: id}, function(result) {
              $scope.measurement = result;
            });
        };
        $scope.load($stateParams.id);
    });
