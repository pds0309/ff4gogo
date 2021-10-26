"use strict"
$(document).ready(function () {
    $("#id-txt-user").on("keyup",function(key){
        if(key.keyCode===13) {
            searchUsers();
        }
    });

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
    var name = $('#id-txt-user').val();
    var username = name.replace(/[^a-zA-z0-9ㄱ-힣]/g, '');
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

function getPlayerInfo(playerid) {
    return $.ajax({
        type: 'GET',
        url: `/players/info?pid=${playerid}`
    });
}

var setCookie = function (name, value, path) {
    document.cookie = name + '=' + value + ';path=' + path;
};
var getCookie = function (name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value ? value[2] : null;
};

function isMobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}
