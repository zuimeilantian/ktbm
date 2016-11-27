define(function(){
    'use strict'
    var service = function($http){
        return {
            getUserInfo:function(callback){
                $http.get('/cus/getCusInfo').success(function(data){
                    return callback(data);
                });
            },
            updateInfo: function(params, callback){
                $http.post('/cus/updateCusInfo', params).success(function(data){
                    return callback(data);
                });
            },
            addAddress: function (params, callback) {
                $http.post('/address/addAddress', params).success(function(data){
                    return callback(data);
                });
            },
            getAddressList: function (callback) {
                $http.post('/address/getAddress').success(function(data){
                    return callback(data);
                });
            },
            setAddressDefault: function (params, callback) {
                $http.post('/address/setDefaultAddress', params).success(function(data){
                    return callback(data);
                });
            },
            deleteAddress: function (params, callback) {
                $http.post('/address/delAddress', params).success(function(data){
                    return callback(data);
                });
            },
            getAddressList2:function(level,parent_id,callback){
                $http.get('/address/getAddressList?level='+level+'&parent_id='+parent_id).success(function (data) {
                    return callback(data);
                })
            }, 
            addAddress2: function (params, callback) {
                $http.post('/address/addAddress2', params).success(function(data){
                    return callback(data);
                });
            },
            uploadHeadIcon: function (id, callback) {
                $http.get('/cus/saveOrUpdateIcon/'+id).success(function(data){
                    return callback(data);
                });
            },
            getAllBranches: function (callback) {
                $http.get('/dept/getDept').success(function(data){
                    return callback(data);
                });
            }
        }
    };
    service.$inject = ['$http'];
    return service;
});