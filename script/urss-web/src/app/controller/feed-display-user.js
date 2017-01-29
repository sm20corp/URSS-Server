/**
 * @memberof app
 * @ngdoc controller
 * @name feedListUserCtrl
 * @param $scope {service} Controller scope
 * @param $http {service} Http service
 * @param $window {service} Reference to browser window object
 * @param $q {service} A service that helps you run functions asynchronously and use their return values (or exceptions) when they are done processing
 * @param $sce {service} Service that provides Strict Contextual Escaping services to AngularJS
 * @param $location {service} Service that parses the URL in the browser address bar (based on the window.location) and makes the URL available to your application
 */

'use strict';

angular.module('urssApp').controller('feedListUserCtrl', function($scope, $http, $window, $q, $sce, $location) {
    $scope.username = "Username";
    $scope.test = function(newSelectedArticle) {
        $scope.selectedArticle = $sce.trustAsResourceUrl(newSelectedArticle);
    }
    $scope.articles = [];
    var token;
    var userId;
    var history;
    var starredArticles;
    var unreadArticles;
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
         * Select article as favorite
         * @memberof feedListUserCtrl
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
                    console.log("success add feed to history");
                    getHistoryWithUserId(history);

                }, function myError(response) {
                    $scope.error = "true";
                    console.log("Fail get feed" + response.statusText);
                });
            }
        }
        /**
         * Select article as unread
         * @memberof feedListUserCtrl
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
                      console.log("success add feed to history");
                      getHistoryWithUserId(history);

                  }, function myError(response) {
                      $scope.error = "true";
                      console.log("Fail get feed" + response.statusText);
                  });
              }
          }
        /**
         * signOut function
         * we change login state and change the page to login
         * @memberof feedListUserCtrl
         */
    $scope.signOut = function() {
            $window.localStorage['is-logged'] = "false";
            $location.path("/login");
        }
        /**
         * Init function when the page is loading
         * we get the userid of the user and the token
         * and we make an http request get to get user info
         * @memberof feedListUserCtrl
         */
    var init = function() {
        $scope.username = $window.localStorage['username'];
        token = $window.localStorage['access-token'];
        userId = $window.localStorage['user-id'];
        //console.log($scope.username + "-" + token + "-" + userId);
        token = 'Bearer ' + token;
        var url = 'http://79.137.78.39:4242/api/users/' + userId;
        console.log(token);
        $http({
            url: url,
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token,
            }
        }).then(function mySucces(response) {
            history = response.data.history;
            console.log("history = " + history);
            getHistoryWithUserId(history);

        }, function myError(response) {

            console.log("Fail" + response.statusText);
        });
    }
    init();

    /**
     * getHistoryWithUserId function
     * Make a get request to get the history so we can have the feed list
     * @memberof feedListUserCtrl
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
            console.log("history = " + bookmarks);
            getFeeds(bookmarks);
        }, function myError(response) {

            console.log("Fail" + response.statusText);
        });
    }

    /**
     * getArticlesFromFeeds function
     * Make a get request to get the articles from the feed list
     * @memberof feedListUserCtrl
     * @param {String} feeds id array of the feeds
     */
    function getFeeds(feeds) {
        var arr = [];

        for (var a = 0; a < feeds.length; ++a) {
            console.log("the feed =" + feeds[a]);
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
            console.log(ret[0]);
            getArticlesFromFeeds(ret);
        });
    }


    /**
     * getArticlesFromFeeds function
     * Make a get request to get the articles from the feed list
     * @memberof feedListUserCtrl
     * @param {String} feeds id array of the feeds
     */
    function getArticlesFromFeeds(feeds) {
        var arr = [];
        console.log(feeds.length);
        for (var a = 0; a < feeds.length; ++a) {
            console.log("the new feed =" + feeds[a].data.articles);
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
            console.log(ret[0]);
            $scope.articles = ret;
        });
    }
    /**
     * getFeed function
     * Make a POST request to get the id of the feed and then we make a call to get the articles
     * @memberof feedListUserCtrl
     */
    $scope.getFeed = function() {
            $scope.error = "false";
            if ($scope.feedUrl) {
                console.log("new feed " + $scope.feedUrl);
                $http({
                    url: 'http://79.137.78.39:4242/api/feeds/fromURL',
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    data: {
                        'url': $scope.feedUrl,
                    }
                }).then(function mySucces(response) {
                    var feedId = response.data.id;
                    console.log("success{" + feedId + "}");
                    addFeedToHistory(feedId);

                }, function myError(response) {
                    $scope.error = "true";
                    console.log("Fail get feed" + response.statusText);
                });
            }
        }
        /**
         * addFeedToHistory function
         * Make a PUT request to add the feed to the history so we can have the feed list
         * @memberof feedListUserCtrl
         * @param {String} feedId id of the selected feed
         */
    function addFeedToHistory(feedId) {
        var url = 'http://79.137.78.39:4242/api/users/bookmarkFeed/' + feedId;
        $http({
            url: url,
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then(function mySucces(response) {
            console.log("success add feed to history");
            getHistoryWithUserId(history);

        }, function myError(response) {
            $scope.error = "true";
            console.log("Fail get feed" + response.statusText);
        });
    }
});
