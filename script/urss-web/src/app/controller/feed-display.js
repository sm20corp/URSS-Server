'use strict';

/**
   * @memberof app
   * @ngdoc controller
   * @name feedListCtrl
   * @param $scope {service} Controller scope
   * @param $http {service} Http service
   * @param $window {service} Reference to browser window object
   * @param $q {service} A service that helps you run functions asynchronously and use their return values (or exceptions) when they are done processing
   * @param $sce {service} Service that provides Strict Contextual Escaping services to AngularJS
   */

angular.module('urssApp').controller('feedListCtrl', function($scope, $http, $window, $q, $sce) {
    $scope.articles = [];
    $scope.error = "false";
    $scope.test = function(newSelectedArticle) {
      $scope.selectedArticle = $sce.trustAsResourceUrl(newSelectedArticle);
    }
    /**
      * getFeed function
      * Make a POST request to get the id of the feed and then we make a call to get the articles
      * @memberof feedListCtrl
      */
    $scope.getFeed = function() {
      $scope.error = "false";
        if ($scope.feedUrl) {

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
                var feedId2 = response.data._id;

                if (feedId) {
                  getFeedArticles(feedId);
                }
                else {
                  getFeedArticles(feedId2);
                  }
            }, function myError(response) {
                $scope.error = "true";
                $scope.articles = [];

            });
        }
    }
    /**
      * getFeedArticles
      * Make a GET request to get all the articles of the feed in an array
      * @memberof feedListCtrl
      * @param {String} feedId id of the selected feed
      */
    function getFeedArticles(feedId) {
        var url = "http://79.137.78.39:4242/api/feeds/" + feedId;
        $http({
            url: url,
            method: "GET",
            headers: {
                'Content-Type': 'application/json'
            },
            data: {
                'url': $scope.feedUrl,
            }
        }).then(function mySucces(response) {
            var articlesIdArray = response.data.articles;

            getArticlesInfo(articlesIdArray);

        }, function myError(response) {


        });
    }
    /**
      * get articles info
      * chain GET request for all articles in order to get their content
      * use $q to know when all the request are finished
      * @memberof feedListCtrl
      * @param {Array} articlesIdArray array of articles
      */
    function getArticlesInfo(articlesIdArray) {
        var arr = [];

        for (var a = 0; a < articlesIdArray.length; ++a) {
          var url = "http://79.137.78.39:4242/api/articles/" + articlesIdArray[a];
            arr.push($http({
                url: url,
                method: "GET",
                headers: {
                    'Content-Type': 'application/json'
                }
            }));
        }

        $q.all(arr).then(function(ret) {
          $scope.articles = ret;
          

        });
    }
});
