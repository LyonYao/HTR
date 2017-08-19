/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    person.controller('personController', ['$scope','$mdDialog', '$http', '$mdToast',
        function ($scope, $mdDialog, $http, $mdToast) {

        $scope.items = [];
        $scope.selected = [];

        $scope.showNewPerson = function(ev) {
            $mdDialog.show({
                controller: 'newPersonController',
                templateUrl: 'views/new.person.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:false,
                locals: {
                    person: []
                }
            }).then(function(answer) {
                    if('success' == answer){
                        findAllPerson();
                    }
                }, function () {
                });
        };

        $scope.showEditPerson = function(ev) {

            if($scope.selected.length != 1){
                var editMessage = '';
                if($scope.selected.length == 0){
                    editMessage = '请选择一项修改!';
                }
                if($scope.selected.length > 1){
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
                controller: 'newPersonController',
                templateUrl: 'views/new.person.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:false,
                locals: {
                    person: $scope.selected[0]
                }
            }).then(function(answer) {
                    if('success' == answer){
                        findAllPerson();
                    }
                }, function () {
                });
        };

        $scope.removePersons = function(ev){
            if($scope.selected.length == 0){
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('请先选择要删除的数据!')
                        .position('top right')
                        .hideDelay(2000)
                );
            } else {

                var names= '';
                angular.forEach($scope.selected, function (person) {
                    names += person.name + ', ';
                });

                var confirm = $mdDialog.confirm()
                    .title('确定要删除已选择的数据吗?')
                    .textContent(names.substr(0, names.length - 2))
                    .ariaLabel('remove peron')
                    .targetEvent(ev)
                    .ok('删除')
                    .cancel('取消');

                $mdDialog.show(confirm).then(function() {
                    removePerson();
                    findAllPerson();
                }, function() {
                });
            }
        };


        function findAllPerson(){
            $http.get('/person').then(function(responseData){
                $scope.items = responseData.data;
            });
        }
        
        function removePerson(){
            // $http.delete('/person/delete', {data:$scope.selected}).then(function () {
            //     $mdToast.show(
            //         $mdToast.simple()
            //             .textContent('删除成功!')
            //             .position('top right')
            //             .hideDelay(2000)
            //     );
            // });
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

        $scope.isIndeterminate = function() {
            return ($scope.selected.length !== 0 &&
                $scope.selected.length !== $scope.items.length);
        };

        $scope.isChecked = function() {
            return $scope.selected.length === $scope.items.length;
        };

        $scope.toggleAll = function() {
            if ($scope.selected.length === $scope.items.length) {
                $scope.selected = [];
            } else if ($scope.selected.length === 0 || $scope.selected.length > 0) {
                $scope.selected = $scope.items.slice(0);
            }
        };
        
        findAllPerson();

    }])
        .controller('newPersonController',['$scope', '$mdDialog', '$http', 'person', function($scope, $mdDialog, $http, person) {

            $scope.person = person ? person : {};
            $scope.person.phoneInfos = person.phoneInfos ? person.phoneInfos : [{
                phoneNum:'',
                description:''
            }];

            $scope.addNewPhoneInfo = function () {
                $scope.person.phoneInfos.push({
                    phoneNum:'',
                    description:''
                });
            };

            $scope.removePhoneInfo = function (phoneInfo) {
                if($scope.person.phoneInfos.length > 1){
                    var index = $scope.person.phoneInfos.indexOf(phoneInfo);
                    $scope.person.phoneInfos.splice(index, 1);
                }
            };

            $scope.savePerson = function () {

                $http.post('/person/save', $scope.person).then(function(responseData){
                    $mdDialog.hide('success');
                });

            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }]);
})();