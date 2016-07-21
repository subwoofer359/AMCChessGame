
/*global $ */
/*global jQuery */
/*global console */
var userSearch = function () {
    "use strict";
    function requestGame(userName) {
        var formData = "userToPlay=" + userName;
        $.post('./requestGame', formData, function (data) {
            var $button = $(".play-button");
            if (data === true) {
                $button.removeClass("btn-primary");
                $button.addClass("btn-success");
                $button.html("request sent");
            } else {
                $button = $(".play-button");
                $button.removeClass("btn-primary");
                $button.addClass("btn-danger");
                $button.html("request failed");
            }
            $button.attr("disabled", true);
            $button.parents("tr").unbind("click");
        });
    }

    function addTableRowListener() {
        $("#search-results tbody tr").click(function () {
            $(".play-button").remove();
            var td = $(this).find(".buttonHolder"),
                userName = $(this).find(".userName").text();
            td.append('<button class="play-button btn btn-primary">Request Game</button>');

            $(".play-button").click(function (event) {
                event.preventDefault();
                requestGame(userName);
            });
        });
    }

    function searchForUserName() {
        var inputUserName = $('#userName'),
            userName = inputUserName.val(),
            formData = "searchTerm=" + userName;

        $.post('./searchForUsers', formData, function (data) {
            var users = $.parseJSON(data),
                i,
                userLength,
                $tbody = $("#search-box tbody"),
                tableResult = "";

            if (Array.isArray(users)) {
                if (users.length === 0) {
                    $tbody.html("<tr><td><h2>Search returned no results</h2></td></tr>");
                } else {
                    for (i = 0, userLength = users.length; i < userLength; i += 1) {
                        tableResult += "<tr><td class='userName'>" + users[i].userName + "</td><td class=\"buttonHolder\"><span class=\"block\">" + users[i].fullName + "</span></td></tr>";
                    }
                    $tbody.html(tableResult);
                    addTableRowListener();
                }
            }
        });
    }

    /*jslint unparam: true*/
    function init() {
        $("#search-btn").click(function (event) {
            searchForUserName();
        });

        $("#userName").keypress(function (event) {
            if (event.which === 13) {
                event.preventDefault();
                searchForUserName();
            }
        });
    }
    /*jslint unparam: false*/

    return {
        init : init,
        requestGame : requestGame,
        searchForUserName : searchForUserName,
        addTableRowListener : addTableRowListener
    };
};