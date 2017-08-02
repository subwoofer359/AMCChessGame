/*
 * Creates an InteractActions object
 * To be used by Interact.js
 * @class
 * @access private
 * @param {stompConnection} stompClient - STOMP Connection to server
 * @param {string} gameUID - String represention of the Game unique number
 */
export class InteractActions {

    public readonly ACTION_CLASSES = {
        CAN_DROP: "can-drop",
        DROP_ACTIVE: "drop-active",
        DROP_TARGET: "drop-target",
    };

    public stompClient;

    public gameUID;

    public startOfMove = true;

    public sourceId = "";

    public destId = "";

    constructor(stompClient, gameUID) {
        this.stompClient = stompClient;
        this.gameUID = gameUID;
    }

    public onMove(event) {
        const target = event.target;
        const x = event.dx + target.transform.baseVal[0].matrix.e;
        const y = event.dy + target.transform.baseVal[0].matrix.f;

        target.setAttribute("transform", `translate(${x},${y})`);
    }

    public onDragEnter(event) {
        const draggableElement = event.relatedTarget;
        const dropzoneElement = event.target;

        dropzoneElement.classList.add(this.ACTION_CLASSES.DROP_TARGET);
        draggableElement.classList.add(this.ACTION_CLASSES.CAN_DROP);
    }

    public onDropActivate(event) {
        event.target.classList.add(this.ACTION_CLASSES.DROP_ACTIVE);
    }

    public onDragLeave(event) {
        event.target.classList.remove(this.ACTION_CLASSES.DROP_TARGET);
        event.relatedTarget.classList.remove(this.ACTION_CLASSES.CAN_DROP);
        if (this.startOfMove) {
            this.sourceId = event.target.id;
            this.startOfMove = false;
        }
    }

    public onDrop(event) {
        this.destId = event.target.id;
        this.startOfMove = true;

        const move = `${this.sourceId}-${this.destId}`;

        this.stompClient.send(`/app/move/${this.gameUID}`, { priority: 9 }, move);
    }

    public onDropDeactivate(event) {
        event.target.classList.remove(this.ACTION_CLASSES.DROP_ACTIVE);
        event.target.classList.remove(this.ACTION_CLASSES.DROP_TARGET);
    }
}
