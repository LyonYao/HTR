/**
 * Created by Xinlin on 2017/19/8.
 */
(function () {
    'use strict';

    user.filter('roles', function () {
            return function (roles) {
                return roles.map(function (role) {
                    return role.roleName;
                }).join(',');
            };
        });
})();