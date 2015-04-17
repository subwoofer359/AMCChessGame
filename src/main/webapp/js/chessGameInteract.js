/*global interact*/
function chessGameInteract(stompClient, gameUID) {
    "use strict";

    var DRAGGABLE_CLASS = '.draggable',
        DROPZONE_CLASS = '.dropzone',
        DROPZONE_ACCEPT_CLASS = '.chesspiece',
        RESTRICTION_ELEMENT = '#layer#',
        ACTION_CLASSES = {
            DROP_ACTIVE: 'drop-active',
            DROP_TARGET: 'drop-target',
            CAN_DROP: 'can-drop'
        },
        startOfMove = true,
        sourceId = "",
        destId = "";

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
        }
    });


    interact(DROPZONE_CLASS).dropzone({
        accept: DROPZONE_ACCEPT_CLASS,
        overlap: 0.1,
        ondropactivate: function (event) {
            // add active dropzone feedback
            event.target.classList.add(ACTION_CLASSES.DROP_ACTIVE);
        },
        ondragenter: function (event) {
            var draggableElement = event.relatedTarget,
                dropzoneElement = event.target;

            // feedback the possibility of a drop
            dropzoneElement.classList.add(ACTION_CLASSES.DROP_TARGET);
            draggableElement.classList.add(ACTION_CLASSES.CAN_DROP);

        },
        ondragleave: function (event) {
            event.target.classList.remove(ACTION_CLASSES.DROP_TARGET);
            event.relatedTarget.classList.remove(ACTION_CLASSES.CAN_DROP);
            if (startOfMove) {
                sourceId = event.target.id;
                startOfMove = false;
            }
        },
        ondrop: function (event) {
            destId = event.target.id;
            startOfMove = true;
            var move = sourceId + "-" + destId;
            stompClient.send("/app/move/" + gameUID, {priority: 9}, move);
        },
        ondropdeactivate: function (event) {
            // remove active dropzone feedback
            event.target.classList.remove(ACTION_CLASSES.DROP_ACTIVE);
            event.target.classList.remove(ACTION_CLASSES.DROP_TARGET);
        }
    });
}