require.config({
    paths:{
        jquery: '../jquery-2.1.1.min',
        angular:'../lib/ionic/js/angular/angular.min',
        angularAnimate:'../lib/ionic/js/angular/angular-animate.min',
        angularSanitize:'../lib/ionic/js/angular/angular-sanitize.min',
        uiRouter:'../lib/ionic/js/angular-ui/angular-ui-router.min',
        ngResource:'../lib/ionic/js/angular/angular-resource.min',

        ionic:'../lib/ionic/js/ionic.min',
        ionicAngular:'../lib/ionic/js/ionic-angular.min',
        ionicCitypicker: '../lib/ionic-citypicker.min',
        app:'app',
        config:'config',

        services:'services/services',
        mainServices: 'services/mainServices',
        orderServices:'services/orderServices',
        bonusService:'services/bonusService',
        presonalServices:'services/presonalServices',
        chatService:'services/chatService',
        
        personalCtrl:'controllers/personalCtrl',
        registerCtrl: 'controllers/registerCtrl',
        myOrderCtrl:'controllers/myOrderCtrl',
        bonusCtrl:'controllers/bonusCtrl',
        wareCtrl:'controllers/wareCtrl',
        orderCtrl:'controllers/orderCtrl',
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
        ionicCitypicker: {deps: ['angular', 'ionic'], exports: 'ionicCitypicker'},
        ionic :  {deps: ['angular'], exports : 'ionic'},
        ionicAngular: {deps: ['angular', 'ionic','uiRouter',
                              'angularAnimate', 'angularSanitize',
                              'ngResource'],exports: 'ionicAngular'}                      
    },
    priority:['angular','ionic'],
    deps:['bootstrap']
    // urlArgs: "bust=" +  (new Date()).getTime()
});
