define(function () {
    'use strict';
    function ctrl($scope, $presonalServices, $bonusService, $ionicModal, $ionicScrollDelegate, $chat) {
        var modal;
        $presonalServices.getUserInfo(function (data) {
            $scope.point = data.points;
        });
        $scope.showRecord = function () {
            $ionicModal.fromTemplateUrl('templates/bonus-info.html', {
                scope: $scope,
                animation: 'slide-in-right'
            }).then(function(data) {
                modal = data;
                $bonusService.getRecord({flag:1, type:2,page:0, size: 999}, function (data) {
                    $scope.bonusInfo = data.datalist;
                    modal.show();
                });
            });
            
            $scope.close = function () {
                modal.hide();
            }
        };

    }

    ctrl.$inject = ['$scope', '$presonalServices', '$bonusService', '$ionicModal', '$ionicScrollDelegate', '$chat'];
    return ctrl;

});