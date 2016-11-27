define(function () {
    'use strict';
    function ctrl($scope, $mainServices, $stateParams, $http, $state, $ionicNavBarDelegate) {
       var from = $stateParams.from,
           second = 60,
           timer,
           complete = true;
        if(from == 'personal'){
            $ionicNavBarDelegate.title('');
        }else{
            $ionicNavBarDelegate.title(' 注册');
        }
        function countdown(){
            var $code = $("#verification"),
                _width = $code.width();
            $code.width(_width);
            $code.css('font-size', '14px')
            second--;
            complete = false;
            $code.text('重新发送'+second+'秒');
            timer = setTimeout(countdown, 1000);
            if (second <= 0){
                second = 60;
                complete = true;
                $code.text('获取验证码');
                clearTimeout(timer);
            }
        };

        $scope.getVerificode = function(phone){
            if(complete){
                if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){
                    $mainServices.alertTip('warning', '手机号码有误，请重填');
                }else{
                    countdown();
                    $http.post('/msg/sendMsg', {phone: phone}).success(function(data){
                        if(!data.success){
                            $mainServices.alertTip('warning', data.msg);
                        }
                    });
                }
            }
        };

        $scope.doRegist = function(formData){
            if(!(/^1(3|4|5|7|8)\d{9}$/.test(formData.phone))){
                $mainServices.alertTip('warning', '手机号码有误，请重填');
            }else{
                console.log(formData)
                $http.post('msg/register', formData).success(function(data){
                    if(data.success){
                        $scope.formData = {};
                        $mainServices.alertTip('success', data.msg);
                        $state.go(from);
                    }else{
                        $mainServices.alertTip('warning', data.msg);
                    }
                });
            }
        }

    }

    ctrl.$inject = ['$scope', '$mainServices', '$stateParams', '$http', '$state', '$ionicNavBarDelegate'];
    return ctrl;

});