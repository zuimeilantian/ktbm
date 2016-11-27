define(function () {
    'use strict';
    function ctrl($scope, $presonalServices, $ionicNavBarDelegate, $ionicModal, $mainServices, $ionicPopup, $ionicLoading, $ionicPopover) {
        var modal, popup;
        $scope.person = {};
        $scope.formData = {};
        $ionicNavBarDelegate.setTitle('个人中心');
        function initInfo(){
            $presonalServices.getUserInfo(function(data){
                $scope.person = data;
                $scope.localCityInfo = '';
                angular.forEach(data.localCity, function (item) {
                    $scope.localCityInfo = $scope.localCityInfo + item;
                });
            });
        }
        initInfo();
        /*$scope.editLocalCity = function () {
            console.log($scope.person.localCity)
            var localCity = {};
            popup = $ionicPopup.show({
                templateUrl: 'edit_localcity.tpl.html',
                title:'修改城市',
                scope: $scope,
                buttons: [
                    { text: '取消' },
                    {
                        text: '<b>保存</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            var prov = $('.prov').val(),
                                city = $('.city').val(),
                                dist = $('.dist').val();
                            localCity = [prov, city, dist];
                            $presonalServices.updateInfo({localCity: localCity}, function (data) {
                                if(data.success){
                                    initInfo();
                                    $mainServices.alertTip('success', '更新城市成功')
                                }else{
                                    $mainServices.alertTip('wanring', '更新城市失败')
                                }
                            });
                        }
                    },
                ]
            });
            setTimeout(function () {
                $("#localcity").citySelect({
                    prov: $scope.person.localCity[0],
                    city: $scope.person.localCity[1] || "",
                    dist: $scope.person.localCity[2] || "",
                    nodata: "none"
                });
            }, 300)
        }*/

        $scope.showqrCode = function (item) {
            var qrModal, outputId;
            $scope.displayPerson = item;
            $ionicModal.fromTemplateUrl('qrcode.tpl.html', {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function(data) {
                qrModal = data;
                qrModal.show();
            });
            $scope.changeHead = function () {
                var head = document.getElementById("uploadHead");
                head.click();
                head.addEventListener("change", function (e) {
                    var src,
                        url = window.URL || window.webkitURL || window.mozURL,
                        files = e.target.files,
                        file = files[0];
                    if (url) {
                        src = url.createObjectURL(files[0]);
                    } else {
                        src = e.target.result;
                    }
                    $scope.$apply(function () {
                        $scope.displayPerson.iconUrl = src;
                    });
                    setTimeout(function () {
                        file.timestamp = +new Date();
                        uploadFile(file)
                    });
                })
            };
            function uploadFile(file) {
                var url = '/ktbm/filesController/upload';
                var xhr = new XMLHttpRequest();
                var fd = new FormData();
                xhr.open("POST", url, true);
                xhr.onreadystatechange = function () {
                    if (xhr.readyState == 4 && xhr.status == 200) {
                        var data = JSON.parse(xhr.responseText);
                        $presonalServices.uploadHeadIcon(data.id, function (data) {
                            if(data.success){
                                qrModal.hide();
                                $mainServices.alertTip('success', '更新头像成功')
                            }else{
                                $mainServices.alertTip('wanring', '更新头像失败')
                            }
                        })
                    }
                };
                fd.append("file", file);
                xhr.send(fd);
            };
            $scope.closeQRCode = function () {
                qrModal.hide();
            };
        };


        // 地址管理
        $scope.addressManage = function(){
            var addressModal;
            function initAddrs() {
                $presonalServices.getAddressList(function (data) {
                    console.log(data)
                    $scope.addrList = data.list;
                })
            }
            initAddrs();
            $ionicModal.fromTemplateUrl('address.tpl.html', {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function(data) {
                addressModal = data;
                addressModal.show();
            });
            $scope.goBack = function () {
                addressModal.hide();
            };

            $scope.doDelete = function (item) {
                item.showDelete = false;
                $presonalServices.deleteAddress({id: item.id}, function (data) {
                    if(data){
                        initAddrs();
                        $mainServices.alertTip('success', '删除成功');
                    }else{
                        $mainServices.alertTip('warning', '删除失败');
                    }
                })
            };
            $scope.editAddr = function (item) {
                var editModal;
                $ionicLoading.show({
                    template: '加载中...'
                });
                $presonalServices.getAddressList2(1,0, function (data) {
                    $scope.provincelist = data;
                        $presonalServices.getAddressList2(2,item.provinceId, function (data) {
                            $scope.citylist = data;
                            $presonalServices.getAddressList2(3,item.cityId, function (data) {
                                $scope.districtlist = data;
                                $ionicModal.fromTemplateUrl('templates/add_address.tpl.html', {
                                    scope: $scope,
                                    animation: 'slide-in-right'
                                }).then(function(data) {
                                    editModal = data;
                                    $scope.formData = {
                                        province: parseInt(item.provinceId),
                                        city: parseInt(item.cityId),
                                        district: parseInt(item.districtId),
                                        detail: item.detail,
                                        id: item.id
                                    };
                                    $ionicLoading.hide();
                                    editModal.show();
                                });
                            })
                        })
                });
                $scope.addAddress2 = function (formData) {
                    console.log(formData)
                    $scope.optionFlag = true;
                    $presonalServices.addAddress2(formData,function(data){
                        if(data){
                            initAddrs();
                            $mainServices.alertTip('success', '更新成功');
                            editModal.remove();
                            setTimeout(function(){
                                $scope.optionFlag = false;
                            },300);
                        }else{
                            $mainServices.alertTip('warning', '更新失败');
                        }
                    })
                };
                $scope.closeAddr = function () {
                    editModal.hide();
                }
            };

            $scope.setDefault = function (item) {
                $presonalServices.setAddressDefault({id: item.id}, function (data) {
                    if(data){
                        initAddrs();
                        $mainServices.alertTip('success', '设置成功');
                    }else{
                        $mainServices.alertTip('warning', '设置失败');
                    }
                })
            }

            $scope.addressAdd = function () {
                var addModal;
                $scope.formData = {};
                $ionicModal.fromTemplateUrl('templates/add_address.tpl.html', {
                    scope: $scope,
                    animation: 'slide-in-right'
                }).then(function(data) {
                    addModal = data;
                    addModal.show();
                });

                $scope.closeAddr = function () {
                    addModal.hide();
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
                            initAddrs();
                            $mainServices.alertTip('success', '添加成功');
                            addModal.remove();
                        }else{
                            $mainServices.alertTip('warning', '添加失败');
                        }
                	})
                }
            }

        };
////////////////////done
        $scope.updateField = function(title, field){
            $scope.data = {
                field: $scope.person[field]
            };
            var obj = {};
            var temp = '<input type="text" ng-model="data.field">';
            popup = $ionicPopup.show({
                template: temp,
                title: '修改' + title,
                scope: $scope,
                buttons: [
                    { text: '取消' },
                    {
                        text: '<b>保存</b>',
                        type: 'button-calm',
                        onTap: function(e) {
                            if (!$scope.data.field) {
                                $mainServices.alertTip('warning', '请输入' + title);
                            }else{
                                obj[field] = $scope.data.field;
                                $presonalServices.updateInfo(obj, function (data) {
                                    if(data.success){
                                        initInfo();
                                        $mainServices.alertTip('success', '更新'+title+'成功')
                                    }else{
                                        $mainServices.alertTip('wanring', '更新'+title+'成功')
                                    }

                                });
                            }
                        }
                    },
                ]
            });
        };
        $scope.getAddressList = function(level,parent_id){
        	$presonalServices.getAddressList2(level,parent_id,function(data){
        		if(level=='1'){
                	$scope.provincelist = data;
                }else if(level=='2'){
                	$scope.citylist = data;
                    $scope.formData.city = data[0].id;
                    $presonalServices.getAddressList2('3', data[0].id, function (data) {
                        $scope.districtlist = data;
                        $scope.formData.district = data[0].id;
                    })
                }else if(level=='3'){
                    $scope.formData.district = data[0].id;
                	$scope.districtlist = data;
                }
        	})
        };
        $scope.getAddressList(1,0);

        //分公司
        $presonalServices.getAllBranches(function (data) {
            $scope.branches = data;
        });
        $scope.openBranch = function ($event) {
            var popover;
            $ionicPopover.fromTemplateUrl("branchs.popover.html", {
                scope: $scope
            }).then(function(data){
                popover = data;
                popover.show($event);
            });
            $scope.setBranch = function (item, field) {
                var obj = {};
                obj[field] = item.id;
                $presonalServices.updateInfo(obj, function (data) {
                    if(data.success){
                        initInfo();
                        popover.remove();
                        $mainServices.alertTip('success', '更新分公司成功')
                    }else{
                        $mainServices.alertTip('wanring', '更新分公司成功')
                    }

                });
            }
        };
    }

    ctrl.$inject = ['$scope', '$presonalServices', '$ionicNavBarDelegate', '$ionicModal', '$mainServices', '$ionicPopup', '$ionicLoading', '$ionicPopover'];
    return ctrl;

});