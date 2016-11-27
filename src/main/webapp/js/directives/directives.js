define(['app','services'], function(app){
    'use strict';
    var directives = angular.module('app.directives', ['app.services']);
    directives.directive('cMark',[function(){
        return {
            restrict: 'E',
            replace: true,
            scope:{
                mark: "=",
                preset: '='
            },
            template:'<span><i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(1)"></i>' +
            '<i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(2)"></i>' +
            '<i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(3)"></i>' +
            '<i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(4)"></i>' +
            '<i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(5)"></i></span>',
            link:function(scope, element){
                scope.doMark = function(num){
                    var mark = $(element).find(".mark");
                    for(var i = 0;i < 5; i++){
                        mark[i].style.color = '#d1d1d1';
                    }
                    for(var i = 0;i < num; i++){
                        mark[i].style.color = 'red';
                    }
                    scope.mark = num;
                }
                if(scope.preset){
                    scope.doMark(parseInt(scope.preset));
                }
            }
        }
    }])
    .directive('cPhoto', function($ionicBackdrop, $ionicPopup, $ionicScrollDelegate, $mainServices){
        return{
            restrict: 'E',
            scope:{
                output: '='
            },
            templateUrl: 'templates/directive/photo.tpl.html',
            link: function(scope){
                scope.cameraClick = function(){
                    $("input[type='file']").click();
                };
                var $uploaderInput = $("#uploaderInput"),
                    popupImgs = [],
                    imgIds = [];
                    scope.urlMap = {};
                $uploaderInput.on("change", function(e){
                    var src,
                        url = window.URL || window.webkitURL || window.mozURL,
                        files = e.target.files,
                        timestamp = +new Date();
                    console.log(files, files.length)
                    if(files && files.length > 8){
                        $mainServices.alertTip('warning', '最多上传八张');
                        files = files.splice(0, 7);
                    }
                    for (var i = 0, len = files.length; i < len; ++i) {
                        var file = files[i];
                        if (url) {
                            src = url.createObjectURL(file);
                        } else {
                            src = e.target.result;
                        }
                        popupImgs.push({
                            src: src,
                            name: file.name,
                            timestamp: timestamp += 1
                        });
                        file.timestamp = popupImgs[i].timestamp;
                    };
                    scope.$apply(function () {
                        scope.display = popupImgs;
                    });
                    setTimeout(function () {
                        angular.forEach(files, function (file) {
                            uploadFile(file)
                        })
                    });
                    $ionicScrollDelegate.scrollBottom(true);
                });
                var popup;
                scope.checkImg = function(imgObj){
                    var temp = createImg(imgObj);
                        popup = $ionicPopup.show({
                            cssClass:'wx-popup',
                            template: temp,
                            title:imgObj.name,
                            scope: scope
                        });
                };

                scope.checkOver = function(){
                    popup.close();
                };

                scope.removeImg = function(file, index){
                    scope.display.splice(index, 1);
                    imgIds.splice(imgIds.indexOf(file.id), 1);
                    scope.output = imgIds + '';
                };

                function createImg(imgObj){
                    var imgHTML,
                        img = new Image();
                    img.src = imgObj.src;
                    imgHTML = '<img ng-click="checkOver()" width="100%" alt="" src="'+ img.src +'">';
                    return imgHTML;
                };
                function _setProgress(timestamp, percent) {
                    var fileProgress = document.getElementById('file-progress-' + timestamp);
                    fileProgress.style.width = percent + '%';
                };
                function bytesToSize(bytes) {
                    var sizes = ['Bytes', 'KB', 'MB'];
                    if (bytes == 0) return 'n/a';
                    var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
                    return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
                }
                function uploadFile(file) {
                    var url = '/ktbm/filesController/upload';
                    var xhr = new XMLHttpRequest();
                    var fd = new FormData();
                    xhr.open("POST", url, true);
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState == 4 && xhr.status == 200) {
                            var data = JSON.parse(xhr.responseText)
                                , url = 'upload/files/' + data.name
                                , curTimestamp = file.timestamp;
                            file.id = data.id;
                            imgIds.push(file.id);
                            scope.output = imgIds + '';
                            console.log(scope.output)
                            scope.$apply(function () {
                                scope.output = imgIds + '';
                            })
                        }
                    };
                    xhr.upload.onprogress = function (e) {
                        if (e.lengthComputable) {
                            var percent = Math.round(e.loaded / e.total);
                            _setProgress(file.timestamp, percent * 100);
                        }
                    };
                    fd.append("file", file);
                    xhr.send(fd);
                }
            }
        }
    }).directive('errSrc', function() {
        return {
            link: function(scope, element, attrs) {
                element.bind('error', function() {
                    if (attrs.src != attrs.errSrc) {
                        attrs.$set('src', attrs.errSrc);
                    }
                });
            }
        }
    })
   
    return directives;

});