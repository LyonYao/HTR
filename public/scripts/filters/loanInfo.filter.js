/**
 * Created by Xinlin on 2017/19/8.
 */
(function () {
    'use strict';

    bankCard.filter('currentLoanNum', function () {
        return function (LoanNum) {
            if (LoanNum) {
                return '第 ' + LoanNum + ' 期';
            }
        };
    });

    bankCard.filter('isCompleted', function () {
        return function (completed) {
            if (completed) {
                return "已完结";
            } else {
                return "进行中";
            }
        };
    });

    bankCard.filter('isOverdue', function () {
        return function (overdue) {
            if (overdue >= 0) {
                return overdue + ' 天';
            } else {
                return '逾期 ' + Math.abs(overdue) + ' 天';
            }
        };
    });
})();