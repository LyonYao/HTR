(function () {
    'use strict';

    angular.module('MasterApp.test', []);
    angular.module('MasterApp.auth', []);
    angular.module('MasterApp.login', ['MasterApp.auth']);
})();
