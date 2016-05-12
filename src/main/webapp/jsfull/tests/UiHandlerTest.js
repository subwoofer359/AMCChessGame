/*global QUnit*/
/*global $*/
/*global promotionAction*/

QUnit.module("UIHandler tests", {
    beforeEach: function () {
        "use strict";
        var $fixture;

        $fixture = $("#qunit-fixture");
        $fixture.append('<div id="promotionDialog" class="container hidePromotionDialog"><div class="row"><div class="panel-primary"><div class="panel-heading"><h3 class="panel-title">Promote Chess Piece</h3></div><div class="panel-body">      <div class="row"><div class="col-xs-12 col-md-3"><button id="queenBtn" class="btn btn-primary" type="button"><img src="../../img/Queen.svg" alt="Queen"></button></div><div class="col-xs-12 col-md-3"><button id="rookBtn" class="btn btn-primary" type="button"><img src="../../img/Rook.svg" alt="Rook"></button></div><div class="col-xs-12 col-md-3">             <button id="knightBtn" class="btn btn-primary" type="button"><img src="../../img/Knight.svg" alt="Knight"></button>         </div><div class="col-xs-12 col-md-3"><button id="bishopBtn" class="btn btn-primary" type="button"><img src="../../img/Bishop.svg" alt="Bishop"></button></div></div></div></div></div></div>');
    }
});

QUnit.test("testing player colour updated in UIHandler closure", function (assert) {
    "use strict";
    assert.expect(1);

    var player = {
            colour : "WHITE"
        },
        callback = function (piece) { assert.equal("Q", piece); };
    promotionAction.handleUserInteract(player, callback);

    player.colour = "BLACK";

    $("#queenBtn").trigger("click");
});