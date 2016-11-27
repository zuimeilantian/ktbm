/**
 * Created by wz on 2016/11/8.
 */
define(function(){
    'use strict'
    var service = function($http){
        return {
            getOrderList:function(params, callback){
                params.flag = 1;
                $http.post('/ktbm/ktbmOrderController/getKtbmOrderPageList', params).success(function(data){
                    return callback(data);
                });
            },
            saveOrder:function(params, callback){
                $http.post('/ktbm/ktbmOrderController/saveKtbmOrder', params).success(function(data){
                    return callback(data);
                });
            },
            getOneOrder:function(id, callback){
                $http.post('/ktbm/ktbmOrderController/getKtbmOrderMap?id='+id).success(function(data){
                    return callback(data);
                });
            },
            doRated: function (params, callback) {
                $http.post('/ktbm/ktbmOrderController/saveEvaluation', params).success(function(data){
                    return callback(data);
                });
            }
        }
    };
    service.$inject = ['$http'];
    return service;
});