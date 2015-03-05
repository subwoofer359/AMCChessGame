/**
 * Makes the Sidebar the same same size as the window
 */
$(document).ready(function(){
    (function(){
        var sidebarLeft=$('.sidebar-left');
        sidebarLeft.css("height",window.innerHeight);
        $(window).resize(function(){
            var windowHeight=$( window ).height();
            console.log(windowHeight);
            sidebarLeft.css("height",windowHeight);
        });
    })()
});