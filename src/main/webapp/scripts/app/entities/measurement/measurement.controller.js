'use strict';

angular.module('awarenessApp')
    .controller('MeasurementController', function ($scope, Measurement, Habit, ParseLinks) {
        $scope.measurements = [];
        $scope.habits = Habit.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Measurement.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.measurements = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Measurement.get({id: id}, function(result) {
                $scope.measurement = result;
                $('#saveMeasurementModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.measurement.id != null) {
                Measurement.update($scope.measurement,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Measurement.save($scope.measurement,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Measurement.get({id: id}, function(result) {
                $scope.measurement = result;
                $('#deleteMeasurementConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Measurement.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMeasurementConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveMeasurementModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.measurement = {value: null, date: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
