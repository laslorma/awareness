'use strict';

angular.module('awarenessApp')
    .controller('FrequencyController', function ($scope, Frequency, Habit) {
        $scope.frequencys = [];
        $scope.habits = Habit.query();
        $scope.loadAll = function() {
            Frequency.query(function(result) {
               $scope.frequencys = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Frequency.get({id: id}, function(result) {
                $scope.frequency = result;
                $('#saveFrequencyModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.frequency.id != null) {
                Frequency.update($scope.frequency,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Frequency.save($scope.frequency,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Frequency.get({id: id}, function(result) {
                $scope.frequency = result;
                $('#deleteFrequencyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Frequency.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFrequencyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveFrequencyModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.frequency = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
