/*
 * @file To set up the interact.js library to allow the user to move the chess pieces on the board
 * @author Adrian McLaughlin
 */

/*global interact*/
/*global console*/

/*
 * Creates an InteractActions object
 * To be used by Interact.js
 * @class
 * @access private
 * @param {stompConnection} stompClient - STOMP Connection to server
 * @param {string} gameUID - String represention of the Game unique number
 */
var InteractActions = function (stompClient, gameUID) {
    "use strict";
    this.stompClient = stompClient;
    this.gameUID = gameUID;
    this.ACTION_CLASSES = {
        DROP_ACTIVE: 'drop-active',
        DROP_TARGET: 'drop-target',
        CAN_DROP: 'can-drop'
    };
    this.startOfMove = true;
    this.sourceId = "";
    this.destId = "";
};

InteractActions.prototype = {
    onMove : function (event) {
        "use strict";
        var target = event.target,
            // keep the dragged position in the data-x/data-y attributes
            x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
            y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy,
            correctY = target.transform.baseVal[0].matrix.f,
            correctX = target.transform.baseVal[0].matrix.e;
        // translate the element
        target.style.webkitTransform =
            target.style.transform =
            'translate(' + (x + correctX) + 'px, ' + (y + correctY) + 'px)';

        // update the posiion attributes
        target.setAttribute('data-x', x);
        target.setAttribute('data-y', y);
    },

    onDragEnter : function (event) {
        "use strict";
        console.log("Drop dragEnter");
        var draggableElement = event.relatedTarget,
            dropzoneElement = event.target;

            // feedback the possibility of a drop
        dropzoneElement.classList.add(this.ACTION_CLASSES.DROP_TARGET);
        draggableElement.classList.add(this.ACTION_CLASSES.CAN_DROP);
    },

    onDropActivate : function (event) {
        "use strict";
        console.log("Drop onDropActivate");
        // add active dropzone feedback
        event.target.classList.add(this.ACTION_CLASSES.DROP_ACTIVE);
    },

    onDragLeave : function (event) {
        "use strict";
        console.log("Drop onDragLeave");
        event.target.classList.remove(this.ACTION_CLASSES.DROP_TARGET);
        event.relatedTarget.classList.remove(this.ACTION_CLASSES.CAN_DROP);
        if (this.startOfMove) {
            this.sourceId = event.target.id;
            this.startOfMove = false;
        }
    },

    onDrop : function (event) {
        "use strict";
        console.log("Drop onDrop");
        this.destId = event.target.id;
        this.startOfMove = true;
        var move = this.sourceId + "-" + this.destId;
        this.stompClient.send("/app/move/" + this.gameUID, {priority: 9}, move);
    },

    onDropDeactivate : function (event) {
        "use strict";
        console.log("Drop deactivate");
        // remove active dropzone feedback
        event.target.classList.remove(this.ACTION_CLASSES.DROP_ACTIVE);
        event.target.classList.remove(this.ACTION_CLASSES.DROP_TARGET);
    }
};


/*
 * 
 */
function chessGameInteract(stompClient, gameUID) {
    "use strict";

    var DRAGGABLE_CLASS = '.draggable',
        DROPZONE_CLASS = '.dropzone',
        DROPZONE_ACCEPT_CLASS = '.chesspiece',
        RESTRICTION_ELEMENT = '#layer',
        actions = new InteractActions(stompClient, gameUID);

    interact(DRAGGABLE_CLASS).draggable({
        // enable inertial throwing
        inertia: true,
        // keep the element within the area of it's parent
        restrict: {
            restriction: RESTRICTION_ELEMENT,
            endOnly: true,
            elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
        },

        // call this function on every dragmove event
        onmove: function (event) {
            actions.onMove.call(actions, event);
        }
    });



    interact(DROPZONE_CLASS).dropzone({
        accept: DROPZONE_ACCEPT_CLASS,
        overlap: 0.1,
        ondropactivate: function (event) {
            actions.onDropActivate.call(actions, event);
        },
        ondragenter: function (event) {
            actions.onDragEnter.call(actions, event);
        },
        ondragleave: function (event) {
            actions.onDragLeave.call(actions, event);
        },
        ondrop: function (event) {
            actions.onDrop.call(actions, event);
        },
        ondropdeactivate: function (event) {
            actions.onDropDeactivate.call(actions, event);
        }
    });
}

