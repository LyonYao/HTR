/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    beidouRecord.controller('beidouRecordController', ['$scope', '$mdDialog', '$http', '$mdToast','$location', 'auth',
        function ($scope, $mdDialog, $http, $mdToast, $location, auth) {

            $scope.currentButtons = {};
            auth.checkPermissions($location.path(), function (currentButtons) {
                $scope.currentButtons = currentButtons;
            });

            $scope.items = [];
            $scope.selected = [];

            $scope.facet = {};
            $scope.installTypes = ['新装', '并网', '挂平台'];

            $scope.paging = {
                total: 0,
                current: 1,
                onPageChanged: findAllBeidouRecord
            };

            function findAllBeidouRecord() {
                var url = '/beidouRecord/' + $scope.paging.current + '/' + PAGESIZE;
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

            $scope.showNewBeidouRecord = function (ev) {
                $mdDialog.show({
                    controller: 'newBeidouRecordController',
                    templateUrl: 'views/new.beidouRecord.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        beidouRecord: {}
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllBeidouRecord();
                    }
                }, function () {
                });
            };

            $scope.showEditBeidouRecord = function (ev) {

                if ($scope.selected.length != 1) {
                    var editMessage = '';
                    if ($scope.selected.length == 0) {
                        editMessage = '请选择一项档案修改!';
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
                    controller: 'newBeidouRecordController',
                    templateUrl: 'views/new.beidouRecord.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        beidouRecord: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllBeidouRecord();
                    }
                }, function () {
                });
            };

            $scope.showRenewal = function (ev) {

                if ($scope.selected.length != 1) {
                    var editMessage = '';
                    if ($scope.selected.length == 0) {
                        editMessage = '请选择一项档案续费!';
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
                    controller: 'renewalController',
                    templateUrl: 'views/renewal.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        beidouRecord: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllBeidouRecord();
                    }
                }, function () {
                });
            };

            $scope.showDetailLoanRecords = function (ev) {

                if ($scope.selected.length != 1) {
                    var editMessage = '';
                    if ($scope.selected.length == 0) {
                        editMessage = '请选择一项档案查看!';
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
                    controller: 'showLoanRecordDetailController',
                    templateUrl: 'views/detail.loanRecords.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        beidouRecord: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                    }
                }, function () {
                });
            };

            $scope.showDetailBeidouRecord = function (ev) {

                if ($scope.selected.length != 1) {
                    var editMessage = '';
                    if ($scope.selected.length == 0) {
                        editMessage = '请选择一项档案查看!';
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
                    controller: 'repaymentController',
                    templateUrl: 'views/detail.beidouRecord.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        beidouRecord: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                    }
                }, function () {
                });
            };

            $scope.removeBeidouRecords = function (ev) {
                if ($scope.selected.length == 0) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('请先选择要删除的数据!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                } else {

                    var names = '';
                    angular.forEach($scope.selected, function (beidouRecord) {
                        names += beidouRecord.licensePlate + ', ';
                    });

                    var confirm = $mdDialog.confirm()
                        .title('确定要删除已选择的数据吗?')
                        .textContent(names.substr(0, names.length - 2))
                        .ariaLabel('remove peron')
                        .targetEvent(ev)
                        .ok('删除')
                        .cancel('取消');

                    $mdDialog.show(confirm).then(function () {
                        removeBeidouRecord();
                    }, function () {
                    });
                }
            };

            function removeBeidouRecord() {
                $http.post('/beidouRecord/delete', $scope.selected).then(function () {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('删除成功!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                    findAllBeidouRecord();
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

            $scope.searchBeidouRecords = findAllBeidouRecord;

            $scope.openDatePopup = function (popup) {
                $scope.datePopup[popup] = true;
            };

            $scope.datePopup = {
                GTE_expireTime: false,
                LTE_expireTime: false
            };
        }]);

    beidouRecord.controller('newBeidouRecordController', ['$scope', '$mdDialog', '$http', '$timeout', '$q', 'beidouRecord',
        function ($scope, $mdDialog, $http, $timeout, $q, beidouRecord) {

            $scope.beidouRecord = beidouRecord ? angular.copy(beidouRecord) : {};

            if(angular.isUndefined($scope.beidouRecord.joinTime)){
                $scope.beidouRecord.joinTime = new Date();
                $scope.beidouRecord.expireTime = moment($scope.beidouRecord.joinTime).add(1, 'years').toDate();
            } else {
                $scope.beidouRecord.joinTime = moment($scope.beidouRecord.joinTime).toDate();
            }

            $scope.$watch('beidouRecord.joinTime', function (newValue) {
                $scope.beidouRecord.expireTime = moment(newValue).add(1, 'years').toDate();
            });

            $scope.installTypes = ['新装', '并网', '挂平台'];

            $scope.beidouBranchs = [];
            function findAllBeidouBranchs() {
                var url = '/beidouBranch/1';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.beidouBranchs = responseData.data;
                });
            }
            findAllBeidouBranchs();

            $scope.installers = [];
            function findAllInstallers() {
                var url = '/user/2';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.installers = responseData.data;
                });
            }
            findAllInstallers();

            $scope.saveBeidouRecord = function () {
                var req = {
                    method: 'POST',
                    url: '/beidouRecord/save',
                    data: $scope.beidouRecord
                };
                $http(req).then(function () {
                    $mdDialog.hide('success');
                });

            };

            $scope.openDatePopup = function (popup) {
                $scope.datePopup[popup] = true;
            };

            $scope.datePopup = {
                joinTime: false,
                expireTime: false
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }]);

    beidouRecord.controller('showLoanRecordDetailController', ['$scope', '$mdDialog', '$http', 'beidouRecord',
        function ($scope, $mdDialog, $http, beidouRecord) {

            $scope.beidouRecord = beidouRecord ? angular.copy(beidouRecord) : {};

            $scope.subLoanRecords = [];
            function fundAllSubLoanRecords() {
                var req = {
                    method: 'GET',
                    url: '/subLoanRecord/beidouRecordID/' + $scope.beidouRecord.uuid
                };
                $http(req).then(function (response) {
                    $scope.subLoanRecords = response.data;
                });
            }
            fundAllSubLoanRecords();

            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }]);

    beidouRecord.controller('renewalController', ['$scope', '$mdDialog', '$http', '$timeout', '$q', 'beidouRecord',
        function ($scope, $mdDialog, $http, $timeout, $q, beidouRecord) {

            $scope.beidouRecord = beidouRecord ? angular.copy(beidouRecord) : {};

            $scope.beidouRenewal = {
                renewalDate: new Date(),
                renewalFee:840,
                months:12
            };

            $scope.saveBeidouRenewal = function () {
                if($scope.cardType <= 0){
                    $scope.beidouRenewal.changeCard = true;
                    if($scope.cardType < 0){
                        $scope.beidouRecord.borrowCardFlow = true;
                    }
                } else {
                    $scope.beidouRenewal.changeCard = false;
                }
                $scope.beidouRenewal.beidouRecord = $scope.beidouRecord;
                var req = {
                    method: 'POST',
                    url: '/beidouRenewal/renewal',
                    data: $scope.beidouRenewal
                };
                $http(req).then(function () {
                    $mdDialog.hide('success');
                });
            };

            $scope.cardType = 2;
            $scope.cardTypes = [{
                name:"未换直接续",
                value:2
            },{
                name:"换新卡",
                value:0
            },{
                name:"借流量",
                value:-1
            }];

            $scope.beidouBranchs = [];
            function findAllBeidouBranchs() {
                var url = '/beidouBranch/1';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.beidouBranchs = responseData.data;
                });
            }
            findAllBeidouBranchs();

            $scope.installers = [];
            function findAllInstallers() {
                var url = '/user/2';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.installers = responseData.data;
                });
            }
            findAllInstallers();

            $scope.saveBeidouRecord = function () {
                var req = {
                    method: 'POST',
                    url: '/beidouRecord/save',
                    data: $scope.beidouRecord
                };
                $http(req).then(function () {
                    $mdDialog.hide('success');
                });

            };

            $scope.openDatePopup = function (popup) {
                $scope.datePopup[popup] = true;
            };

            $scope.datePopup = {
                renewalDate: false
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }]);
})();
