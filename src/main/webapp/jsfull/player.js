/*global $*/
function updatePlayer(message) {
    "use strict";
    var jsonPlayer, name, whitePlayerName, whiteNameHolder, blackNameHolder;
    jsonPlayer = $.parseJSON(message).currentPlayer;
    name = jsonPlayer.player.name;

    whitePlayerName = $("#white-player").find("span.name").text();
    whiteNameHolder = $("#white-player").find("span.name");
    blackNameHolder = $("#black-player").find("span.name");


    if (whitePlayerName === name) {
        whiteNameHolder.addClass("player-name-highlight");
        blackNameHolder.removeClass("player-name-highlight");
    } else {
        whiteNameHolder.removeClass("player-name-highlight");
        blackNameHolder.addClass("player-name-highlight");
    }
}