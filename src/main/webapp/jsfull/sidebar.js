/*global $*/
/*global document*/
/*global window*/

/**
 * Makes the Sidebar the same same size as the window
 */
$(document).ready(function () {
    "use strict";
    (function () {
        var sidebarLeft = $('.sidebar-left');
        sidebarLeft.css("height", window.innerHeight);
        $(window).resize(function () {
            var windowHeight = $(window).height();
            sidebarLeft.css("height", windowHeight);
        });
    }());
});