/*global $*/

/*global WebSocket*/
/*global updatePlayer*/
/*global chessboardModule*/
/*global Stomp*/
/*global SockJS*/
/*global headers*/

import * as _SockJS from "sockjs-client";
import * as _Stomp from "stompjs";
import "./chessGameInteract";
import { ChessSounds } from "./ChessSounds";
import { InteractActions } from "./InteractActions";
import { OneViewStompActions } from "./OneViewStompActions";
import { StompActions } from "./StompActions";
import { StompObject } from "./StompObject";

let SockJS = _SockJS;
let Stomp = _Stomp;

const sounds = new ChessSounds();

/**
 * Opens a Websocket connection or display a message if the connection fails
 * @param {url} websocketURL url to open a websocket connection
 * @param {object} headers contains values to place into the STOMP headers
 * @param {StompActions} stompCallBack callback object
 */
export function openStompConnection(stompObj: StompObject, stompCallBack) {
    let stompClient;
    let socket;
    const USER_UPDATES = "/user/queue/updates";
    const TOPIC_UPDATES = "/topic/updates/";
    const APP_GET = "/app/get/";
    const PRIORITY = {priority : 9};
    const APP_QUIT = "/app/quit/";
    const APP_SAVE = "/app/save/";

    socket = new SockJS(stompObj.url);
    stompClient = Stomp.over(socket);
    stompClient.connect(stompObj.headers, function onStompConnect() {
        stompClient.subscribe(USER_UPDATES, (message) => {
            stompCallBack.userUpdate.call(stompCallBack, message);
        });

        stompClient.subscribe(TOPIC_UPDATES + stompCallBack.gameUID, (message) => {
            stompCallBack.topicUpdate.call(stompCallBack, message);
        });

        $(".quit-btn").click(() => {
            stompClient.send(APP_QUIT + stompCallBack.gameUID, PRIORITY, "quit");
        });

        $(".save-btn").click(() => {
            stompClient.send(APP_SAVE + stompCallBack.gameUID, PRIORITY, "save");
        });
        stompClient.send(APP_GET + stompCallBack.gameUID, PRIORITY, "Get ChessBoard");
    }, function onStompError() {
        stompCallBack.showAlertMessage.call(stompCallBack, "Couldn't connect to the Chess server");
    });
    return stompClient;
}

/**
 * Sets up the required objects before calling for websocket connection
 * Sets up the connection for two view chess game
 * @param {object} stompObject contains values required to open a connection
 * @returns stompClient STOMP connection object
 */
export function setupStompConnection(stompObject: StompObject) {
    const stompCallBack = new StompActions(stompObject);
    stompCallBack.setSounds(sounds);
    const stompClient = openStompConnection(stompObject, stompCallBack);

    chessGameInteract(new InteractActions(stompClient, stompObject.gameUUID), sounds);
    return stompClient;
}

/**
 * Sets up the required objects before calling for websocket connection
 * Sets up the connection for one view chess game
 * @param {object} stompObject contains values required to open a connection
 * @returns stompClient STOMP connection object
 */
export function setupOneViewStompConnection(stompObject) {
    const stompCallBack = new OneViewStompActions(stompObject);
    stompCallBack.setSounds(sounds);
    const stompClient = openStompConnection(stompObject, stompCallBack);

    chessGameInteract(new InteractActions(stompClient, stompObject.gameUUID), sounds);
    return stompClient;
}

export function setSockJs(mock) {
    SockJS = mock;
}

export function setStompjs(mock) {
    Stomp = mock;
}
