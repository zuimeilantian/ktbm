/**
 * Created by wz on 2016/11/8.
 */
define(function () {
    'use strict';
    function ctrl($scope, $registerServices, $ionicPopup, $mainServices) {
        var second = 60, timer, flag = true;
        function countdown(){
            var $code = $("#verification"),
                _width = $code.width();

            $code.width(_width);
            $code.css('font-size', '14px')
            second--;
            flag = false;
            $code.text('重新发送'+second+'秒');
            timer = setTimeout(getTimes, 1000);
            if (second <= 0){
                second = 60;
                flag = true;
                $code.text('获取验证码');
                clearTimeout(timer);
            }
        }
        $scope.getVerificode = function(phone){
            if(flag){
                if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){
                    $mainServices.alert('warning', '手机号码有误，请重填');
                }else{
                    countdown();
                    /* $registerServices.getCode('xx', function(data){
                        console.log(data)
                     })*/
                }
            }
        };


    }

    ctrl.$inject = ['$scope', '$registerServices', '$ionicPopup', '$mainServices'];
    return ctrl;

});