/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    beidouRenewal.controller('beidouRenewalController', ['$scope', '$mdDialog', '$http', '$location', 'auth',
        function ($scope, $mdDialog, $http, $location, auth) {

            $scope.currentButtons = {};
            auth.checkPermissions($location.path(), function (currentButtons) {
                $scope.currentButtons = currentButtons;
            });

            $scope.items = [];

            $scope.facet = {};

            $scope.paging = {
                total: 0,
                current: 1,
                onPageChanged: findAllBeidouRenewal
            };

            function findAllBeidouRenewal() {
                var url = '/beidouRenewal/' + $scope.paging.current + '/' + PAGESIZE;
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

            $scope.searchBeidouRenewals = findAllBeidouRenewal;

            $scope.openDatePopup = function(popup) {
                $scope.datePopup[popup] = true;
            };

            $scope.datePopup = {
                GTE_receiptDate: false,
                LTE_receiptDate: false
            };
        }]);
})();
