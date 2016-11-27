define(['app', 'ionic'], function (app) {
    'use strict';
    return angular.module('app.config', ['ionic'])
        .run(function ($chat, $presonalServices) {
            $presonalServices.getUserInfo(function (data) {
                // console.log(data)
                $chat.open({username:data.imname,pwd:data.imname});
            })

        })
        .config(function($ionicConfigProvider) {
        $ionicConfigProvider.platform.android.tabs.style('standard');
        
        $ionicConfigProvider.platform.android.tabs.position('standard');
        $ionicConfigProvider.platform.ios.navBar.alignTitle('center');
        $ionicConfigProvider.platform.android.navBar.alignTitle('center');
        
        $ionicConfigProvider.platform.android.views.transition('android');
        $ionicConfigProvider.platform.ios.views.transition('ios');
    });

});