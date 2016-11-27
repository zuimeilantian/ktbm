/*global define, require, console, cordova, navigator */

define(['ionic', 'angular', 'routes', 'app','ionicCitypicker'], function (ionic, angular,routes, app) {
    'use strict';
    var $html;
        /*onDeviceReady = function () {
            angular.bootstrap(document, [app['name']]);
        };*/
   
    /*if (document.URL.indexOf('http://') === -1 && document.URL.indexOf('https://') === -1) {
        document.addEventListener("deviceready", onDeviceReady, false);
    }*/
    $html = angular.element(document.getElementsByTagName('html')[0]);
    angular.element().ready(function () {
        try {
            angular.bootstrap(document, [app['name']]);
        } catch (e) {
            console.error(e.stack || e.message || e);
        }
    });
});
