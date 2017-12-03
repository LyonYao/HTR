/**
 * Created by Xinlin on 2017/19/8.
 */
(function () {
    'use strict';

    systemLog.filter('translateModules', function () {
            return function (modules) {
                switch (modules){
                    case 'person':
                        return '人员';
                    case 'bankCard':
                        return '银行卡';
                    case 'user':
                        return '用户';
                    case 'role':
                        return '角色';
                    case 'vehicle':
                        return '车辆';
                    case 'loanInfo':
                        return '档案';
                    // default:
                    //     return '未知模块';
                }
            };
        });
})();