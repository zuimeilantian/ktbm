/*global define, require */

define(['angular',
        'ionic',
        'uiRouter',
        'ionicAngular',
        'services',
        'controllers',
        'directives',
        'ionicCitypicker'
        ],

    function (angular,ionic ,uiRouter) {
        'use strict';
        var app = angular.module('app', [
            'ngResource',
            'ionic',
            'app.controllers',
            'app.directives',
            'app.services',
            'ui.router',
            'ionic-citypicker'
        ]);
    return app;
});