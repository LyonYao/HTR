/**
 * Created by Xinlin on 2017/12/8.
 */
(function () {
    'use strict';

    user.controller('userController', ['$scope','$window', '$compile', function ($scope, $window, $compile) {

        $scope.firstName= "John";
        $scope.lastName= "Doe";
        $scope.printOpen = function(){
            $scope.oPop = $window.open('', '_blank', 'width='+ (window.screen.availWidth)+',height='+(window.screen.availHeight)+ ',top=0,left=0');
            var str = '<!DOCTYPE html>';
            str +='<html>';
            str +='<head>';
            str +='<meta charset="utf-8">';
            str +='<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">';
            str+='<style>';
            str+='#oDiv2 div{width: 100px;height: 100px;border:1px solid #c50000;}';
            str+='</style>';
            str +='</head>';
            str +='<body onload="window.print()">';
            str +="<div id='oDiv2'><div>{{firstName}}</div></div>";
            str +='</body>';
            str +='</html>';
            $scope.oPop.document.write($compile(str));
            $scope.oPop.document.close();
        };
        $scope.test = function(){
            console.log('ffffff')
        }

    }]);
})();
