"use strict";

function searchUsers() {
    var e = $("#id-txt-user").val().replace(/[^a-zA-z0-9ㄱ-힣]/g, "");
    e=e.replace(/[^a-zA-z0-9ㄱ-힣]/g,'');
    $.ajax({
        type: "POST", url: "/users", contentType: "applicatiion/json", data: e, success: function (e) {
            location.href = `/users/${e.data.userId}`
        }, error: function (e) {
            sendErrorAlert(e)
        }
    })
}
function sendErrorAlert(e) {
    let t = "[" + e.responseJSON.code + "]\n", r = e.responseJSON.message;
    window.alert(t + r)
}

function isMobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)
}

$(document).ready(function () {
    $('#id-btn-user').on("click",function(){
       searchUsers();
    });
    $("#id-txt-user").on("keyup", function (e) {
        13 === e.keyCode && searchUsers()
    }), $(".tab_content.outer").hide(), $("ul.tabs.outer li:first").addClass("active").show(), $(".tab_content.outer:first").show(), $("ul.tabs.outer li").click(function () {
        $("ul.tabs.outer li").removeClass("is-active"), $(this).addClass("is-active"), $(".tab_content.outer").hide();
        var e = $(this).find("a").attr("href");
        return $(e).fadeIn(), !1
    })
});