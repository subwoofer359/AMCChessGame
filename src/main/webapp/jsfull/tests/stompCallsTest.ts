/*global QUnit*/
/*global $*/
/*global chessgameportal*/
/*global console*/
/*jslint unparam: true*/

import * as chessGamePortal from "../chessGamePortal";
import { StompActions } from "../StompActions";
import { StompObject } from "../StompObject";

const SockJS = function(URL) {
    this.url = URL;
};
const Stomp: any = {};
const subscriptions = [];
const messages = [];
const APP_GET = "/app/get/";
const USER_UPDATES = "/user/queue/updates";
const TOPIC_UPDATES = "/topic/updates/";

QUnit.module("Stomp Calls Test", {
    beforeEach : () => {
        Stomp.over = (socket) => {
            return {
                connect : (object, connectCallBack, errorCallBack) => {
                    connectCallBack();
                },

                send : (aDestination, aPriority, aMessage) => {
                    messages.push({
                        destination : aDestination,
                        message : aMessage,
                        priority : aPriority,
                    });
                },

                subscribe : (destination, callBack) => {
                    subscriptions.push(destination);
                },
            };
        };
    },
});

QUnit.test("testing Stomp Calls Test", (assert) => {
    let sentMessage;

    chessGamePortal.setSockJs(SockJS);
    chessGamePortal.setStompjs(Stomp);

    const stompObject = new StompObject("url", "gameUID", "playerName", "opponentName", "playerColour");

    chessGamePortal.openStompConnection(stompObject, new StompActions(stompObject));

    assert.equal(subscriptions.indexOf(USER_UPDATES), 0);
    assert.equal(subscriptions.indexOf(TOPIC_UPDATES + "gameUID"), 1);

    sentMessage = messages.pop();

    assert.equal(sentMessage.destination, APP_GET + "gameUID");
    assert.equal(sentMessage.message, "Get ChessBoard");
});
