import { InteractActions } from "./InteractActions";

/**
 * InteractActions for an One view chess game
 * @class
 * @constructor
 * @augments InteractActions
 * @param {object} stompClient stomp connectiob object
 * @param {string} gameUID game identification number
 */
export class OneViewInteractActions extends InteractActions {
    constructor(stompClient, gameUID) {
        super(stompClient, gameUID);
    }

    public onDrop(event) {
        this.destId = event.target.id;
        this.startOfMove = true;

        const move = `${this.sourceId}-${this.destId}`;

        this.stompClient.send(`/app/oneViewMove/${this.gameUID}`, { priority: 9 }, move);
    }
}
