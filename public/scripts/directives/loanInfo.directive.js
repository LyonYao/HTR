/**
 * Created by Xinlin on 2015/12/4.
 */
(function () {
    'use strict';

    loanInfo.directive('loanRecords', ['$filter', function ($filter) {
        return {
            restrict: 'E',
            link: function (scope, element) {
                var html = '<table  class="table table-bordered"><thead><tr><th class="col-sm-1">期数</th>' +
                    '<th class="col-sm-1">金额</th><th class="col-sm-1">还款时间</th><th class="col-sm-2">银行卡</th>' +
                    '<th class="col-sm-1">收款人</th><th class="col-sm-4">备注说明</th>' +
                    '</tr></thead><tbody>';
                angular.forEach(scope.loanInfo.loanRecords, function (loanRecord, index) {
                    if ('subLoanRecords' in loanRecord) {
                        var length = loanRecord.subLoanRecords.length;
                        angular.forEach(loanRecord.subLoanRecords, function (subLoanRecord, index) {
                            html += '<tr>';
                            if (length > 1 && index === 0) {
                                html += '<td rowspan="' + length + '">第 ' + loanRecord.loanNum + ' 期</td>';
                            }
                            if (length === 1) {
                                html += '<td>第 ' + loanRecord.loanNum + ' 期</td>';
                            }
                            html += '<td>' + $filter('currency')(subLoanRecord.receipts) + '</td>' +
                                '<td>' + subLoanRecord.receiptDate + '</td>' +
                                '<td>' + subLoanRecord.bankCard.cardNumber + '</td>' +
                                '<td>' + subLoanRecord.payee.userName + '</td>' +
                                '<td>' + subLoanRecord.description + '</td>' +
                                '</tr>';
                        });
                    }
                });
                html += '</tbody></table>';
                element.replaceWith(html);
            }
        };
    }]);

})();
