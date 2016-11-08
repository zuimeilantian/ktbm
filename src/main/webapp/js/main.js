/*global requirejs, document, cordova, window, navigator, console */

require.config({
    paths:{
        jquery: '../lib/jquery-2.1.1.min.js',
        angular:'../lib/ionic/js/angular/angular',
        angularAnimate:'../lib/ionic/js/angular/angular-animate',
        angularSanitize:'../lib/ionic/js/angular/angular-sanitize',
        uiRouter:'../lib/ionic/js/angular-ui/angular-ui-router',
        ngResource:'../lib/ionic/js/angular/angular-resource',

        ionic:'../lib/ionic/js/ionic',
        ionicAngular:'../lib/ionic/js/ionic-angular',

        ocLazyLoad: '../lib/ocLazyLoad',
        app:'app',
        config:'config',

        services:'services/services',
        registerServices: 'services/registerServices',

        indexCtrl:'controllers/indexCtrl',
        registerCtrl:'controllers/registerCtrl',
        controllers:'controllers/controllers',
        directives:'directives/directives',
    },
    waitSeconds: 40,
    shim:{
        angular : {exports : 'angular'},
        app : {exports : 'app'},
        angularAnimate : {deps: ['angular']},
        angularSanitize : {deps: ['angular']},
        uiRouter : {deps: ['angular']},
        ngResource: {deps: ['angular']},
        ocLazyLoad: {exports: 'ocLazyLoad'},
        ionic :  {deps: ['angular'], exports : 'ionic'},
         ionicAngular: {deps: ['angular', 'ionic','uiRouter',
                              'angularAnimate', 'angularSanitize',
                              'ngResource'],exports: 'ionicAngular'}                      
    },
    priority:['angular','ionic'],
    deps:['bootstrap'],
    urlArgs: "bust=" +  (new Date()).getTime()
});
