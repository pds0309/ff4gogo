"use strict"
$(document).ready(function () {
    let sqdId='#id-index-bestplayer';
    if($(sqdId).text().length === 0){
        getPosBestPlayers($('#id-index-cnt').text() , $('#id-index-season').text().split('-')[0])
            .done(function(result){
                $(sqdId).append(bestSquadHtml());
                putBestSquad(result.data);
                mapImages();
            });
    }
    function getPosBestPlayers(c,s){
        let cc = parseInt(c*0.8);
        return $.ajax({
            type: 'GET',
            url: `/stats/bestpos?cnt=${cc}&season=${s}`,
            error: function(){
                $(`#id-index-bestplayer`).text(`스쿼드 정보를 불러올 수 없습니다.`);
            }
        });
    }

    function mapImages(){
        let list = Array.prototype.slice.call(document.querySelectorAll(`.mainimg- `));
        list.map(value => getImage(value.className.split("- ")[1]).done(function(res){value.src =  res.data}));
    }
    function getImage(pid){
        return $.ajax({
            type: 'GET',
            url: `/players/image?pid=${pid}`,
        });
    }

    function bestSquadHtml() {
        return `
<div class="column is-mobile" style="background: url(/img/soccerfield.png) no-repeat 0 0;background-size:100% 100%;">
<div class="columns is-mobile">
        <div id="sqd-LW" class="column is-one-third has-text-centered"></div>
        <div id="sqd-ST" class="column is-one-third has-text-centered"></div>
        <div id="sqd-RW" class="column is-one-third has-text-centered"></div>
</div>
<div class="columns is-mobile">
    <div class="column is-one-quarter">&nbsp;</div>                                        
    <div id="sqd-CF" class="column is-one-quarter"></div>                                        
    <div id="sqd-CAM" class="column is-one-quarter"></div>                                        
    <div class="column is-one-quarter">&nbsp;</div>                                        
</div>
<div class="columns is-mobile">
        <div id="sqd-LM" class="column is-one-third has-text-centered"></div>
        <div id="sqd-CM" class="column is-one-third has-text-centered"></div>
        <div id="sqd-RM" class="column is-one-third has-text-centered"></div>
</div>
<div class="columns is-mobile">
        <div id="sqd-LWB" class="column is-one-third has-text-centered"></div>
        <div id="sqd-CDM" class="column is-one-third has-text-centered"></div>
        <div id="sqd-RWB" class="column is-one-third has-text-centered"></div>
</div>
<div class="columns is-mobile">
    <div id="sqd-LB" class="column is-one-quarter has-text-centered"></div>                                        
    <div id="sqd-SW" class="column is-one-quarter has-text-centered"></div>
    <div id="sqd-CB" class="column is-one-quarter has-text-centered"></div>                                        
    <div id="sqd-RB" class="column is-one-quarter has-text-centered"></div>                                        
</div>
<div class="columns is-mobile">
    <div class="column is-one-third has-text-centered">&nbsp;</div>
    <div id="sqd-GK" class="column is-one-third has-text-centered"></div>
    <div class="column is-one-third has-text-centered">&nbsp;</div>
</div>                      
</div>`
    }
    function putBestSquad(statList){
        statList.map(v=>
            $(`#sqd-${v.mostPos}`).append(`
            <p><small style="color: ${(v.star >= 8) ? "skyblue": "white"};background-color: black;border-radius: 100%">${v.star.toFixed(1)}</small></p>
            <figure class="image is-96x96 resp is-inline-block mt-1 is-relative">
                        <img class="mainimg- ${v.statId.player.playerId}" src="" alt="player">
                         <img class="image" style="position: absolute;bottom: 0;left: 0;width: 30%;height: 30%" src="${v.statId.player.season.img}" alt="season">    
                    </figure>
                    <p class="has-text-white"><small class="p-${v.mostPos}">${v.mostPos}</small>
                    <small>${(v.statId.player.playerName.indexOf('. ')===-1)?v.statId.player.playerName:v.statId.player.playerName.split('. ')[1]}</small>
                    </p>
            `)
        );
    }
});

