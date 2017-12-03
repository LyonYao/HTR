/**
 * Created by Xinlin on 2017/19/8.
 */
(function () {
    'use strict';

    person.filter('phoneNums', function () {
        return function (phoneInfos) {
            if (!phoneInfos) return;
            var phoneNums = '';
            angular.forEach(phoneInfos, function (phoneInfo) {
                phoneNums = phoneNums + phoneInfo.phoneNum + phoneInfo.description + '/';
            });
            return phoneNums.substr(0, phoneNums.length - 1);
        };
    })
        .filter('isSurety', function () {
            return function (surety) {
                if(surety){
                    return "担保人";
                } else {
                    return "贷款人";
                }
            };
        });
})();