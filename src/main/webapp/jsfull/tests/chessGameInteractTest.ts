/*global QUnit*/
/*global InteractActions*/
/*global OneViewInteractActions*/
/*global console*/

import { InteractActions } from "../InteractActions";

const stompClient: any = {
    send(destination, priorityObj, moveString) {
        this.destination = destination;
        this.priorityObj = priorityObj;
        this.moveString = moveString;
    },
};

let event: any = {};
const gameUID: string = "3939393";

function createEvent() {

    event.target = {};
    event.relatedTarget = {};
    event.target.classList = {
        addedClasses : [],
        removedClasses : [],
    };
    event.relatedTarget.classList = {
        addedClasses : [],
        removedClasses : [],
    };

    event.relatedTarget.classList.remove = function(aClass) {
        this.removedClasses.push(aClass);
    };
    event.relatedTarget.classList.add = function(aClass) {
        this.addedClasses.push(aClass);
    };

    event.target.classList.remove = function(aClass) {
        this.removedClasses.push(aClass);
    };
    event.target.classList.add = function(aClass) {
        this.addedClasses.push(aClass);
    };

    return event;
}

QUnit.module("chessGameInteract Unit tests", {
    beforeEach: () => {
        "use strict";
        event = createEvent();
    },
});

QUnit.test("testing chessGameInteract: function onDropDeactivate ", (assert) => {
    "use strict";
    const interactActions = new InteractActions(stompClient, gameUID);
    interactActions.onDropDeactivate(event);

    assert.equal(event.target.classList.removedClasses.length, 2);
});

QUnit.test("chessGameInteract: function onDropActivate", (assert) => {
    "use strict";
    const interactActions = new InteractActions(stompClient, gameUID);
    interactActions.onDropActivate(event);
    assert.equal(event.target.classList.addedClasses.length, 1);
});

QUnit.test("chessGameInteract: function onDragEnter", (assert) => {
    "use strict";
    const interactActions = new InteractActions(stompClient, gameUID);
    interactActions.onDragEnter(event);
    assert.equal(event.target.classList.addedClasses.length, 1);
    assert.equal(event.relatedTarget.classList.addedClasses.length, 1);
});

QUnit.test("chessGameInteract: function onDrop", (assert) => {
    "use strict";
    const interactActions = new InteractActions(stompClient, gameUID);
    const expectedPriority = {priority: 9};
    const expectedDestination = "/app/move/" + gameUID;
    interactActions.sourceId = "A1";
    event.target.id = "A2";
    interactActions.onDrop(event);

    assert.equal(interactActions.destId, event.target.id);
    assert.equal(interactActions.startOfMove, true);
    assert.equal(stompClient.moveString, interactActions.sourceId + "-" + interactActions.destId);
    assert.equal(stompClient.priorityObj.priority, expectedPriority.priority);
    assert.equal(stompClient.destination, expectedDestination);
});

QUnit.test("chessGameInteract: function onDragLeave", (assert) => {
    "use strict";
    const interactActions = new InteractActions(stompClient, gameUID);

    event.target.id = "A1";
    interactActions.onDragLeave(event);

    assert.equal(interactActions.sourceId, event.target.id);
    assert.equal(interactActions.startOfMove, false);
    assert.equal(event.target.classList.removedClasses.length, 1);
    assert.equal(event.relatedTarget.classList.removedClasses.length, 1);

});
