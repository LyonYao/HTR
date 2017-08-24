/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    bankCard.controller('bankCardController', ['$scope', '$mdDialog', '$http', '$mdToast',
        function ($scope, $mdDialog, $http, $mdToast) {

            $scope.items = [];
            $scope.selected = [];

            function findAllBankCard() {
                var url = '/bankCard/0';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.items = responseData.data;
                    $scope.selected = [];
                });
            }

            $scope.showNewBankCard = function (ev) {
                $mdDialog.show({
                    controller: 'newBankCardController',
                    templateUrl: 'views/new.bankCard.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        bankCard: {}
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllBankCard();
                    }
                }, function () {
                });
            };

            $scope.showEditBankCard = function (ev) {

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
                    controller: 'newBankCardController',
                    templateUrl: 'views/new.bankCard.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        bankCard: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllBankCard();
                    }
                }, function () {
                });
            };

            $scope.removeBankCards = function (ev) {
                if ($scope.selected.length == 0) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('请先选择要禁用的银行卡!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                } else {

                    var names = '';
                    angular.forEach($scope.selected, function (bankCard) {
                        names += bankCard.cardNumber + ', ';
                    });

                    var confirm = $mdDialog.confirm()
                        .title('确定要禁用已选择的银行卡吗?')
                        .textContent(names.substr(0, names.length - 2))
                        .ariaLabel('remove peron')
                        .targetEvent(ev)
                        .ok('禁用')
                        .cancel('取消');

                    $mdDialog.show(confirm).then(function () {
                        removeBankCard();
                    }, function () {
                    });
                }
            };

            function removeBankCard() {
                $http.post('/bankCard/stop', $scope.selected).then(function () {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('禁用成功!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                    findAllBankCard();
                });
            }

            $scope.activeBankCards = function (ev) {
                if ($scope.selected.length == 0) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('请先选择要启用的的银行卡!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                } else {

                    var names = '';
                    angular.forEach($scope.selected, function (bankCard) {
                        names += bankCard.cardNumber + ', ';
                    });

                    var confirm = $mdDialog.confirm()
                        .title('确定要启用已选择的银行卡吗?')
                        .textContent(names.substr(0, names.length - 2))
                        .ariaLabel('remove peron')
                        .targetEvent(ev)
                        .ok('启用')
                        .cancel('取消');

                    $mdDialog.show(confirm).then(function () {
                        activeBankCard();
                    }, function () {
                    });
                }
            };

            function activeBankCard() {
                $http.post('/bankCard/active', $scope.selected).then(function () {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('启用成功!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                    findAllBankCard();
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

            $scope.searchBankCards = findAllBankCard;
            findAllBankCard();

        }])
        .controller('newBankCardController', ['$scope', '$mdDialog', '$http', 'bankCard', function ($scope, $mdDialog, $http, bankCard) {

            $scope.bankCard = bankCard ? bankCard : {};

            $scope.saveBankCard = function () {
                var req = {
                    method: 'POST',
                    url: '/bankCard/save',
                    data: $scope.bankCard
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
