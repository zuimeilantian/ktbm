/**
 * Created by wz on 2016/11/8.
 */
define(function(){
    'use strict'
    var service = function($http){
        return {
            getOrderList:function(params, callback){
                $http.get('/ktbm/ktbmOrderController/getKtbmOrderPageList', params).success(function(data){
                    return callback(data);
                });
            },
            saveOrder:function(params, callback){
                $http.get('/ktbm/ktbmOrderController/saveKtbmOrder', params).success(function(data){
                    return callback(data);
                });
            },
            getOneOrder:function(id, callback){
                $http.get('/ktbm/ktbmOrderController/getKtbmOrderMap?id='+id).success(function(data){
                    return callback(data);
                });
            }
        }
    };
    service.$inject = ['$http'];
    return service;
});