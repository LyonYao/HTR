var app = angular.module('app', [
    'ngRoute',
    'ngMaterial',
    'test',
    'auth',
    'login'
]);
var test = angular.module('test', []);
var auth = angular.module('auth', []);
var login = angular.module('login', ['auth']);

