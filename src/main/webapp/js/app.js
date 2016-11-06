/*global define, require */

define(['angular',
        'ionic',
        'uiRouter',
        'ionicAngular',
        'services',
        'controllers',
        'directives'
        ],

    function (angular,ionic ,uiRouter) {
        'use strict';
        var app = angular.module('app', [
            'ngResource',
            'ionic',
            'app.controllers',
            'app.directives',
            'app.services',
            'ui.router'
        ]).run(function ($ionicPlatform, $http, $rootScope) {
            $rootScope.platform = ionic.Platform.platform();
            $http.defaults.headers.common.token = 'admin';
            $rootScope.$on('$ionicView.beforeEnter', function() {
                $(".loading").show();
            });
            $rootScope.$on('$ionicView.afterEnter', function() {
                $(".loading").hide();
            }, false);
        })
    return app;
});