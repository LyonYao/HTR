/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    beidouBranch.controller('beidouBranchController', ['$scope', '$mdDialog', '$http', '$mdToast', '$location', 'auth',
        function ($scope, $mdDialog, $http, $mdToast, $location, auth) {

            $scope.currentButtons = {};
            auth.checkPermissions($location.path(), function (currentButtons) {
                $scope.currentButtons = currentButtons;
            });

            $scope.items = [];
            $scope.selected = [];

            function findAllBeidouBranch() {
                var url = '/beidouBranch/0';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.items = responseData.data;
                    $scope.selected = [];
                });
            }

            $scope.showNewBeidouBranch = function (ev) {
                $mdDialog.show({
                    controller: 'newBeidouBranchController',
                    templateUrl: 'views/new.beidouBranch.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        beidouBranch: {}
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllBeidouBranch();
                    }
                }, function () {
                });
            };

            $scope.showEditBeidouBranch = function (ev) {

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
                    controller: 'newBeidouBranchController',
                    templateUrl: 'views/new.beidouBranch.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        beidouBranch: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllBeidouBranch();
                    }
                }, function () {
                });
            };

            $scope.removeBeidouBranchs = function (ev) {
                if ($scope.selected.length == 0) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('请先选择要禁用的分部!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                } else {

                    var names = '';
                    angular.forEach($scope.selected, function (beidouBranch) {
                        names += beidouBranch.branchName + ', ';
                    });

                    var confirm = $mdDialog.confirm()
                        .title('确定要禁用已选择的分部吗?')
                        .textContent(names.substr(0, names.length - 2))
                        .ariaLabel('remove peron')
                        .targetEvent(ev)
                        .ok('禁用')
                        .cancel('取消');

                    $mdDialog.show(confirm).then(function () {
                        removeBeidouBranch();
                    }, function () {
                    });
                }
            };

            function removeBeidouBranch() {
                $http.post('/beidouBranch/stop', $scope.selected).then(function () {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('禁用成功!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                    findAllBeidouBranch();
                });
            }

            $scope.activeBeidouBranchs = function (ev) {
                if ($scope.selected.length == 0) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('请先选择要启用的的分部!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                } else {

                    var names = '';
                    angular.forEach($scope.selected, function (beidouBranch) {
                        names += beidouBranch.branchName + ', ';
                    });

                    var confirm = $mdDialog.confirm()
                        .title('确定要启用已选择的分部吗?')
                        .textContent(names.substr(0, names.length - 2))
                        .ariaLabel('remove peron')
                        .targetEvent(ev)
                        .ok('启用')
                        .cancel('取消');

                    $mdDialog.show(confirm).then(function () {
                        activeBeidouBranch();
                    }, function () {
                    });
                }
            };

            function activeBeidouBranch() {
                $http.post('/beidouBranch/active', $scope.selected).then(function () {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('启用成功!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                    findAllBeidouBranch();
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

            $scope.searchBeidouBranchs = findAllBeidouBranch;
            findAllBeidouBranch();

        }])
        .controller('newBeidouBranchController', ['$scope', '$mdDialog', '$http', 'beidouBranch', function ($scope, $mdDialog, $http, beidouBranch) {

            $scope.beidouBranch = beidouBranch ? angular.copy(beidouBranch) : {};

            $scope.saveBeidouBranch = function () {
                var req = {
                    method: 'POST',
                    url: '/beidouBranch/save',
                    data: $scope.beidouBranch
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
