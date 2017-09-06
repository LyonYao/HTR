var app = angular.module('app', [
    'ngRoute',
    'ngMaterial',
    'ui.bootstrap',
    'cl.paging',
    'test',
    'auth',
    'login',
    'user',
    'role',
    'vehicle',
    'ngMessages',
    'person',
    'bankCard',
    'systemLog',
    'loanInfo',
    'subLoanRecord',
    'ngIdle',
    'beidouRecord'
]);
var test = angular.module('test', []);
var auth = angular.module('auth', []);
var login = angular.module('login', ['auth']);
var user = angular.module('user', []);
var role = angular.module('role', []);
var vehicle = angular.module('vehicle', []);
var person = angular.module('person', []);
var bankCard = angular.module('bankCard', []);
var systemLog = angular.module('systemLog', []);
var loanInfo = angular.module('loanInfo', []);
var subLoanRecord = angular.module('subLoanRecord', []);
var beidouRecord = angular.module('beidouRecord', []);