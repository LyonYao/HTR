var app = angular.module('app', [
    'ngRoute',
    'ngMaterial',
    'test',
    'auth',
    'login',
    'menuList',
    'vehicle',
    'ngMessages',
    'person',
]);
var test = angular.module('test', []);
var auth = angular.module('auth', []);
var login = angular.module('login', ['auth']);
var menuList = angular.module('menuList', []);
var user = angular.module('user', []);
var vehicle = angular.module('vehicle', []);
var person = angular.module('person', []);
