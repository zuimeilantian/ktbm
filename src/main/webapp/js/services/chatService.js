define(function(){
    'use strict'
    var service = function(){
        var config = WebIM.config = {
            xmppURL: 'im-api.easemob.com',
            apiURL: 'http://a1.easemob.com',
            appkey: '33oa#ktbm',
            https: '',
            isMultiLoginSessions: false,
            isAutoLogin: false
        };
        WebIM.Emoji = {};
        var conn = new WebIM.connection({
            https: config.https,
            url: config.xmppURL,
            isAutoLogin: config.isAutoLogin,
            isMultiLoginSessions: config.isMultiLoginSessions
        })
            , that = this;
        conn.onOpened = function (message) {
            conn.setPresence();
        };
        conn.onError = function (message) {
            console.error(message);
        };
        conn.onTextMessage = function (message) {
            that.receiveMesg(message);
        };
        conn.onReceivedMessage = function (cum) {
            console.log(cum);
            
        };
        this.open = function (user) {
            var options = {
                apiUrl: WebIM.config.apiURL,
                user: user.username,
                pwd: user.pwd,
                appKey: config.appkey
            };
            conn.open(options);
        };
        this.sendMsg = function (semen, to) {
            var id = conn.getUniqueId();
            var cum = new WebIM.message('txt', id);
            cum.set({
                msg: semen,
                to: to,
                success: function (id, serverMsgId) {
                    console.log(id , serverMsgId)
                }
            });
            conn.send(cum.body);
        };
        
    };
    service.$inject = [];
    return service;
});