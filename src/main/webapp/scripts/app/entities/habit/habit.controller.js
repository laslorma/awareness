'use strict';

angular.module('awarenessApp')
    .controller('HabitController', function ($scope, Habit, Frequency, Type, Measurement, User, ParseLinks) {
        $scope.habits = [];
        $scope.frequencys = Frequency.query();
        $scope.types = Type.query();
        $scope.measurements = Measurement.query();
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Habit.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.habits = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Habit.get({id: id}, function(result) {
                $scope.habit = result;
                $('#saveHabitModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.habit.id != null) {
                Habit.update($scope.habit,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Habit.save($scope.habit,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Habit.get({id: id}, function(result) {
                $scope.habit = result;
                $('#deleteHabitConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Habit.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteHabitConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveHabitModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.habit = {name: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
