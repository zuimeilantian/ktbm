/**
 * Created by wz on 2016/11/8.
 */
define(function () {
    'use strict';
    function ctrl($scope, $registerServices) {
        var second = 60, timer, flag = true;
        function getTimes(){
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
        $scope.getVerificode = function(){
            if(flag){
                console.log('111')
                getTimes();
            }
        };

       /* $registerServices.getCode('xx', function(data){
            console.log(data)
        })*/

    }

    ctrl.$inject = ['$scope', '$registerServices'];
    return ctrl;

});