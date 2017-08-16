(function () {
    'use strict';

// Declare app level module which depends on views, and components
    app.config(['$routeProvider','$mdThemingProvider','$locationProvider','$mdIconProvider', '$httpProvider',
        function ($routeProvider, $mdThemingProvider, $locationProvider, $mdIconProvider, $httpProvider) {

        // $locationProvider.html5Mode(true);

        $routeProvider.otherwise({redirectTo: '/'});
        $routeProvider.when('/test', {
            templateUrl: 'views/test.html',
            controller: 'testController'
        }).when('/login', {
            templateUrl: 'views/login.html',
            controller: 'loginController'
        }).when('/sys/menuList', {
            templateUrl: 'views/menuList.html',
            controller: 'menuListController'
        }).when('/sys/userList', {
            templateUrl: 'views/user.html',
            controller: 'userController'
        }).when('/loan/vehicle', {
            templateUrl: 'views/vehicle.html',
            controller: 'vehicleController'
        }).when('/loan/person', {
            templateUrl: 'views/person.html',
            controller: 'personController'



        // }).when('/tableDataList', {
        //     templateUrl: 'partials/tableDataList.html',
        //     controller: 'tableDataListController'
        // }).when('/dataList/:tableName', {
        //     templateUrl: 'partials/dataList.html',
        //     controller: 'dataListController'
        // }).when('/showDataChain/table/:tableName/rowKey/:rowKey/page/:currentPage', {
        //     templateUrl: 'partials/showDataChain.html',
        //     controller: 'showDataChainController'
        });

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

            $mdThemingProvider.theme('default')
                .primaryPalette('blue')
                .accentPalette('teal')
                .warnPalette('red')
                .backgroundPalette('grey');

            $mdThemingProvider.alwaysWatchTheme(true);


        $mdIconProvider.icon('md-toggle-arrow', 'img/icons/toggle-arrow.svg', 48);


    }])
        .run(['auth', '$rootScope','$location', function(auth, $rootScope,$location) {

            function enter() {
                if ($location.path() != auth.loginPath) {
                    auth.path = $location.path();
                    if (!auth.authenticated) {
                        $location.path(auth.loginPath);
                    }
                }
            }

            // Guard route changes and switch to login page if unauthenticated
            $rootScope.$on('$routeChangeStart', function() {
                enter();
            });

            // Initialize auth module with the home page and login/logout path
            // respectively
            auth.init('/', '/login', '/logout');



    }]);
})();