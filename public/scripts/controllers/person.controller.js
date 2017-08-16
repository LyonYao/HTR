/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    person.controller('personController', ['$scope','$mdDialog', function ($scope,$mdDialog) {


        $scope.showNewPerson = function(ev) {
            $mdDialog.show({
                controller: 'newPersonController',
                templateUrl: 'views/new.person.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:false
            })
                .then(function(answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function() {
                    $scope.status = 'You cancelled the dialog.';
                });
        };

    }])
        .controller('newPersonController',['$scope', '$mdDialog', '$http', function($scope, $mdDialog, $http) {

            $scope.person = {};
            $scope.person.phoneInfos = [{
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
                var index = $scope.person.phoneInfos.indexOf(phoneInfo);
                $scope.person.phoneInfos.splice(index, 1);
            };

            $scope.savePerson = function () {

                $http.post('/person/save', $scope.person).then(function(responseData){
                    console.log(responseData.data)
                });

                $mdDialog.cancel();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.answer = function(answer) {
                $mdDialog.hide(answer);
            };
        }]);
})();
