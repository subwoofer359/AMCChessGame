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

    function setUp(promotionModule, promotionAction) {
        var stompClient = getStompClient(),
            message = {
                headers : {
                    TYPE : "STATUS"
                },
                body : "PAWN_PROMOTION (A,1)"
            },
            connection;

        connection = new promotionModule.OneViewStompConnection();
        connection.stompClient = stompClient;
        connection.setUpStompConnection(promotionAction.handleUserInteract);

        return {
            message : message,
            stompClient : stompClient
        };
    }

    function sendStatusMessageToUser(promotionModule, promotionAction) {
        var squareOfPawn,
            obj = setUp(promotionModule, promotionAction);
        squareOfPawn =  obj.stompClient.getUserSubscribe().call(obj.stompClient, obj.message);
        return squareOfPawn;
    }

    function sendStatusMessageToTopic(promotionModule, promotionAction) {
        var squareOfPawn,
            obj = setUp(promotionModule, promotionAction);

        squareOfPawn =  obj.stompClient.getTopicSubscribe().call(obj.stompClient, obj.message);
        return squareOfPawn;
    }

    function sendErrorMessageToTopic(promotionModule, promotionAction) {
        var squareOfPawn,
            obj = setUp(promotionModule, promotionAction),
            message = {
                headers : { TYPE : "ERROR" },
                body : "Error can't promoted other player piece"
            };

        squareOfPawn =  obj.stompClient.getTopicSubscribe().call(obj.stompClient, message);

        return squareOfPawn;
    }

    function sendErrorMessageToUser(promotionModule, promotionAction) {
        var squareOfPawn,
            obj = setUp(promotionModule, promotionAction),
            message = {
                headers : {
                    TYPE : "ERROR"
                },
                body : "Error can't promoted other player piece"
            };

        squareOfPawn =  obj.stompClient.getUserSubscribe().call(obj.stompClient, message);
        return squareOfPawn;
    }

    function sendStatusMessageToTwoViewTopic(promotionModule, promotionAction) {
        var squareOfPawn,
            stompClient = getStompClient(),
            message = {
                headers : {
                    TYPE : "STATUS"
                },
                body : "PAWN_PROMOTION (A,1)"
            },
            connection;

        connection = new promotionModule.TwoViewStompConnection();
        connection.stompClient = stompClient;
        connection.setUpStompConnection(promotionAction.handleUserInteract);

        squareOfPawn =  stompClient.getTopicSubscribe().call(stompClient, message);
        return squareOfPawn;
    }
    return {
        sendStatusMessageToUser : sendStatusMessageToUser,
        sendStatusMessageToTopic : sendStatusMessageToTopic,
        sendErrorMessageToTopic : sendErrorMessageToTopic,
        sendErrorMessageToUser : sendErrorMessageToUser,
        sendStatusMessageToTwoViewTopic : sendStatusMessageToTwoViewTopic,
        getStompClient : getStompClient
    };
}());