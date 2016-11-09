define(['app','config','registerServices','mainServices','orderServices'],
    function (app) {
        'use strict';
        var config = require('config'),
            services = angular.module('app.services', ['app.config', 'ngResource']);

        services.service('$registerServices', require('registerServices'));
        services.service('$mainServices', require('mainServices'));
        services.factory('$orderServices', require('orderServices'));

        // services.factory('$mapServices', require('mapServices'));
        // services.service('$indexServices', require('indexServices'));
        // services.service('$moreServices', require('moreServices'));
        // services.factory('$backButtonServices', require('backButtonServices'));

    return services;
});