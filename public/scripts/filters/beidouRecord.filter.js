/**
 * Created by Xinlin on 2017/19/8.
 */
(function () {
    'use strict';

    beidouRecord.filter('leftDays', function () {
        return function (expireTime) {
           return moment(expireTime, "YYYY/MM/DD").fromNow();
        };
    });
})();