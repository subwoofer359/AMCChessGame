/**
 * Highlights a selected row in a table and checks a checkbox and unchecks the
 * other checkboxes in the table
 *
 * @param {Object} element DOM Element
 */
$(document).ready(function () {
    "use strict";
    $(".games-table tbody tr").click(function () {
        $(".games-table tbody tr").each(function () {
            $(this).removeClass("selected");
            $(this).find(":checkbox").prop("checked",false);
        });
        $(this).addClass("selected");
        $(this).find(":checkbox").prop("checked",true);
    });
});