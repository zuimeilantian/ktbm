/**
 * Created by wz on 2016/11/10.
 */
define(function () {
    'use strict';
    function ctrl($scope, $bonusService, $ionicModal, $mainServices, $presonalServices, $state, $ionicNavBarDelegate) {
       $ionicNavBarDelegate.setTitle('积分兑换');
        var pager = {
            limit: 8,
            offset: 0
        },
            modal;
        function initData(pager){
            console.log(pager);
            $bonusService.getWareList(pager, function(data){
                console.log(data)
                $scope.list = data.list;
                $scope.size = data.count;
            });
            $presonalServices.getUserInfo(function (data) {
                $scope.point = data.points;
            })
        };
        initData(pager);
        $scope.getFlagInfinite = function () {
            return $scope.list&&$scope.list.length<$scope.size;
        };
        $scope.loadMore = function(){
            pager.offset = pager.offset + pager.limit;
            $bonusService.getWareList(pager, function(data){
                angular.forEach(data.list, function (item) {
                    $scope.list.push(item);
                });
                $scope.$broadcast('scroll.infiniteScrollComplete');
            });
        };
        $scope.getExchangeRecord = function () {
            $bonusService.getExchangeRecord({offset:0, limit: 999}, function (data) {
                $scope.exchanegs = data.list;
                $ionicModal.fromTemplateUrl('templates/exchange-record.html', {
                    scope: $scope,
                    animation: 'slide-in-left'
                }).then(function(data) {
                    modal = data;
                    modal.show();
                });
            });
        };

        $scope.close = function () {
            modal.hide();
        }
        
        $scope.fillInfo = function(list){
            var count = 0, goods = [], good = {};
            angular.forEach(list, function (item) {
                if(item.flag){
                    count++;
                    good.id = item.id;
                    good.count = 1;
                    goods.push(good);
                }
            });
            if(!count){
                $mainServices.alertTip('warning', '至少选择一个商品');
                return;
            }else{
                $presonalServices.getUserInfo(function (data) {
                    console.log(data)
                    $scope.address = data.address;
                    $ionicModal.fromTemplateUrl('templates/shipping-info.html', {
                        scope: $scope,
                        animation: 'slide-in-left'
                    }).then(function(data) {
                        modal = data;
                        modal.show();
                    });
                });

                $scope.closeShop = function () {
                    modal.hide();
                };

                $scope.goExchange = function (formData) {
                    var poster = {};
                    poster.goods = goods;
                    poster.phone = formData.phone
                    poster.address = formData.address
                    $bonusService.exchange(poster, function(data){
                       if(data.success){
                           initData(pager = {limit: 8,offset: 0});
                           modal.remove();
                           $mainServices.alertTip('success', data.msg);
                           //$state.go('myBonus');
                       }else{
                           $mainServices.alertTip('warning', data.msg);
                       }
                    })
                }
            }
            $scope.addAddress = function () {
                var addModal;
                $ionicModal.fromTemplateUrl('templates/add_address.tpl.html', {
                    scope: $scope,
                    animation: 'slide-in-right'
                }).then(function(data) {
                    addModal = data;
                    addModal.show();
                });
                $scope.closeAddr = function () {
                    addModal.hide();
                };
                $scope.addAddress2 = function (formData) {
                    if(formData.province==null||formData.city==null||formData.district==null||formData.detail==null||formData.detail.trim()==""){
                        $mainServices.alertTip('warning', '请填写完整信息');
                        return ;
                    }
                    formData['isDefault'] = 0,
                        formData['statue'] = 0,
                        $presonalServices.addAddress2(formData,function(data){
                            if(data){
                                $mainServices.alertTip('success', '添加成功');
                                addModal.remove();
                                $presonalServices.getUserInfo(function(data){
                                    $scope.address = data.address;
                                });
                            }else{
                                $mainServices.alertTip('warning', '添加失败');
                            }
                        })
                };

                $scope.getAddressList = function(level,parent_id){
                    $presonalServices.getAddressList2(level,parent_id,function(data){
                        if(level=='1'){
                            $scope.provincelist = data;
                        }else if(level=='2'){
                            $scope.citylist = data;
                        }else if(level=='3'){
                            $scope.districtlist = data;
                        }
                    })
                };
                $scope.getAddressList(1,0);
            }

        }

    }
    ctrl.$inject = ['$scope', '$bonusService', '$ionicModal', '$mainServices', '$presonalServices', '$state', '$ionicNavBarDelegate'];
    return ctrl;

});