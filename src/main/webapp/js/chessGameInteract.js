function chessGameInteract(stompClient, gameUID) {
    "use strict";

    var startOfMove = true,
        sourceId = "",
        destId = "";

    interact('.draggable').draggable({
        // enable inertial throwing
        inertia: true,
        // keep the element within the area of it's parent
        restrict: {
            restriction: '#layer1',
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
        // call this function on every dragend event
    });


    interact('.dropzone').dropzone({
        accept: '.chesspiece',
        overlap: 0.1,
        ondropactivate: function (event) {
            // add active dropzone feedback
            event.target.classList.add('drop-active');
        },
        ondragenter: function (event) {
            var draggableElement = event.relatedTarget,
                dropzoneElement = event.target;

            // feedback the possibility of a drop
            dropzoneElement.classList.add('drop-target');
            draggableElement.classList.add('can-drop');

        },
        ondragleave: function (event) {
            event.target.classList.remove('drop-target');
            event.relatedTarget.classList.remove('can-drop');
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
            event.target.classList.remove('drop-active');
            event.target.classList.remove('drop-target');
        }
    });
}