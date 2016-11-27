define(['app'], function(app) {
    'use strict';
    app.config(['$stateProvider', '$urlRouterProvider',
        function($stateProvider, $urlRouterProvider) {
            $stateProvider
                .state('register', {
                    url: '/register/:from',
                    templateUrl: 'templates/register.html',
                    controller: 'registerCtrl'
                })
                .state('orderForm', {
                    url: '/orderForm',
                    templateUrl: 'templates/order/order-form.tpl.html',
                    controller: 'orderCtrl'
                })
                .state('myBonus', {
                    url: '/myBonus',
                    templateUrl: 'templates/bonus/bonus.tpl.html',
                    controller: 'bonusCtrl'
                })
                .state('exchange', {
                    url: '/exchange',
                    templateUrl: 'templates/bonus/bonus-exchange.tpl.html',
                    controller: 'wareCtrl'
                })
                .state('myOrder', {
                    url: '/myOrder',
                    templateUrl: 'templates/order/my-order.tpl.html',
                    controller: 'myOrderCtrl'
                })
                .state('personal', {
                    url: '/personal',
                    templateUrl: 'templates/personal/personal.tpl.html',
                    controller: 'personalCtrl'
                })
                .state('record', {
                    url: '/record',
                    templateUrl: 'templates/bonus/bonus-record.tpl.html',
                    controller: 'bonusCtrl'
                })
            $urlRouterProvider.otherwise('/myBonus');

    }]);
});
