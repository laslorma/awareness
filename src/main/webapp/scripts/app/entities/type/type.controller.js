'use strict';

angular.module('awarenessApp')
    .controller('TypeController', function ($scope, Type, Habit) {
        $scope.types = [];
        $scope.habits = Habit.query();
        $scope.loadAll = function() {
            Type.query(function(result) {
               $scope.types = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Type.get({id: id}, function(result) {
                $scope.type = result;
                $('#saveTypeModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.type.id != null) {
                Type.update($scope.type,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Type.save($scope.type,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Type.get({id: id}, function(result) {
                $scope.type = result;
                $('#deleteTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Type.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveTypeModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.type = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
