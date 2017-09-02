(function () {
    'use strict';

    app.factory('auth', [
        '$rootScope',
        '$http',
        '$location',
        '$window',
        function ($rootScope, $http, $location, $window) {

            var auth = {

                authenticated: false,

                loginPath: '/login',
                logoutPath: '/logout',
                homePath: '/',
                path: $location.path(),

                authenticate: function (credentials, callback) {

                    var headers = credentials && credentials.username ? {
                        authorization: "Basic " + btoa(credentials.username + ":" + credentials.password)
                    } : {};

                    $http.get('loginUser', {
                        headers: headers
                    }).then(function (response) {
                        if (response.data.name) {
                            auth.authenticated = true;
                            $rootScope.$broadcast('changeLogin', true);
                        } else {
                            auth.authenticated = false;
                        }
                        callback(auth.authenticated);
                        $location.path(auth.path == auth.loginPath ? auth.homePath : auth.path);
                    }, function () {
                        auth.authenticated = false;
                        callback(false);
                    });

                },

                checkPermissions: function (subUrl, callback) {
                    var currentButtons = {};
                    $http.get('/resource/currentRes', {
                    }).then(function (response) {
                        for (var i in response.data) {
                            for (var j in response.data[i].childrenRes){
                                if(response.data[i].childrenRes[j].resPath === subUrl.substr(1)) {
                                    checkButtonsPermission(response.data[i].childrenRes[j], currentButtons);
                                    callback(currentButtons);
                                    return;
                                }
                            }
                        }
                        callback(null);
                    }, function (response) {
                        if(response.status === 400){
                            $window.location.reload();
                        }
                    });
                },

                clear: function () {
                    $location.path(auth.loginPath);
                    auth.authenticated = false;
                    $http.post(auth.logoutPath, {}).then(function () {
                        console.log("Logout succeeded");
                    }, function () {
                        console.log("Logout failed");
                    });
                },

                init: function (homePath, loginPath, logoutPath) {

                    auth.homePath = homePath;
                    auth.loginPath = loginPath;
                    auth.logoutPath = logoutPath;

                    auth.authenticate({}, function (authenticated) {
                        if (authenticated) {
                            $location.path(auth.path);
                        }
                    });
                }

            };
            
            function checkButtonsPermission(currentRes, currentButtons) {
                if (currentRes === null || currentRes.selected !== true){
                    $location.path(auth.homePath);
                } else {
                    angular.forEach(currentRes.childrenRes, function (button) {
                        if(button.selected){
                            angular.forEach(button, function (value, key) {
                                if(key === 'resPath'){
                                    currentButtons[value] = {
                                        show:true,
                                        name:button.resourceName
                                    };
                                }
                            });
                        }
                    });
                }
            }

            return auth;
        }]);
})();