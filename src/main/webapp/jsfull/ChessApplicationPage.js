/*global $*/
/*global document*/
/*global addTableRowListener*/
/*global setInterval*/
/*global playerName*/

$(document).ready(function () {
    "use strict";

    addTableRowListener();

    $(".join-button").click(function (event) {
        var selectedRow = $("tr.selected");
        if (selectedRow.length === 0) {
            event.preventDefault();
        } else {
            $(".join-button").attr("formaction", "joinGame");
        }
        return;
    });

    function getStatusSymbol(status) {
        var output = '<span class="glyphicon ';
        if (status === "AWAITING_PLAYER") {
            output += 'glyphicon-time"';
            output += ' title="Awaiting Player"';
        } else if (status === "IN_PROGRESS") {
            output += 'glyphicon-play-circle"';
            output += ' title="In Progress"';
        } else if (status === "FINISHED") {
            output += 'glyphicon-remove"';
            output += ' title="Game over"';
        }
        output += '> </span>';
        return output;
    }

    /**
     * Long polling for Table updates
     *
     */
    setInterval(function () {
        $.post("getGameMap", function (data) {
            var yourEntry = "",
                otherEntry = "",
                tempEntry,
                gameUUID,
                selectedRow = $(".games-table tbody tr").find("input:checked").val();
            for (gameUUID in data) {
                if (data.hasOwnProperty(gameUUID)) {
                    tempEntry = '<tr><td>' + gameUUID.toString().slice(-5) + '</td><td>' +
                        data[gameUUID].player.player.userName +
                        '</td><td>' + (data[gameUUID].opponent ? data[gameUUID].opponent.player.userName : "") +
                        '</td><td>' + getStatusSymbol(data[gameUUID].currentStatus) +
                        '</td><td><input type="checkbox" name="gameUUID" value="' +
                        gameUUID + '"></td></tr>';
                    if (playerName === data[gameUUID].player.player.name) {
                        yourEntry += tempEntry;
                    } else {
                        otherEntry += tempEntry;
                    }
                }
            }
            $("#your-games-table tbody").html(yourEntry);
            $("#other-games-table tbody").html(otherEntry);
            addTableRowListener();
            $(".games-table tbody tr").find("input:checkbox[value=" + selectedRow + "]").click();
        });
    }, 5000);
});