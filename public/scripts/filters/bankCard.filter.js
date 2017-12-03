/**
 * Created by Xinlin on 2017/19/8.
 */
(function () {
    'use strict';

    bankCard.filter('isActive', function () {
            return function (surety) {
                if(surety){
                    return "未禁用";
                } else {
                    return "已禁用";
                }
            };
        });
})();