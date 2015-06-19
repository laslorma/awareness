angular.module('starter.services', [])

.factory('Chats', function() {
  // Might use a resource here that returns a JSON array

  // Some fake testing data
  var chats = [{
    id: 0,
    name: 'Water ',
    lastText: 'Cups per day',
    face: 'http://www.cleanacwa.org/assets/images/2df62839.waterpic.gif'
  }, {
    id: 1,
    name: 'Complaints',
    lastText: 'Complaints per day',
    face: 'https://www.fbpe.org/images/jdownloads/catimages/icon-complaint.gif'
  }];

  return {
    all: function() {
      return chats;
    },
    remove: function(chat) {
      chats.splice(chats.indexOf(chat), 1);
    },
    get: function(chatId) {
      for (var i = 0; i < chats.length; i++) {
        if (chats[i].id === parseInt(chatId)) {
          return chats[i];
        }
      }
      return null;
    }
  };
});
