/*global QUnit*/
/*global $*/
QUnit.module("chessboard tests", {
    beforeEach: function () {
        "use strict";
        $("#qunit-fixture").append("<input type='text' id='userName'/>");
        $("#qunit-fixture").append("<input type='password' id='password1'/>");
        $("#qunit-fixture").append("<input type='password' id='password2'/>");
    }
});

QUnit.test("testing user.js: checkPasswordSame", function (assert) {
    "use strict";
    
    $("#password1").val("password");
    $("#password2").val("password");
    assert.ok(loginModule.checkPassword("password1", "password2"));
});

QUnit.test("testing user.js: checkPasswordNotSame", function (assert) {
    "use strict";
    
    $("#password1").val("password");
    $("#password2").val("differentPassword");
    assert.notOk(loginModule.checkPassword("password1", "password2"));
});

