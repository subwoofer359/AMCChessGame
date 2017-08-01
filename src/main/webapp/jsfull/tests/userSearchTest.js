/**
 * Unit tests for userSearch.js
 */

/*global QUnit */
/*global $ */
/*global sinon */
/*global jQuery */
/*global userSearch */
/*global setTimeout */

var $fixture;
QUnit.module('UserSearch tests', {
    beforeEach: function () {
        'use strict';
        $fixture = $('#qunit-fixture');
        $fixture.append('<div><button class="play-button btn-primary"></button></div>');
    }
});

QUnit.test('testing requestGame ajax request sent', function (assert) {
    'use strict';

    sinon.stub(jQuery, 'ajax');

    var done = assert.async(),
        userName = 'Ted',
        request = userSearch().requestGame;

    request(userName);
    setTimeout(function () {
        assert.ok(jQuery.ajax.calledWithMatch({ url: './requestGame'}), 'wrong url');
        done();
        jQuery.ajax.restore();
    });

});

function testAsyncCallRequestUserName(assert, properties, success) {
    'use strict';
    var done = assert.async(),
        userName = 'Ted',
        request = userSearch().requestGame,
        $button;

    /*jslint unparam: true*/
    sinon.stub(jQuery, 'post', function (url, data, callback) {
        callback(success);
    });

    request(userName);
    setTimeout(function () {
        $button = $('.play-button');
        assert.ok($button.hasClass(properties.hasClass), 'Button doesn\'t have class \'' + properties.hasClass + '\'');
        assert.notOk($button.hasClass(properties.shouldnotHaveClass), 'Button does have class \'' + properties.shouldnotHaveClass + '\'');
        assert.equal(properties.messageReceived, $button.html());
        assert.ok($button.attr('disabled'));
        done();
        jQuery.post.restore();
    });
}

QUnit.test('testing playButton updated on success', function (assert) {
    'use strict';

    var SUCCESS = true;

    testAsyncCallRequestUserName(assert, {
        hasClass : 'btn-success',
        shouldnotHaveClass : 'btn-primary',
        messageReceived : 'request sent'
    }, SUCCESS);

});

QUnit.test('testing playButton updated on failure', function (assert) {
    'use strict';

    var FAILURE = false;

    testAsyncCallRequestUserName(assert, {
        hasClass : 'btn-danger',
        shouldnotHaveClass : 'btn-primary',
        messageReceived : 'request failed'
    }, FAILURE);
});

QUnit.test('testing for empty searchForUsername', function (assert) {
    'use strict';
    $fixture.append('<input type="text" id="userName" value="norman"/>');
    $fixture.append('<table id="search-box"><tbody></tbody></table>');

    var done = assert.async(),
        search = userSearch().searchForUserName,
        $tbody;

    /*jslint unparam: true*/
    sinon.stub(jQuery, 'post', function (url, data, callback) {
        callback('[]');
    });
    /*jslint unparam: false*/

    search();

    setTimeout(function () {
        $tbody = $('#search-box tbody');
        assert.ok(/Search returned no results/.exec($tbody.html()));
        jQuery.post.restore();
        done();
    });
});

QUnit.test('testing for user with searchForUsername', function (assert) {
    'use strict';
    $fixture.append('<input type="text" id="userName" value="norman"/>');
    $fixture.append('<table id="search-box"><tbody></tbody></table>');

    var done = assert.async(),
        search = userSearch().searchForUserName,
        userName = 'norman',
        fullName = 'Norman Munz';

    /*jslint unparam: true*/
    sinon.stub(jQuery, 'post', function (url, data, callback) {
        callback('[{"userName" : "' + userName + '", "fullName" : "' + fullName + '"}]');
    });
    /*jslint unparam: false*/

    search();

    setTimeout(function () {
        var $userNameRow = $('#search-box tbody tr');
        assert.equal(userName, $userNameRow.find('td').html());
        jQuery.post.restore();
        done();
    });
});

QUnit.test('testing addTableRowListener or play button added', function (assert) {
    'use strict';
    $fixture.append('<table id="search-results"><tbody><tr><td class="userName"></td><td class="buttonHolder"></td></tr></tbody></table>');

    var done = assert.async();
    userSearch().addTableRowListener();

    $('#search-results tbody tr').trigger('click');
    setTimeout(function () {
        assert.ok($('.buttonHolder').find('button'));
        done();
    });
});

QUnit.test('testing addTableRowListener for play-button click', function (assert) {
    'use strict';
    $fixture.append('<table id="search-results"><tbody><tr><td class="userName"></td><td class="buttonHolder"></td></tr></tbody></table>');

    var done = assert.async();

    /*jslint unparam: true*/
    sinon.stub(jQuery, 'post', function (url, data, callback) {
        assert.equal('./requestGame', url);
    });
    /*jslint unparam: false*/

    userSearch().addTableRowListener();

    $('#search-results tbody tr').trigger('click');
    $('.play-button').trigger('click');
    setTimeout(function () {
        assert.ok($('.buttonHolder').find('button'));
        done();
        jQuery.post.restore();
    });
});