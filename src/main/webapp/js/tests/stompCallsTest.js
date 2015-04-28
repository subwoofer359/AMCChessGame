/*global QUnit*/
/*global $*/
/*global console*/
/*global openStompConnection*/
/*global StompActions*/
/*jslint unparam: true*/

var WebSocket = function (URL) {
    "use strict";
    this.url = URL;
},
    Stomp = {},
    subscriptions = [],
    messages = [],
    APP_GET = "/app/get/",
    USER_UPDATES = "/user/queue/updates",
    TOPIC_UPDATES = "/topic/updates/";

QUnit.module("Stomp Calls Test", {
    beforeEach : function () {
        "use strict";
        Stomp.over = function (socket) {
            console.log(socket);
            return {
                connect : function (object, connectCallBack, errorCallBack) {
                    connectCallBack();
                },

                send : function (aDestination, aPriority, aMessage) {
                    messages.push({
                        destination : aDestination,
                        priority : aPriority,
                        message : aMessage
                    });
                },

                subscribe : function (destination, callBack) {
                    subscriptions.push(destination);
                }
            };
        };
    }
});

QUnit.test("testing Stomp Calls Test", function (assert) {
    "use strict";
    var sentMessage;
    openStompConnection("", new StompActions("gameUID", "playerName", "opponentName", "playerColour"));
    assert.equal(subscriptions.indexOf(USER_UPDATES), 0);
    assert.equal(subscriptions.indexOf(TOPIC_UPDATES + "gameUID"), 1);
    sentMessage = messages.pop();
    assert.equal(sentMessage.destination, APP_GET + "gameUID");
    assert.equal(sentMessage.message, "Get ChessBoard");
});


