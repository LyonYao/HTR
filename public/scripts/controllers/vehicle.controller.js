/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    vehicle.controller('vehicleController', ['$scope','$http', '$mdDialog',
        function ($scope, $http, $mdDialog) {

        $scope.vehicles = [];


        function findAllVehicles() {
            $http.get('/vehicle').then(function(responseData){
                $scope.vehicles = responseData.data;
            });
        }

        findAllVehicles();


        $scope.showAdvanced = function(ev) {
            $mdDialog.show({
                controller: dialogController,
                templateUrl: 'views/new.vehicle.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:true,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            })
                .then(function(answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function() {
                    $scope.status = 'You cancelled the dialog.';
                });
        };

        var dialogController = ['$scope', '$mdDialog',function($scope, $mdDialog) {
            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.answer = function(answer) {
                $mdDialog.hide(answer);
            };
        }];


    }]);
})();
