define(function () {
    'use strict';
    function ctrl($scope, $orderServices, $ionicModal, $mainServices, $presonalServices, $state, $http, $ionicPopover, $ionicNavBarDelegate,$ionicLoading) {
        $ionicNavBarDelegate.setTitle('订单预约');
        function init(){
            $presonalServices.getUserInfo(function(data){
                $scope.formData = {
                    name: data.name,
                    linkName: data.name,
                    linkPhone: data.phone,
                    defaultGroupName:data.defaultGroupName,
                    groupId: data.defaultGroup
                };
                $scope.adrOpts = data.address;
                console.log(data)
                angular.forEach(data.address, function(item){
                    if(item.isDefault){
                        $scope.formData.addressId = item.id;
                    }
                });
            });
        };
        init();
        var saModal, pop;
        $scope.formData = {};
        $scope.confim = function(formData){
            if(!formData || !formData.linkName || !formData.linkPhone ||
                !formData.brand || !formData.machineType){
                $mainServices.alertTip('warning', '★ 为必填');
                return;
            }
            $ionicModal.fromTemplateUrl('templates/serve-info.html', {
                scope: $scope,
                animation: 'slide-in-right'
            }).then(function(data) {
                saModal = data;
                saModal.show();
            });

        };

        $scope.openPop = function ($event, key) {
            $scope.dataField = '';
            $http.post('/ktbm/ktbmOrderController/getBoundType',{key: key}).success(function (data) {
                $scope.list = data;
                $ionicPopover.fromTemplateUrl("templates/model.html", {
                    scope: $scope
                }).then(function(popover){
                    pop = popover;
                    pop.show($event);
                });
                $scope.doSelect = function (item) {
                    if(key == 'ktbm_ktpp'){
                        $scope.formData.brand = item.name;
                    }else if(key == 'ktbm_ktjx'){
                        $scope.formData.machineType = item.name;
                    }
                    pop.remove();
                };
                $scope.doInput = function (data) {
                    if(key == 'ktbm_ktpp'){
                        $scope.formData.brand = data;
                    }else if(key == 'ktbm_ktjx'){
                        $scope.formData.machineType = data;
                    }
                    pop.remove();
                }
            });

        };

        var modal;
        $scope.addressAdd = function () {
            $ionicModal.fromTemplateUrl('templates/add_address.tpl.html', {
                scope: $scope,
                animation: 'slide-in-right'
            }).then(function(data) {
                modal = data;
                modal.show();
            });
            $scope.closeAddr = function () {
                modal.hide();
            };
            
            function initAddrs() {
                $presonalServices.getAddressList(function (data) {
                    $scope.addrList = data.list;
                })
            }

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
                        modal.remove();
                        $presonalServices.getUserInfo(function(data){
                        	$scope.adrOpts = data.address;
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
            
        };
        $scope.submit = function(formData){
            formData.addressId = formData.address.id;
            formData.province = formData.address.province;
            formData.city = formData.address.city;
            formData.district = formData.address.district;
            formData.detail = formData.address.detail;
            formData.provinceId = formData.address.provinceId;
            formData.cityId = formData.address.cityId;
            formData.districtId = formData.address.districtId;
            // delete formData.address;
            console.log(formData)
            if(formData.serveBeginTime && formData.serveEndTime){
                if(+new Date(formData.serveBeginTime) > +new Date(formData.serveEndTime)){
                    $mainServices.alertTip('warning', '开始时间不能大于结束时间');
                }
            }
            $orderServices.saveOrder(formData, function(data){
                if(data.success){
                    $mainServices.alertTip('info', data.msg);
                    saModal.remove();
                    $state.go('myOrder');
                }else{
                    $mainServices.alertTip('warning', data.msg);
                }
            })
        };
        $scope.closeModal = function(){
            saModal.hide();
        };

        $scope.openBranch = function ($event) {
            $ionicLoading.show({
                template: '请稍等...'
            });
            var popover;
            $ionicPopover.fromTemplateUrl("branchs.popover.html", {
                scope: $scope
            }).then(function(data){
                popover = data;
                $presonalServices.getAllBranches(function (data) {
                    $scope.branches = data;
                    $ionicLoading.hide();
                    popover.show($event);
                });
            });
            $scope.setBranch = function (item, field) {
                var obj = {};
                obj[field] = item.id;
                $presonalServices.updateInfo(obj, function (data) {
                    if(data.success){
                        $presonalServices.getUserInfo(function(data){
                            $scope.formData['defaultGroupName'] = data.defaultGroupName;
                            $scope.formData['groupId'] = data.defaultGroup;
                        });
                        popover.remove();
                        $mainServices.alertTip('success', '更新分公司成功')
                    }else{
                        $mainServices.alertTip('wanring', '更新分公司成功')
                    }
                });
            }
        };
    };
    ctrl.$inject = ['$scope', '$orderServices', '$ionicModal', '$mainServices', '$presonalServices', '$state', '$http', '$ionicPopover', '$ionicNavBarDelegate', '$ionicLoading'];
    return ctrl;

});