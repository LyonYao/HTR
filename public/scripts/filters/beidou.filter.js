/**
 * Created by Xinlin on 2017/19/8.
 */
(function () {
    'use strict';

    beidouRecord.filter('isChangeCard', function () {
        return function (changeCardType) {
            if(changeCardType === 2){
                return "未换直接续";
            } else if(changeCardType === 0) {
                return "换新卡";
            } else {
                return "借流量";
            }
        };
    })
        .filter('isChangeTerminal', function () {
            return function (changeTerminal) {
                if(changeTerminal){
                    return "是";
                } else {
                    return "否";
                }
            };
        })

        .filter('showCardNum', function () {
            return function (beidouRecord) {
                if(beidouRecord.borrowCardFlow){
                    return beidouRecord.oldCardNum + "借流量" + beidouRecord.newCardNum;
                } else {
                    return beidouRecord.newCardNum;
                }
            };
        });
})();