/*global define */

define(['app','services'/*,'mainDirective'*/], function(app){
    'use strict';

    var directives = angular.module('app.directives', ['app.services']);

    directives.directive('cMark',[function(){
        return {
            restrict: 'E',
            replace: true,
            scope:{
                mark: "="
            },
            template:'<span><i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(1)"></i>' +
            '<i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(2)"></i>' +
            '<i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(3)"></i>' +
            '<i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(4)"></i>' +
            '<i class="ion-ios-star mark" style="color: #d1d1d1;font-size: 18px;margin-left: 4px;" ng-click="doMark(5)"></i></span>',
            link:function(scope){
                scope.doMark = function(num){
                    var mark = document.getElementsByClassName("mark");
                    for(var i = 0;i < 5; i++){
                        mark[i].style.color = '#d1d1d1';
                    }
                    for(var i = 0;i < num; i++){
                        mark[i].style.color = '#E6D932';
                    }
                    scope.mark = num;
                }
            }
        }
    }])
    .directive('cPhoto', function($ionicBackdrop, $ionicPopup){
        return{
            restrict: 'E',
            scope:{},
            templateUrl: './templates/directive/photo.tpl.html',
            link: function(scope){
                scope.cameraClick = function(){
                    angular.element("input[type='file']").click();
                };
                var $uploaderInput = angular.element("#uploaderInput"),
                    uploadImgs = [];
                $uploaderInput.on("change", function(e){
                    var src, url = window.URL || window.webkitURL || window.mozURL, files = e.target.files, timestamp = +new Date();
                    for (var i = 0, len = files.length; i < len; ++i) {
                        var file = files[i];
                        if (url) {
                            src = url.createObjectURL(file);
                        } else {
                            src = e.target.result;
                        }
                        uploadImgs.push({
                            src: src,
                            name: file.name,
                            timestamp: timestamp,
                            readableSize: bytesToSize(file.size)
                        });
                    };
                    scope.uploadImgs = uploadImgs;
                    setTimeout(function () {
                        angular.forEach(uploadImgs, function (file) {
                            uploadFile(file)
                        })
                    })
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

                scope.removeImg = function(index){
                    uploadImgs.splice(index, 1);
                    scope.uploadImgs = uploadImgs;
                };
                function bytesToSize(bytes) {
                    var sizes = ['Bytes', 'KB', 'MB'];
                    if (bytes == 0) return 'n/a';
                    var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
                    return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
                };
                function createImg(imgObj){
                    var imgHTML,
                        img = new Image();
                    img.src = imgObj.src;
                    imgHTML = '<img ng-click="checkOver()" width="100%" alt="" src="'+ img.src +'">';
                    return imgHTML;
                };
                function uploadFile(file) {
                    var url = 'http://192.168.1.41:8080/oa/file/upload';
                    var xhr = new XMLHttpRequest();
                    var fd = new FormData();
                    xhr.open("POST", url, true);
                    xhr.setRequestHeader("POWERED-BY-MENGXIANHUI", "Approve");
                    xhr.setRequestHeader("Content-Type", "application/xml");
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState == 4 && xhr.status == 200) {
                            var data = JSON.parse(xhr.responseText)
                                , url = 'http://192.168.1.41:8080/upload/files/' + data.name
                                , curTimestamp = file.timestamp;
                            file.id = data.id;
                            console.log(data, url)
                            //fileIds.push(data.id);
                            //scope.output = scope.outputType=='arr'?fileIds:fileIds + '';
                            //_setIcon(curTimestamp, url, data.type);
                            /*scope.$apply(function () {
                                scope.urlMap[curTimestamp] = url;
                            })*/
                        }else{
                            console.log('不允许跨域')
                        }
                    };
                    xhr.upload.onprogress = function (e) {
                        if (e.lengthComputable) {
                            var percent = Math.round(e.loaded / e.total);
                            console.log(percent);
                            //_setProgress(file.timestamp, percent * 100);
                        }
                    };
                    fd.append("file", file);
                    xhr.send(fd);
                }
            }
        }
    });
    
    return directives;

});