var app = angular.module('app', [
    'ngRoute',
    'ngMaterial',
    'ui.bootstrap',
    'cl.paging',
    'test',
    'auth',
    'login',
    'user',
    'menuList',
    'vehicle',
    'ngMessages',
    'person',
    'bankCard',
    'systemLog',
    'loanInfo'
]);
var test = angular.module('test', []);
var auth = angular.module('auth', []);
var login = angular.module('login', ['auth']);
var menuList = angular.module('menuList', []);
var user = angular.module('user', []);
var vehicle = angular.module('vehicle', []);
var person = angular.module('person', []);
var bankCard = angular.module('bankCard', []);
var systemLog = angular.module('systemLog', []);
var loanInfo = angular.module('loanInfo', []);
