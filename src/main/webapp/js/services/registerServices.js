/**
 * Created by wz on 2016/11/8.
 */
define(function(){
    'use strict'
    var service = function($http){
        return {
            getCode:function(phone, callback){
                $http.get(phone + ' ').success(function(data){
                    return callback(data);
                });
            }
        }
    };
    service.$inject = ['$http'];
    return service;
});