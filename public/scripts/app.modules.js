var app = angular.module('app', [
    'ngRoute',
    'ngMaterial',
    'test',
    'auth',
    'login',
    'menuList'
]);
var test = angular.module('test', []);
var auth = angular.module('auth', []);
var login = angular.module('login', ['auth']);
var menuList = angular.module('menuList', []);
var user = angular.module('user', []);
