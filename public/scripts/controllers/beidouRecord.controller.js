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
            $scope.completeds = [{completed: '', name: '全部'}, {completed: 0, name: '进行中'}, {completed: 1, name: '已完结'}];

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

            $scope.showRepayment = function (ev) {

                if ($scope.selected.length != 1) {
                    var editMessage = '';
                    if ($scope.selected.length == 0) {
                        editMessage = '请选择一项档案还款!';
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
                    templateUrl: 'views/repayment.html',
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
                        names += beidouRecord.beidouRecordNum + ', ';
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
                GTE_loanDate: false,
                LTE_loanDate: false
            };
        }]);

    beidouRecord.controller('newBeidouRecordController', ['$scope', '$mdDialog', '$http', '$timeout', '$q', 'beidouRecord',
        function ($scope, $mdDialog, $http, $timeout, $q, beidouRecord) {

            $scope.beidouRecord = beidouRecord ? angular.copy(beidouRecord) : {};

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

            $scope.persons = [];
            $scope.searchPerson = null;
            $scope.querySearchPerson = querySearchPerson;

            function findAllPerson() {
                var url = '/person/1/' + 10000000;
                url += '?jsonFilter=' + encodeURIComponent(JSON.stringify({'search_EQ_surety': '1'}));
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.persons = responseData.data.content;
                });
            }

            findAllPerson();

            function querySearchPerson(query) {
                var results = query ? $scope.persons.filter(createFilterForPerson(query)) : $scope.persons;
                var deferred = $q.defer();
                $timeout(function () {
                    deferred.resolve(results);
                }, Math.random() * 1000, false);
                return deferred.promise;
            }

            function createFilterForPerson(query) {
                return function filterFn(person) {
                    return (person.name.indexOf(query) >= 0);
                };
            }

            $scope.addNewPerson = function (ev) {
                $mdDialog.show({
                    multiple: true,
                    controller: 'newPersonController',
                    templateUrl: 'views/new.person.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        person: {}
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllPerson();
                    }
                }, function () {
                });
            };

            $scope.vehicles = [];
            $scope.searchVehicle = null;
            $scope.querySearchVehicle = querySearchVehicle;

            function findAllVehicle() {
                var url = '/vehicle/1/' + 10000000;
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.vehicles = responseData.data.content;
                });
            }

            findAllVehicle();

            function querySearchVehicle(query) {
                var results = query ? $scope.vehicles.filter(createFilterForVehicle(query)) : $scope.vehicles;
                var deferred = $q.defer();
                $timeout(function () {
                    deferred.resolve(results);
                }, Math.random() * 1000, false);
                return deferred.promise;
            }

            function createFilterForVehicle(query) {
                return function filterFn(vehicle) {
                    return (vehicle.licensePlate.indexOf(query) >= 0);
                };
            }

            $scope.addNewVehicle = function (ev) {
                $mdDialog.show({
                    multiple: true,
                    controller: 'newVehicleController',
                    templateUrl: 'views/new.vehicle.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        vehicle: {}
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllVehicle();
                    }
                }, function () {
                });
            };

            $scope.beidouRecord.loanRecords = [];
            $scope.$watchGroup(['beidouRecord.loansNum', 'beidouRecord.loanDate'], function (newValues) {
                if (newValues[0] > 0) {
                    if (newValues[0] > 36) {
                        alert('还款期数不能大于36个月!!');
                        $scope.beidouRecord.loanRecords = [];
                    } else {
                        $scope.beidouRecord.loanRecords = [];
                        $scope.datePopup.expectDate = [];
                        for (var i = 0; i < $scope.beidouRecord.loansNum; i++) {
                            var endDateMoment = moment(newValues[1]).add(i + 1, 'months');
                            $scope.beidouRecord.loanRecords.push({
                                loanNum: i + 1,
                                expectDate: endDateMoment.toDate(),
                                expectMoney: ''
                            });
                            $scope.datePopup.expectDate.push(false);
                        }
                    }
                }
            });

            $scope.bankCards = [];
            function findAllBankCards() {
                var url = '/bankCard/1';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.bankCards = responseData.data;
                });
            }

            findAllBankCards();

            $scope.openDatePopup = function (popup, index) {
                if (index != undefined) {
                    $scope.datePopup[popup][index] = true;
                } else {
                    $scope.datePopup[popup] = true;
                }
            };

            $scope.datePopup = {
                loanDate: false,
                expectDate: []
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

    beidouRecord.controller('repaymentController', ['$scope', '$mdDialog', '$http', '$timeout', '$q', 'beidouRecord',
        function ($scope, $mdDialog, $http, $timeout, $q, beidouRecord) {

            $scope.beidouRecord = beidouRecord ? angular.copy(beidouRecord) : {};

            $scope.subLoanRecord = {
                receiptDate: new Date()
            };

            $scope.saveBeidouRecord = function () {
                $scope.subLoanRecord.beidouRecord = $scope.beidouRecord;
                $scope.subLoanRecord.loanRecord = $scope.beidouRecord.nextRepay;
                var req = {
                    method: 'POST',
                    url: '/subLoanRecord/repayment',
                    data: $scope.subLoanRecord
                };
                $http(req).then(function () {
                    $mdDialog.hide('success');
                });
            };

            $scope.bankCards = [];
            function findAllBankCards() {
                var url = '/bankCard/1';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.bankCards = responseData.data;
                });
            }

            findAllBankCards();

            $scope.openDatePopup = function (popup) {
                $scope.datePopup[popup] = true;
            };

            $scope.datePopup = {
                receiptDate: false
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }]);
})();
