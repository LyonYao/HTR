/**
 * Created by Xinlin on 2017/7/30.
 */
(function () {
    'use strict';

    login.controller('loginController', ['$scope', 'auth', function ($scope, auth) {
            var self = this;
            self.authenticated = function() {
                return auth.authenticated;
            };

            $scope.credentials = {};

            $scope.login = function() {
                auth.authenticate($scope.credentials, function(authenticated) {
                    if (authenticated) {
                        console.log("Login succeeded");
                        self.error = false;
                    } else {
                        console.log("Login failed");
                        self.error = true;
                    }
                });
            };
        }]);
})();
