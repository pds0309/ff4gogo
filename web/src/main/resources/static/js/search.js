"use strict"
$(document).ready(function () {

    //When page loads...
    $(".tab_content.outer").hide(); //Hide all content
    $("ul.tabs.outer li:first").addClass("active").show(); //Activate first tab
    $(".tab_content.outer:first").show(); //Show first tab content

    //On Click Event
    $("ul.tabs.outer li").click(function () {

        $("ul.tabs.outer li").removeClass("is-active"); //Remove any "active" class
        $(this).addClass("is-active"); //Add "active" class to selected tab
        $(".tab_content.outer").hide(); //Hide all tab content

        var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
        $(activeTab).fadeIn(); //Fade in the active ID content
        return false;
    });
});

function searchUsers() {
    var username = $('#id-txt-user').val();
    $.ajax({
        type: 'POST',
        url: '/users',
        contentType: "applicatiion/json",
        data: username,
        success: function (response) {
            location.href = `/users/${response['data'].userId}`;
        },
        error: function (response) {
            sendErrorAlert(response);
        }
    });
}

function sendErrorAlert(response) {
    let code = "[" + response.responseJSON.code + "]\n";
    let msg = response.responseJSON.message;
    window.alert(code + msg);
}