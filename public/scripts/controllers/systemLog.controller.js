/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    systemLog.controller('systemLogController', ['$scope', '$mdDialog', '$http', '$location', 'auth',
        function ($scope, $mdDialog, $http, $location, auth) {

            $scope.currentButtons = {};
            auth.checkPermissions($location.path(), function (currentButtons) {
                $scope.currentButtons = currentButtons;
            });

            $scope.items = [];

            $scope.facet = {};
            $scope.operaTypes = ['新建','修改','删除','禁用','启用','扣车','解扣'];
            $scope.modules = [
                {module:'', name:'全部'},
                {module:'person', name:'人员'},
                {module:'bankCard', name:'银行卡'},
                {module:'vehicle', name:'车辆'}
            ];

            $scope.paging = {
                total: 0,
                current: 1,
                onPageChanged: findAllSystemLog
            };

            function findAllSystemLog() {
                var url = '/systemLog/' + $scope.paging.current + '/' + PAGESIZE;
                url += '?jsonFilter=' + encodeURIComponent(JSON.stringify($scope.facet));
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.items = responseData.data.content;
                    $scope.paging.total = responseData.data.totalPages;
                    $scope.paging.totalElements = responseData.data.totalElements;
                    $scope.selected = [];
                });
            }

            $scope.searchSystemLogs = findAllSystemLog;
            findAllSystemLog();

            $scope.openDatePopup = function(popup) {
                $scope.datePopup[popup] = true;
            };

            $scope.datePopup = {
                GTE_createdDate: false,
                LTE_createdDate: false
            };
        }]);
})();
