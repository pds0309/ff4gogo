"use strict"
$(document).ready(function () {
    let list = Array.prototype.slice.call(document.querySelectorAll(`.mainimg- `));
    list.map(value => getImage(value.className.split("- ")[1]).done(function(res){value.src =  res.data}));
});
function getImage(pid){
    return $.ajax({
        type: 'GET',
        url: `/players/image?pid=${pid}`,
    });
}
