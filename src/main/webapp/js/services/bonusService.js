/**
 * Created by wz on 2016/11/9.
 */
define(function(){
    'use strict'
    var service = function($http){
        return {
            getWareList: function (params, callback) {
                $http.post('/good/getGoods', params).success(function(data){
                    return callback(data);
                });
            },
            exchange: function (params, callback) {
                $http.post('/good/byGoods', params).success(function(data){
                    return callback(data);
                });
            },
            getRecord: function (params, callback) {
                $http.post('/ktbm/pointCrtl/getPointsPageList', params).success(function(data){
                    return callback(data);
                });
            },
            getExchangeRecord: function (params, callback) {
                $http.post('/good/getGoodsRecord', params).success(function(data){
                    return callback(data);
                });
            }
        }
    };
    service.$inject = ['$http'];
    return service;
});