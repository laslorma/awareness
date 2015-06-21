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


        if (localStorage.getItem("historyArray") != null) {
            $scope.historyArray = JSON.parse(window.localStorage.getItem('historyArray'));
        }else{
            $scope.historyArray = [];
        }




        $scope.addToCounter = function(){

          // var entry = new Entry(new Date(),$scope.historyArray.length+1);

            $scope.historyArray.push(new Date());


            window.localStorage.setItem("historyArray", JSON.stringify($scope.historyArray));



        };

        $scope.substractToCounter = function(){

            if ($scope.historyArray.length > 0)   {
                $scope.historyArray.pop();
                window.localStorage.setItem("historyArray", JSON.stringify($scope.historyArray));
            }
        }

    })

    .controller('AccountCtrl', function($scope) {
        $scope.settings = {
            enableFriends: true
        };
    });
