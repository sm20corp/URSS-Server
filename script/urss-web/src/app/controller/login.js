'use strict';

/**
   * @memberof app
   * @ngdoc controller
   * @description
   * Handles application credential creation and authentification.
   * @name loginCtrl
   * @param $scope {service} Controller scope
   * @param $http {service} Http service
   * @param $window {service} Reference to browser window object
   * @param $location {service} Service that parses the URL in the browser address bar (based on the window.location) and makes the URL available to your application
   */
angular.module('urssApp').controller('loginCtrl', function($scope, $http, $window, $location) {
    $scope.emailFormat = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
    $scope.error = "false";

    /**
      * Login function making HTTP Post request to send login info
      * If success we store the token and the username, and we update the login state
      * If failure we display the error for the user
      * @memberof loginCtrl
      */
    $scope.login = function() {
      $scope.error = "false";

            $http({
                url: 'http://79.137.78.39:4242/auth/local',
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {
                    'email': $scope.username,
                    'password': $scope.password
                }
            }).then(function mySucces(response) {
                var accessToken = response.data.token;
                var userId = response.data.userId;

                $window.localStorage['is-logged'] = "true";
                $window.localStorage['access-token'] = accessToken;
                $window.localStorage['username'] = $scope.username;
                $window.localStorage['user-id'] = userId;
                $location.path("/main");
            }, function myError(response) {
                $scope.error = "true";

            });

    };
    /**
      * Subscribe function making HTTP Post request to send subscribe info
      * If success we store the token and move to login page
      * Contains a directive that check that the two passwords matches
      * @memberof loginCtrl
      */
    $scope.subscribe = function() {
        

        $http({
            url: 'http://79.137.78.39:4242/api/users/createAccount',
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            data: {
                'email': $scope.username,
                'password': $scope.password
            }
        }).then(function mySucces(response) {
            var accessToken = response.data.id;

            $window.localStorage['access-token'] = accessToken;
            $location.path("/login");
        }, function myError(response) {


        });
    };
}).directive("passwordVerify", function() {
    return {
        require: "ngModel",
        scope: {
            passwordVerify: '='
        },
        link: function(scope, element, attrs, ctrl) {
            scope.$watch(function() {
                var combined;

                if (scope.passwordVerify || ctrl.$viewValue) {
                    combined = scope.passwordVerify + '_' + ctrl.$viewValue;
                }
                return combined;
            }, function(value) {
                if (value) {
                    ctrl.$parsers.unshift(function(viewValue) {
                        var origin = scope.passwordVerify;
                        if (origin !== viewValue) {
                            ctrl.$setValidity("passwordVerify", false);
                            return undefined;
                        } else {
                            ctrl.$setValidity("passwordVerify", true);
                            return viewValue;
                        }
                    });
                }
            });
        }
    };
});
