define(['app', 'orderCtrl', 'bonusCtrl', 'personalCtrl', 'wareCtrl', 'myOrderCtrl', 'registerCtrl', 'services'], function(app){
    'use strict';
    var controllers = angular.module('app.controllers', ['app.services', 'app.config']);
        controllers.controller('orderCtrl', require('orderCtrl'));
        controllers.controller('bonusCtrl', require('bonusCtrl'));
        controllers.controller('personalCtrl', require('personalCtrl'));
        controllers.controller('wareCtrl', require('wareCtrl'));
        controllers.controller('myOrderCtrl', require('myOrderCtrl'));

        controllers.controller('registerCtrl', require('registerCtrl'));

    return controllers;

});
