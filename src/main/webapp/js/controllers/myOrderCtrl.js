define(function () {
    'use strict';
    function ctrl($scope, $ionicSlideBoxDelegate, $orderServices, $ionicModal, $mainServices, $ionicScrollDelegate, $ionicNavBarDelegate ,$chat, $ionicPopup) {
        $ionicNavBarDelegate.title('我的订单');
        var popover, modal;
        $scope.index = 0;
        function changeCss(type){
            switch (type){
                case 0:
                    $(".dot-line").css('left', '2%');
                    break;
                case 1:
                    $(".dot-line").css('left', '22%');
                    break;
                case 2:
                    $(".dot-line").css('left', '42%');
                    break;
                case 3:
                    $(".dot-line").css('left', '62%');
                    break;
                case 4:
                    $(".dot-line").css('left', '82%');
                    break;

            };
            $(".nav-box > ul > li > span").css('color', '#999');
            $(".nav-box > ul > li:eq("+type+") > span").css('color', '#333');
        }
        $scope.selectType = function(type){
            changeCss(type);
            $ionicSlideBoxDelegate.slide(type);
        };
        $scope.go_changed=function(index){
            changeCss(index)
        };

        $scope.doRefresh = function() {
            initOrders();
            $scope.$broadcast('scroll.refreshComplete');
        };
        function initOrders(){
            /**
             * 100：未接单101：未分配 102：未签到 103：进行中 104：完成
             * */
            $orderServices.getOrderList({status:'100',page: 0, size: 999}, function(data){
                $scope.noSignLIst = data.datalist;
            });
            $orderServices.getOrderList({status:'101',page: 0, size: 999}, function(data){
                $scope.unallocatedList = data.datalist;
            });
            $orderServices.getOrderList({status:'102',page: 0, size: 999}, function(data){
                $scope.noCheckInList = data.datalist;
            });
            $orderServices.getOrderList({status:'103',page: 0, size: 999}, function(data){
                $scope.ongoingList = data.datalist;
            });
            $orderServices.getOrderList({status:'104',page: 0, size: 999}, function(data){
                $scope.completeList = data.datalist;
            });
        }

        $scope.doRefresh();

        $scope.openDetail = function(item, $event) {
            var detailModal;
            $orderServices.getOneOrder(item.id, function (data) {
                console.log(data)
                $scope.display = data;
                $ionicModal.fromTemplateUrl('detail.modal.html', {
                    scope: $scope,
                    animation: 'slide-in-right'
                }).then(function(data) {
                    detailModal = data;
                    detailModal.show();
                });
            });
            $scope.closeDetail = function () {
                detailModal.hide();
            };

        };

        $scope.rated = function ($event, item) {
            $event.stopPropagation();
            $orderServices.getOneOrder(item.id, function (data) {
                $scope.ratedObj = data;
                $ionicModal.fromTemplateUrl('rate_modal.tpl.html', {
                    scope: $scope,
                    animation: 'slide-in-right'
                }).then(function(data) {
                    modal = data;
                    modal.show();
                });
                $scope.closeModal = function(){
                    modal.remove();
                };
                $scope.public = function (obj) {
                    var post = {
                        orderevaluationgroup: {
                            groupId:'',
                            groupStar: '',
                            details: ''
                        },
                        orderevaluationusers: []
                    };
                    post.orderevaluationgroup.groupId = obj.orderevaluationgroup.groupId;
                    post.orderevaluationgroup.orderId = obj.id;
                    post.orderevaluationgroup.groupStar = obj.orderevaluationgroup.groupStar;
                    post.orderevaluationgroup.details = obj.orderevaluationgroup.details;
                    angular.forEach(obj.orderevaluationusers, function (item) {
                        post.orderevaluationusers.push({
                            userId:item.userId,
                            userStar: item.userStar,
                            orderId: obj.id
                        })
                    });
                    $orderServices.doRated(post, function (data) {
                        if(data.success){
                            $scope.closeModal();
                            $mainServices.alertTip('success', data.msg);
                        }else{
                            $mainServices.alertTip('success', data.msg);
                        }
                    })
                }
            })
        };
        /**
         * IM chat
         * */
        $scope.data = {};
        var messages = {}, gloabIds = [], gloabId = '';
        $scope.messages = [];
        $scope.chatIM = function (id) {
            if(gloabIds.indexOf(id) < -1){
                gloabIds.push(id);
            }
            gloabId = id;
            $scope.messages = messages[id] || [];
            $ionicModal.fromTemplateUrl('templates/IMchat.html', {
                scope: $scope,
                animation: 'slide-in-right'
            }).then(function(data) {
                modal = data;
                modal.show();
            });
            $scope.closeIM = function () {
                modal.hide();
            };
            $scope.sendMessage = function(message) {
                if(!message) return;
                var d = new Date();
                $scope.messages.push({
                    text: message,
                    time: d,
                    position: 'right',
                    image: 'img/client.png'
                });
                $chat.sendMsg(message, 'ktbm_' + gloabId);
                messages[gloabId] = $scope.messages;
                delete $scope.data.message;
                $ionicScrollDelegate.scrollBottom(true);

            };
        };

        $chat.receiveMesg = function (msg) {
            var d = new Date();
            $scope.$apply(function () {
                $scope.messages.push({
                    text: msg.data,
                    time: d,
                    position: 'left',
                    image: 'img/yuangong.png'
                });
            });
            $ionicScrollDelegate.scrollBottom(true);
        }
    }

    ctrl.$inject = ['$scope', '$ionicSlideBoxDelegate', '$orderServices', '$ionicModal', '$mainServices', '$ionicScrollDelegate', '$ionicNavBarDelegate', '$chat', '$ionicPopup'];
    return ctrl;

});