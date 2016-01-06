/*global document*/
/*global $*/
/*global alert*/
/*global showPanelMessage*/

/**
 * @fileoverview User related functions
 * @author Adrian McLaughlin
 */

/**
 * Global variable
 */
var validUsername = false;

/**
 * The function uses a ajax request to the server to check if the username is valid.
 * if false the function changes the userName input element's style and sets text
 * in the '#userNameMsg' element
 * 
 * @param event
 */
function isUsernameAvailable() {
    "use strict";
    var inputUserName = $('#userName'),
        userName = inputUserName.val(),
        formData = "userName=" + userName;

    $.post('./isUserNameAvailable', formData, function (data) {
        var userNameBox = $("#userName");
        if (data) {
            userNameBox.css("background-color", "#28794a");
        } else {
            userNameBox.css("background-color", "#dc4040");
        }
    });
}

/**
 * Compares the two passwords from two form input elements
 * @public
 * @param {string}
 *            inputElementOne html form input whose value contains a password
 * @param {string}
 *            inputElementTwo html form input whose value contains a password
 * @return {boolean} true if the passwords are the same
 */
function checkPassword(inputElementOne, inputElementTwo) {
    "use strict";
    var password1 = document.getElementById(inputElementOne),
        password2 = document.getElementById(inputElementTwo);

    return password1.value === password2.value;
}


/**
 * Checks that the requirements of submission have been met
 * before submitting the form.
 * Requirements:
 * 1. The passwords supplied by the user are the same
 * 2. The username is a valid username.
 * 
 * @param event
 * @returns {Boolean}
 */
function canSubmit() {
    "use strict";
    if (!checkPassword('password', 'passwordTwo')) {
        return false;
    }

    if (!validUsername) {
        showPanelMessage("#alert", "Username is not valid.", true);
        return false;
    }

    return true;

}

$(document).ready(function () {
    "use strict";
    $('#userName').keyup(isUsernameAvailable);
    $('#userForm').submit(canSubmit);
});