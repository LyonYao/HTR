/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    role.controller('roleController', ['$scope', '$mdDialog', '$http', '$mdToast', '$location', 'auth',
        function ($scope, $mdDialog, $http, $mdToast, $location, auth) {

            $scope.currentButtons = {};
            auth.checkPermissions($location.path(), function (currentButtons) {
                $scope.currentButtons = currentButtons;
            });

            $scope.items = [];
            $scope.selected = [];

            function findAllRole() {
                var url = '/role/0';
                var req = {
                    method: 'GET',
                    url: url
                };

                $http(req).then(function (responseData) {
                    $scope.items = responseData.data;
                    $scope.selected = [];
                });
            }

            $scope.assignPermissions = function (ev) {
                if ($scope.selected.length != 1) {
                    var editMessage = '';
                    if ($scope.selected.length == 0) {
                        editMessage = '请选择一项记录!';
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
                    controller: 'rolePermissionsController',
                    templateUrl: 'views/permission.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        role: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllRole();
                    }
                }, function () {
                });
            };

            $scope.showNewRole = function (ev) {
                $mdDialog.show({
                    controller: 'newRoleController',
                    templateUrl: 'views/new.role.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        role: {}
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllRole();
                    }
                }, function () {
                });
            };

            $scope.showEditRole = function (ev) {

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
                    controller: 'newRoleController',
                    templateUrl: 'views/new.role.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                        role: $scope.selected[0]
                    }
                }).then(function (answer) {
                    if ('success' == answer) {
                        findAllRole();
                    }
                }, function () {
                });
            };

            $scope.removeRoles = function (ev) {
                if ($scope.selected.length == 0) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('请先选择要禁用的记录!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                } else {

                    var names = '';
                    angular.forEach($scope.selected, function (role) {
                        names += role.roleName + ', ';
                    });

                    var confirm = $mdDialog.confirm()
                        .title('确定要禁用已选择的记录吗?')
                        .textContent(names.substr(0, names.length - 2))
                        .ariaLabel('remove peron')
                        .targetEvent(ev)
                        .ok('禁用')
                        .cancel('取消');

                    $mdDialog.show(confirm).then(function () {
                        removeRole();
                    }, function () {
                    });
                }
            };

            function removeRole() {
                $http.post('/role/stop', $scope.selected).then(function () {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('禁用成功!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                    findAllRole();
                });
            }

            $scope.activeRoles = function (ev) {
                if ($scope.selected.length == 0) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('请先选择要启用的的记录!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                } else {

                    var names = '';
                    angular.forEach($scope.selected, function (role) {
                        names += role.roleName + ', ';
                    });

                    var confirm = $mdDialog.confirm()
                        .title('确定要启用已选择的记录吗?')
                        .textContent(names.substr(0, names.length - 2))
                        .ariaLabel('remove peron')
                        .targetEvent(ev)
                        .ok('启用')
                        .cancel('取消');

                    $mdDialog.show(confirm).then(function () {
                        activeRole();
                    }, function () {
                    });
                }
            };

            function activeRole() {
                $http.post('/role/active', $scope.selected).then(function () {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('启用成功!')
                            .position('top right')
                            .hideDelay(2000)
                    );
                    findAllRole();
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

            $scope.searchRoles = findAllRole;
            findAllRole();

        }])
        .controller('newRoleController', ['$scope', '$mdDialog', '$http', 'role', function ($scope, $mdDialog, $http, role) {

            $scope.role = role ? angular.copy(role) : {};

            $scope.saveRole = function () {
                var req = {
                    method: 'POST',
                    url: '/role/save',
                    data: $scope.role
                };
                $http(req).then(function () {
                    $mdDialog.hide('success');
                });

            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }]);

    role.controller('rolePermissionsController', ['$scope', '$mdDialog', '$http', 'role', function ($scope, $mdDialog, $http, role) {

        $scope.role = role ? angular.copy(role) : {};

        $scope.resources = [];

        $scope.setSelected = function (item) {
            item.selected = !item.selected;
            if (item.selected) {
                selectedParentRes(item);
            } else {
                unSeclectedChildrenRes(item);
            }
        };

        function selectedParentRes(item) {
            angular.forEach($scope.resources, function (resource) {
                angular.forEach(resource.childrenRes, function (menu) {
                    if(menu.uuid === item.uuid){
                        resource.selected = true;
                    }
                    angular.forEach(menu.childrenRes, function (button) {
                        if(button.uuid === item.uuid){
                            resource.selected = true;
                            menu.selected = true;
                        }
                    });
                });
            });
        }

        function unSeclectedChildrenRes(resource) {
            if (resource.childrenRes !== null && resource.childrenRes !== undefined && resource.childrenRes.length > 0) {
                for (var i in resource.childrenRes) {
                    resource.childrenRes[i].selected = false;
                    unSeclectedChildrenRes(resource.childrenRes[i]);
                }
            }
        }

        function findAllResources() {
            var req = {
                method: 'GET',
                url: '/resource',
                data: $scope.role
            };
            $http(req).then(function (responseDate) {
                $scope.resources = responseDate.data;
                angular.forEach(role.resources, function (resource) {
                    checkResourceSelected($scope.resources, resource);
                });
            });
        }

        function checkResourceSelected(resources, selectedResource) {
            for (var i in resources) {
                if (selectedResource.uuid === resources[i].uuid) {
                    resources[i].selected = true;
                    break;
                } else {
                    if (resources[i].childrenRes !== null) {
                        checkResourceSelected(resources[i].childrenRes, selectedResource);
                    }
                }
            }
        }

        findAllResources();

        $scope.saveRole = function () {
            var resourceList = [];
            getCheckedResources(resourceList, $scope.resources);
            $scope.role.resources = resourceList;
            var req = {
                method: 'POST',
                url: '/role/save',
                data: $scope.role
            };
            $http(req).then(function () {
                $mdDialog.hide('success');
            });

        };


        function getCheckedResources(roleList, resources) {
            for (var i in resources) {
                if (resources[i].selected) {
                    roleList.push(resources[i]);
                    if (resources[i].childrenRes !== null) {
                        getCheckedResources(roleList, resources[i].childrenRes);
                    }
                }
            }
        }

        $scope.cancel = function () {
            $mdDialog.cancel();
        };
    }]);
})();
