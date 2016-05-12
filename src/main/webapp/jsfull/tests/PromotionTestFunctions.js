/*jshint unused:vars */
/*jslint unparam: true*/
/*exported  promotionFixture */
var promotionFixture = (function () {
    "use strict";
    function getStompClient() {
        var StompClient = function StompClient() {return this; };
        StompClient.prototype = {
            connect : function (header, callback) {
                callback();
            },
            subscribe : function (destination, callback) {
                if ("/user/queue/updates" === destination) {
                    this.userSubscribe = callback;
                } else {
                    this.topicSubscribe = callback;
                }
            },
            send : function (destination, priority, message) {
                return {
                    destination : destination,
                    priority : priority,
                    message : message
                };
            },
            getUserSubscribe : function () {
                return this.userSubscribe;
            },
            getTopicSubscribe : function () {
                return this.topicSubscribe;
            }
        };

        return new StompClient();
    }

    function sendStatusMessageToUser(promotionModule, promotionAction) {
        var stompClient = getStompClient(),
            squareOfPawn,
            message = {};

        message.headers = {};
        message.headers.TYPE = "STATUS";

        promotionModule.setUpStompConnection(stompClient, promotionAction.handleUserInteract);


        message.body = "PAWN_PROMOTION (A,1)";

        squareOfPawn =  stompClient.getUserSubscribe().call(stompClient, message);
        return squareOfPawn;
    }

    function sendStatusMessageToTopic(promotionModule, promotionAction) {
        var stompClient = getStompClient(),
            squareOfPawn,
            message = {};

        message.headers = {};
        message.headers.TYPE = "STATUS";

        promotionModule.setUpStompConnection(stompClient, promotionAction.handleUserInteract);
        message.body = "PAWN_PROMOTION (A,1)";

        squareOfPawn =  stompClient.getTopicSubscribe().call(stompClient, message);
        return squareOfPawn;
    }
    return {
        sendStatusMessageToUser : sendStatusMessageToUser,
        sendStatusMessageToTopic : sendStatusMessageToTopic
    };
}());