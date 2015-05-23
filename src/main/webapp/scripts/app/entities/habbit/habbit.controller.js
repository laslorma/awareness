'use strict';

angular.module('awarenessApp')
    .controller('HabbitController', function ($scope, Habbit, User, ParseLinks) {
        $scope.habbits = [];
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Habbit.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.habbits.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.habbits = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Habbit.get({id: id}, function(result) {
                $scope.habbit = result;
                $('#saveHabbitModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.habbit.id != null) {
                Habbit.update($scope.habbit,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Habbit.save($scope.habbit,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Habbit.get({id: id}, function(result) {
                $scope.habbit = result;
                $('#deleteHabbitConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Habbit.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteHabbitConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveHabbitModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.habbit = {description: null, type: null, frequency: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
