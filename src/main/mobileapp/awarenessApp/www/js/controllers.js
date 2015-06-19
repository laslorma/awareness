function Entry (date, counter){

          // Add object properties like this
          this.date = date;
          this.counter = counter;

       };

angular.module('starter.controllers', [])

    .controller('DashCtrl', function($scope) {})

    .controller('ChatsCtrl', function($scope, Chats) {
        $scope.chats = Chats.all();
        $scope.remove = function(chat) {
            Chats.remove(chat);
        }
    })

    .controller('ChatDetailCtrl', function($scope, $stateParams, Chats) {
        $scope.chat = Chats.get($stateParams.chatId);
        $scope.counter= 3;



        $scope.historyArray = [];

        $scope.addToCounter = function(){



            $scope.counter = $scope.counter +1;

            var entry = new Entry(new Date(),$scope.counter);
            console.log(entry);
            $scope.historyArray.push(entry);

        };

        $scope.substractToCounter = function(){

            if ($scope.counter>0)
                $scope.counter = $scope.counter -1;

        }

    })

    .controller('AccountCtrl', function($scope) {
        $scope.settings = {
            enableFriends: true
        };
    });
