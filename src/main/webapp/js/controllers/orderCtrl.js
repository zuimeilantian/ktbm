/**
 * Created by wz on 2016/11/8.
 */
define(function () {
    'use strict';
    function ctrl($scope, $ionicSlideBoxDelegate, $orderServices) {
        $scope.index = 0;
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
        $scope.go_changed=function(index){
            changeCss(index)
        };
        $scope.doSubmit = function(formData){
            var validate = {}, count = 0;
            if(!formData){
                validate.linkName = true;
                validate.linkPhone = true;
                validate.addressId = true;
                validate.brand = true;
                validate.machineGroup = true;
                validate.machineType = true;
                validate.machineInnerType = true;
                validate.machineOuterType = true;
            }else{
                if(!formData.linkName) validate.linkName = true;
                if(!formData.linkPhone) validate.linkPhone = true;
                if(!formData.brand) validate.brand = true;
                if(!formData.machineGroup) validate.machineGroup = true;
                if(!formData.machineType) validate.machineType = true;
                if(!formData.machineInnerType) validate.machineInnerType = true;
                if(!formData.machineOuterType) validate.machineOuterType = true;
            }
            $scope.validate = validate;
            for(var key in validate){
                if(validate[key]) count++;
            }
            console.log(count)
            if(!count){
                console.log(11)
                $orderServices.saveOrder(formData, function(data){
                    console.log(data)
                })
            }
        }
        $scope.doFill = function(value, field){
            $scope.validate = $scope.validate || {};
            if(value){
                $scope.validate[field] = false;
            }else{
                $scope.validate[field] = true;
            }
        }

    }

    ctrl.$inject = ['$scope', '$ionicSlideBoxDelegate', '$orderServices'];
    return ctrl;

});