/**
   * @memberof app
   * @ngdoc controller
   * @name unreadFeedCtrl
   * @param $scope {service} Controller scope
   * @param $http {service} Http service
   * @param $window {service} Reference to browser window object
   * @param $q {service} A service that helps you run functions asynchronously and use their return values (or exceptions) when they are done processing
   * @param $sce {service} Service that provides Strict Contextual Escaping services to AngularJS
   * @param $location {service} Service that parses the URL in the browser address bar (based on the window.location) and makes the URL available to your application
   */

'use strict';

angular.module('urssApp').controller('unreadFeedCtrl', function($scope, $http, $window, $q, $sce, $location) {
  $scope.articleSelected =  function(user){
    return $.inArray(user, $scope.selectedUser) > -1;
}
  $scope.username = "Username";
  var token;
  var userId;
  var history;
  var starredArticles;
  var unreadArticles;
  $scope.articles = [];
  /**
   * Select article as favorite
   * @memberof unreadFeedCtrl
   */
$scope.selectFavorite = function(articleId) {
      if ($scope.articleSelected(articleId) == -1) {
          var url = 'http://79.137.78.39:4242/api/users/starArticle/' + articleId;
          $http({
              url: url,
              method: "PUT",
              headers: {
                  'Content-Type': 'application/json',
                  'Authorization': token
              }
          }).then(function mySucces(response) {

              getHistoryWithUserId(history);

          }, function myError(response) {
              $scope.error = "true";

          });
      }
  }
  /**
   * Select article as unread
   * @memberof unreadFeedCtrl
   */
  $scope.selectUnread = function(articleId) {
        if ($scope.unreadSelected(articleId) == -1) {
            var url = 'http://79.137.78.39:4242/api/users/viewArticle/' + articleId;
            $http({
                url: url,
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            }).then(function mySucces(response) {

                getHistoryWithUserId(history);

            }, function myError(response) {
                $scope.error = "true";

            });
        }
    }
  $scope.articleSelected = function(user) {
          if (starredArticles.indexOf(user) !== -1) {
              return (1);
          } else {
              return (-1);
          }
      }
      $scope.unreadSelected = function(user) {
              if (unreadArticles.indexOf(user) !== -1) {
                  return (1);
              } else {
                  return (-1);
              }
          }
  /**
    * Init function when the page is loading
    * we get the userid of the user and the token
    * and we make an http request get to get user info
    * @memberof unreadFeedCtrl
    */
  var init = function() {
      $scope.username = $window.localStorage['username'];
      token = $window.localStorage['access-token'];
      userId = $window.localStorage['user-id'];
      token = 'Bearer ' + token;
      var url = 'http://79.137.78.39:4242/api/users/' + userId;

      $http({
          url: url,
          method: "GET",
          headers: {
              'Content-Type': 'application/json',
              'Authorization': token,
          }
      }).then(function mySucces(response) {
          history = response.data.history;

          getHistoryWithUserId(history);

      }, function myError(response) {


      });
  }
  init();

  /**
   * getHistoryWithUserId function
   * Make a get request to get the history so we can have the feed list
   * @memberof unreadFeedCtrl
   * @param {String} userId id of the selected user
   */
  function getHistoryWithUserId(userId) {
      var url = 'http://79.137.78.39:4242/api/histories/' + userId;
      $http({
          url: url,
          method: "GET",
          headers: {
              'Content-Type': 'application/json',
              'Authorization': token,
          }
      }).then(function mySucces(response) {
          var bookmarks = response.data.bookmarks;
          starredArticles = response.data.starredArticles;
          unreadArticles = response.data.viewedArticles;

          getFeeds(bookmarks);
      }, function myError(response) {


      });
  }

  /**
   * getArticlesFromFeeds function
   * Make a get request to get the articles from the feed list
   * @memberof unreadFeedCtrl
   * @param {String} feeds id array of the feeds
   */
  function getFeeds(feeds) {
      var arr = [];

      for (var a = 0; a < feeds.length; ++a) {

          var url = "http://79.137.78.39:4242/api/feeds/" + feeds[a];
          arr.push($http({
              url: url,
              method: "GET",
              headers: {
                  'Content-Type': 'application/json'
              }
          }));
      }

      $q.all(arr).then(function(ret) {
          //$scope.articles = ret;

          getArticlesFromFeeds(ret);
      });
  }


  /**
   * getArticlesFromFeeds function
   * Make a get request to get the articles from the feed list
   * @memberof unreadFeedCtrl
   * @param {String} feeds id array of the feeds
   */
  function getArticlesFromFeeds(feeds) {
      var arr = [];

      for (var a = 0; a < feeds.length; ++a) {

          var articleArray = feeds[a].data.articles;
          for (var b = 0; b < articleArray.length; ++b) {


              var url = "http://79.137.78.39:4242/api/articles/" + articleArray[b];
              arr.push($http({
                  url: url,
                  method: "GET",
                  headers: {
                      'Content-Type': 'application/json'
                  }
              }));
          }
      }

      $q.all(arr).then(function(ret) {
          
          $scope.articles = ret;
      });
  }

  /**
    * signOut function
    * we change login state and change the page to login
    * @memberof unreadFeedCtrl
    */
  $scope.signOut = function() {
      $window.localStorage['is-logged'] = "false";
      $location.path("/login");
  }
});
