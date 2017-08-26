/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    loanInfo.controller('loanInfoController', ['$scope', '$mdDialog', '$http', '$mdToast',
        function ($scope, $mdDialog, $http, $mdToast) {

            $scope.items = [];
            $scope.selected = [];

            $scope.facet = {};
            $scope.suretys = [{surety: '', name: '全部'}, {surety: 0, name: '贷款人'}, {surety: 1, name: '担保人'}];

            $scope.paging = {
                total: 0,
                current: 1,
                onPageChanged: findAllLoanInfo
            };

            function findAllLoanInfo() {
                var url = '/loanInfo/' + $scope.paging.current + '/' + PAGESIZE;
                url += '?jsonFilter=' + encodeURIComponent(JSON.stringify($scope.facet));
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.items = responseData.data.content;
                    $scope.paging.total = responseData.data.totalPages;
                    $scope.selected = [];
                });
            }

            $scope.showNewLoanInfo = function (ev) {
                $mdDialog.show({
                    controller: 'newLoanInfoController',
                    templateUrl: 'views/new.loanInfo.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        loanInfo: {}
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllLoanInfo();
                    }
                }, function () {
                });
            };

            $scope.showEditLoanInfo = function (ev) {

                if ($scope.selected.length != 1) {
                    var editMessage = '';
                    if ($scope.selected.length == 0) {
                        editMessage = '请选择一项修改!';
                    }
                    if ($scope.selected.length > 1) {
                        editMessage = '只能选择一项!';
                    }

                    $mdToast.show(
                        $mdToast.simple()
                            .textContent(editMessage)
                            .position('top right')
                            .hideDelay(2000)
                    );
                    return;
                }

                $mdDialog.show({
                    controller: 'newLoanInfoController',
                    templateUrl: 'views/new.loanInfo.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        loanInfo: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllLoanInfo();
                    }
                }, function () {
                });
            };

            $scope.removeLoanInfos = function (ev) {
                if ($scope.selected.length == 0) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('请先选择要删除的数据!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                } else {

                    var names = '';
                    angular.forEach($scope.selected, function (loanInfo) {
                        names += loanInfo.name + ', ';
                    });

                    var confirm = $mdDialog.confirm()
                        .title('确定要删除已选择的数据吗?')
                        .textContent(names.substr(0, names.length - 2))
                        .ariaLabel('remove peron')
                        .targetEvent(ev)
                        .ok('删除')
                        .cancel('取消');

                    $mdDialog.show(confirm).then(function () {
                        removeLoanInfo();
                    }, function () {
                    });
                }
            };

            function removeLoanInfo() {
                $http.post('/loanInfo/delete', $scope.selected).then(function () {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('删除成功!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                    findAllLoanInfo();
                });
            }

            $scope.toggle = function (item, list) {
                var idx = list.indexOf(item);
                if (idx > -1) {
                    list.splice(idx, 1);
                }
                else {
                    list.push(item);
                }
            };

            $scope.exists = function (item, list) {
                return list.indexOf(item) > -1;
            };

            $scope.isIndeterminate = function () {
                return ($scope.selected.length !== 0 &&
                    $scope.selected.length !== $scope.items.length);
            };

            $scope.isChecked = function () {
                return $scope.selected.length === $scope.items.length;
            };

            $scope.toggleAll = function () {
                if ($scope.selected.length === $scope.items.length) {
                    $scope.selected = [];
                } else if ($scope.selected.length === 0 || $scope.selected.length > 0) {
                    $scope.selected = $scope.items.slice(0);
                }
            };

            $scope.searchLoanInfos = findAllLoanInfo;
            findAllLoanInfo();

        }])
        .controller('newLoanInfoController', ['$scope', '$mdDialog', '$http', 'loanInfo', function ($scope, $mdDialog, $http, loanInfo) {

            $scope.loanInfo = loanInfo ? loanInfo : {};
            $scope.loanInfo.phoneInfos = loanInfo.phoneInfos ? loanInfo.phoneInfos : [{
                phoneNum: '',
                description: ''
            }];

            $scope.addNewPhoneInfo = function () {
                $scope.loanInfo.phoneInfos.push({
                    phoneNum: '',
                    description: ''
                });
            };

            $scope.removePhoneInfo = function (phoneInfo) {
                if ($scope.loanInfo.phoneInfos.length > 1) {
                    var index = $scope.loanInfo.phoneInfos.indexOf(phoneInfo);
                    $scope.loanInfo.phoneInfos.splice(index, 1);
                }
            };

            $scope.saveLoanInfo = function () {
                var req = {
                    method: 'POST',
                    url: '/loanInfo/save',
                    data: $scope.loanInfo
                };
                $http(req).then(function () {
                    $mdDialog.hide('success');
                });

            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }]);
})();
