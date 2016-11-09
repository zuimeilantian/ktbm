define(['app', 'indexCtrl', 'registerCtrl', 'orderCtrl', 'services'], function(app){
    'use strict';
    var controllers = angular.module('app.controllers', ['app.services', 'app.config']);
        controllers.controller('indexCtrl', require('indexCtrl'));
        controllers.controller('registerCtrl', require('registerCtrl'));
        controllers.controller('orderCtrl', require('orderCtrl'));

    return controllers;

});
