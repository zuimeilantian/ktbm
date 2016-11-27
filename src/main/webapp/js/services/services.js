define(['app','config','mainServices','orderServices', 'bonusService', 'presonalServices', 'chatService'],
    function (app) {
        'use strict';
        var config = require('config'),
            services = angular.module('app.services', ['app.config', 'ngResource']);

        services.service('$mainServices', require('mainServices'));
        services.factory('$orderServices', require('orderServices'));
        services.factory('$bonusService', require('bonusService'));
        services.factory('$presonalServices', require('presonalServices'));
        services.service('$chat', require('chatService'));
    return services;
});