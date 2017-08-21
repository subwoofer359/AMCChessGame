/*
 * @file To set up the interact.js library to allow the user to move the chess pieces on the board
 * @author Adrian McLaughlin
 */

/*global interact*/

function chessGameInteract(interactCallBack, sounds) {
    "use strict";

    var DRAGGABLE_CLASS = '.draggable',
        DROPZONE_CLASS = '.dropzone',
        DROPZONE_ACCEPT_CLASS = '.chesspiece',
        RESTRICTION_ELEMENT = '#layer',
        actions = interactCallBack,
        sound = sounds;

    interact(DRAGGABLE_CLASS).draggable({
        // enable inertial throwing
        inertia: false,
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
            sound.playMoveSound();
        },
        ondropdeactivate: function (event) {
            actions.onDropDeactivate.call(actions, event);
        }
    });
}
