/*global $*/

/*
 * add CSRF token to AJAX requests
 */
$(function () {
    "use strict";
    var token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});