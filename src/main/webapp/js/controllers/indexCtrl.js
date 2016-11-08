define(/*['mainCtrl'],*/ function () {
    'use strict';
    function ctrl($scope, $ionicPopover, $ionicPopup, $ionicModal, $ionicNavBarDelegate,/*$mainServices, $indexServices,*/ $ionicTabsDelegate, $ionicSlideBoxDelegate) {
        //$mainServices.showLoading();
       /* $indexServices.get(function(data){

            if(data !== null && data !== undefined){
                $mainServices.hideLoding();
            }
            $scope.bannerList = data.banner.item;
            $scope.couponList = data.coupon.item;
            $scope.dazheList = data.tejia.item;
        });*/
        var _this = this;

        //$ionicNavBarDelegate.setTitle('订单详情');
        $scope.popover = $ionicPopover.fromTemplateUrl('my-popover.html', {
            scope: $scope
        });

        // .fromTemplateUrl() 方法
        $ionicPopover.fromTemplateUrl('my-popover.html', {
            scope: $scope
        }).then(function(popover) {
            $scope.popover = popover;
        });


        $scope.openPopover = function($event) {
            $scope.popover.show($event);
        };
        $scope.closePopover = function() {
            $scope.popover.hide();
        };
        // 清除浮动框
        $scope.$on('$destroy', function() {
            $scope.popover.remove();
        });
        // 在隐藏浮动框后执行
        $scope.$on('popover.hidden', function() {
            // 执行代码
        });
        // 移除浮动框后执行
        $scope.$on('popover.removed', function() {
            // 执行代码
        });

        function changeCss(type){
            switch (type){
                case 0:
                    $(".dot-line").css('left', '5%');
                    break;
                case 1:
                    $(".dot-line").css('left', '38%');
                    break;
                case 2:
                    $(".dot-line").css('left', '72%');
                    break;
            }
            $(".nav-box > ul > li > span").css('color', '#999');
            $(".nav-box > ul > li:eq("+type+") > span").css('color', '#333');
        }

        $scope.selectType = function(type){
            changeCss(type);
            $ionicSlideBoxDelegate.slide(type);
        };

        $scope.index = 0;
        $scope.go_changed=function(index){
            changeCss(index)
        };
        var second = 60, timer, flag = true;
        function getTimes(){
            var $code = $("#get-code"),
                _width = $code.width();

            $code.width(_width);
            $code.css('font-size', '14px')
            second--;
            flag = false;
            $code.text('重新发送'+second+'秒');
            timer = setTimeout(getTimes, 1000);
            if ( second <= 0 ){
                second = 60;
                flag = true;
                $code.text('获取验证码');
                clearTimeout(timer);
            }
        }
        $scope.getCode = function(){
            if(flag){
                getTimes();
            }
        };

        // 触发一个按钮点击，或一些其他目标
        $scope.addAddress = function() {
            $scope.data = {}

            // 一个精心制作的自定义弹窗
            var myPopup = $ionicPopup.show({
                template: '<input type="text" ng-model="data.wifi">',
                title: '<i class="icon ion-location"></i> <span>输入地址</span>',
                cssClass:'wx-popup',
                scope: $scope,
                buttons: [
                    { text: '取消' },
                    {
                        text: '确定',
                        type: 'button-positive',
                        onTap: function(e) {
                            if (!$scope.data.wifi) {
                                e.preventDefault();
                            } else {
                                return $scope.data.wifi;
                            }
                        }
                    },
                ]
            });

        };

        var modal;
        $scope.rated = function() {
            $ionicModal.fromTemplateUrl('rated_modal.tpl.html', {
                scope: $scope
            }).then(function(modalObj) {
                modal = modalObj;
                modal.show();
            });
        };
        $scope.closeModal = function() {
            modal.remove();
        };

        $ionicModal.fromTemplateUrl('recip_modal.tpl.html', {
               scope: $scope,
               animation: 'slide-in-up'
           }).then(function(modal) {
                 $scope.modal = modal;
        });

        $scope.goExchange = function(){
            $scope.modal.show();
        }

    }
    
    ctrl.$inject = ['$scope', '$ionicPopover', '$ionicPopup', '$ionicModal','$ionicNavBarDelegate',/*'$mainServices', '$indexServices', */'$ionicTabsDelegate', '$ionicSlideBoxDelegate'];
    return ctrl;
    
});