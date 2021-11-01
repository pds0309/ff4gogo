"use strict"
$(document).ready(function () {

    const userid = $('#userid').text();
    if (localStorage.getItem(userid) != null) {
        let los = JSON.parse(localStorage.getItem(userid));
        for (let k = 0; k < los.codes.length; k++) {
            getViewMatches(los.detail[k], k);
        }
        let gangsined = los.timestamp;
        postGangsin(gangsined);
        postDerBogi(los.allcode.length, los.codes.length);
        los = JSON.parse(localStorage.getItem(userid));
        getSummaryInfo(userid, los.detail);
        return;
    }
    getMatchCodeList(userid)
        .done(function (res1) {
            let matchList = res1["data"];
            let getMatch = matchList.slice(0, 10);
            let md = [];
            let mc = [];

            function asyncMatches(param, k) {
                return new Promise((resolve, reject) => {
                    setTimeout(() => {
                        getMatchDetailList(userid, param)
                            .done(function (result, textStatus, jqXHR) {
                                if (jqXHR.status !== 204) {
                                    resolve(md[k] = result.data);
                                    resolve(mc[k] = matchList[k]);
                                } else {
                                    resolve(null);
                                    matchList[k] = null;
                                    $(`#id-finderror`).append(`<p>비정상적인 순위경기 제외됨</p>`);
                                }
                            });
                    }, 3);
                });
            }

            async function parallel(array) {
                const promises = array.map((param, t) => asyncMatches(param, t));
                return await Promise.all(promises);
            }

            parallel(getMatch).then(val => {
                let value = val.filter(v => v !== null);
                let matchCodes = mc.filter(v => v !== null);
                let ml = matchList.filter(v => v !== null);
                for (let k = 0; k < value.length; k++) {
                    getViewMatches(value[k], k);
                }
                let matches = {
                    detail: value,
                    codes: matchCodes,
                    timestamp: new Date().getTime(),
                    allcode: ml
                }
                deleteFromLocal();
                localStorage.setItem(userid, JSON.stringify(matches));
                postDerBogi(ml.length, matchCodes.length);
                getSummaryInfo(userid, JSON.parse(localStorage.getItem(userid)).detail);
            });
        })
});

function getViewMatches(dto, k) {
    let basic = dto["basicDtoList"];
    let owngoals = [dto["summaryDtoList"][0].ownGoal, dto["summaryDtoList"][1].ownGoal];
    getHTMLMatches(basic, owngoals, k);
    if (k === 0 && document.cookie.match("playerId") === null) {
        addPlayerCooc(dto.matchPlayerDtoList[0]);
    }

    $(`#id-match-btn-${k}`).click(function () {
        if (dto["matchPlayerDtoList"][0].length === 0 || dto["matchPlayerDtoList"][1].length === 0) {
            $(`#id-match-detail-${k}`).append(`<p class="has-text-danger is-size-5-desktop is-size-7-mobile">조기 몰수 처리로 해당 경기의 데이터가 불온전하여 조회하실 수 없습니다.</p>`);
            $(`#id-match-btn-${k}`).attr('disabled', true);
        }
        clickDetail(k, dto);
    });
}

function clickDetail(k, dto) {
    let yourname = $(`#uname-${k}-1`).text();
    let myname = $(`#uname-${k}-0`).text();
    let detailDiv = document.getElementById(`id-match-detail-${k}`);
    if (detailDiv.style.display === "none") {
        detailDiv.style.display = "block";
        if ($(`#id-match-detail-${k}`).text().length === 0) {
            $(`#id-match-detail-${k}`).append(`<div class="columns is-mobile has-background-info-light">
                            <div id="match-goals-${k}-0" class="column">
                            </div>
                            <div id="match-goals-${k}-1"  class="column">
                            </div>
                        </div>
                        <div class="tabs is-centered is-toggle is-fullwidth is-justify-content-center">
                            <ul class=" p-0 has-text-weight-bold column tabs inner ${k}">
                                <li class="is-active"><a href="#squad-${k}">Squad</a></li>
                                <li><a href="#match-${k}">경기</a></li>
                                <li><a href="#player-${k}">선수</a></li>
                                <li id="id-shoot-${k}-li" class="ele-no-react"><a id="id-shoot-${k}" href="#shoot-${k}">슛</a></li>
                            </ul>
                        </div>
                        <div>
                            <div id="squad-${k}" class="container tab_content inner ${k}">
<div class="field has-text-centered">
  <span class="has-text-weight-bold">${yourname}&nbsp;</span>
  <input id="switch-squad-${k}" type="checkbox" name="switch-squad-${k}" class="switch is-success" checked="checked">
  <label class="has-text-weight-bold" for="switch-squad-${k}">&nbsp;</label><span class="has-text-weight-bold">&nbsp;${myname}</span>
</div>
    <div id="squad-all-${k}-true">${squadHtml(k, 0)}</div>
    <div id="squad-all-${k}-false" style="display: none">${squadHtml(k, 1)}</div>
</div>
                            </div>
                            <div id="match-${k}" class="container tab_content inner ${k}">
<div class="columns">
<div class="column is-one-third has-text-centered is-justify-content-center">
    <div id="poss-chart-${k}" style="width: fit-content; display: inline-block"></div>
</div>
<hr>
<div id="pass-chart-${k}" class="column is-two-thirds p-0"></div>
</div>
<hr>
<div class="has-text-centered is-fullwidth p-0">
    <div id="hobar-${k}-0"></div>
</div>                               
                            </div>
                            <div id="player-${k}" class="container tab_content inner ${k}">
<div class="field has-text-centered">
    <span class="has-text-weight-bold">${yourname}&nbsp;</span>
    <input id="switch-player-${k}" type="checkbox" name="switch-player-${k}" class="switch is-danger" checked="checked">
    <label class="has-text-weight-bold" for="switch-player-${k}">&nbsp;</label><span class="has-text-weight-bold">&nbsp;${myname}</span>
</div>
<div id="player-all-${k}-true" style="width: 100%;overflow: auto;">
    ${playerTableHTML(k, 0)}
</div>
<div id="player-all-${k}-false" style="width: 100%;overflow: auto; display: none">
    ${playerTableHTML(k, 1)}
</div>
                            </div>
                            <div id="shoot-${k}" class="container tab_content inner ${k}"></div>
                            </div>
                        </div>
                    </div>`);
            $(`#switch-squad-${k}`).click(function () {
                let status = $(`#switch-squad-${k}`).is(':checked');
                $(`#squad-all-${k}-${!status}`).css('display', 'none');
                $(`#squad-all-${k}-${status}`).css('display', 'block');
            });
            $(`#switch-player-${k}`).click(function () {
                let status = $(`#switch-player-${k}`).is(':checked');
                $(`#player-all-${k}-${!status}`).css('display', 'none');
                $(`#player-all-${k}-${status}`).css('display', 'block');
            });
            let myShoots = sortDto(dto.shootDtoList[0], "goalTime");
            let yourShoots = sortDto(dto.shootDtoList[1], "goalTime");
            let shootDto = [myShoots, yourShoots];
            getXg(myShoots);
            getXg(yourShoots);
            setTimeout( function () {
                $(`#id-shoot-${k}-li`).removeClass('ele-no-react');
            }, 1800);
            $(`#id-shoot-${k}`).click(function () {
                if ($(`#shoot-${k}`).text().length === 0) {
                    $(`#shoot-${k}`).append(`
                        <div class="has-text-black is-size-3-desktop is-size-5-mobile has-text-centered has-text-weight-bold">
                            <p>슈팅분포</p>
                            <hr>
                                <p id="shoot-all-warn-${k}" class="is-size-7-mobile has-text-danger"></p>
                        </div>
                    <div id="shoot-all-${k}" class="has-text-centered is-fullwidth p-0 shootscatter">
                    </div>
                    <hr>
                        <div class="has-text-black is-size-3-desktop is-size-5-mobile has-text-centered has-text-weight-bold">
                            <p>경기기대득점[XG] 추이</p>
                            <hr>
                                <p id="xgchart-${k}" class="is-size-5-desktop is-size-7-mobile"></p>
                        </div>`
                    );
                    setTimeout( function () {
                        getShootChart(k, shootDto, myname, yourname);
                        getXgChart(k, shootDto, myname, yourname);
                    }, 200);
                }
            });
            getOneMatchInfo(dto, k);
            let mySummary = dto.summaryDtoList[0];
            let yourSummary = dto.summaryDtoList[1];
            addPlayerTableData(k, 0, sortDto(dto.matchPlayerDtoList[0], "spPosition"));
            addPlayerTableData(k, 1, sortDto(dto.matchPlayerDtoList[1], "spPosition"));
            let sumarryKeys = ['shootTotal', 'effectiveShootTotal', 'cornerKick', 'shootFreekick', 'offsideCount'
                , 'foul', 'ownGoal', 'yellowCards', 'redCards'];
            getMatchHorBar(k, 0, dto.summaryDtoList, sumarryKeys, myname, yourname);
            getPossChart(k, mySummary["possession"], yourSummary["possession"], myname, yourname);
            getPassChart(k, myname, dto.passDtoList[0]);
            let pIdSet = new Set();
            dto.matchPlayerDtoList.map(value => value.forEach(function (item) {
                let cook = getCookie(item.spId);
                if (cook === null) {
                    pIdSet.add(item.spId);
                } else {
                    let player = JSON.parse(cook);
                    addClasForPlayer(player);
                }
            }));
            pIdSet.forEach(value =>
                getPlayerInfo(value)
                    .done(function (result) {
                        let player = result.data;
                        addClasForPlayer(player);
                    })
            )
            $(`.tab_content.inner.${k}`).hide(); //Hide all content
            $(`#squad-${k}`).addClass("active").show(); //Activate first tab
            $(`#squad-${k}`).show(); //Show first tab content
            $(`ul.tabs.inner.${k} li`).click(function () {
                $(`ul.tabs.inner.${k} li`).removeClass("is-active"); //Remove any "active" class
                $(this).addClass("is-active"); //Add "active" class to selected tab
                $(`.tab_content.inner.${k}`).hide(); //Hide all tab content
                var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
                $(activeTab).fadeIn(); //Fade in the active ID content
                return false;
            });
        }
    } else {
        detailDiv.style.display = "none";
    }
}

function getXg(shootdtoList) {
    for (let i = 0; i < shootdtoList.length; i++) {
        let shotType = [0, 0, 0, 0];
        shotType[shootdtoList[i]["type"]] = 1;
        let shootInfo = {
            assist: (shootdtoList[i]["assist"]) ? 1 : 0,
            asx: shootdtoList[i]["assistX"],
            asy: shootdtoList[i]["assistY"],
            inv: (i === 0) ? shootdtoList[i]["goalTime"] : shootdtoList[i]["goalTime"] - shootdtoList[i - 1]["goalTime"],
            x: shootdtoList[i]["x"],
            y: shootdtoList[i]["y"],
            nom: shotType[1],
            fin: shotType[2],
            hed: shotType[3]
        }
        $.ajax({
            type: 'POST',
            url: '/expectedgoal',
            contentType: 'application/json',
            data: JSON.stringify(shootInfo),
            success: function (response) {
                shootdtoList[i].prediction = response["prediction"];
            },
            error: function (response) {
                shootdtoList[i].prediction = 0;
            }
        });
    }
}


function addClasForPlayer(player) {
    let pId = player.info.playerId;
    let pName = player.info.playerName;
    Array.prototype.slice.call(document.querySelectorAll(`.get-${pId}-name`))
        .map(v => v.textContent = (pName.length < 8) ? pName : pName.split(' ')[1]);
    Array.prototype.slice.call(document.querySelectorAll(`.get-${pId}-img`))
        .map(v => v.src = player.pimg);
    Array.prototype.slice.call(document.querySelectorAll(`.get-${pId}-simg`))
        .map(v => v.src = player.info.season.img);
}

function getOneMatchInfo(dto, k) {

    let myshoot = sortDto(dto.shootDtoList[0], "goalTime");
    let yourshoot = sortDto(dto.shootDtoList[1], "goalTime");
    putGoalInfo(myshoot, k, 0);
    putGoalInfo(yourshoot, k, 1);
    putSquad(dto.matchPlayerDtoList[0], k, 0);
    putSquad(dto.matchPlayerDtoList[1], k, 1);
}

function putGoalInfo(shootDto, k, idx) {
    for (let obj of shootDto) {
        if (obj.result === 3) {
            let gt = obj.goalTime;
            gt = (gt >= 0) ? gt : '45+' + (-gt - 45);
            $(`#match-goals-${k}-${idx}`).append(`
            <small><i class="fa fa-futbol"></i>&nbsp;${gt}'<span class="get-${obj.spId}-name"></span></small><br>`);
        }
    }
}

function putSquad(playerDto, k, idx) {
    for (let obj of playerDto) {
        if (obj.spPosition !== 28) {
            $(`#sqd-${k}-${idx}-${obj.spPosition}`).append(`
            <figure class="image is-96x96 resp is-inline-block mt-1 is-relative">
                        <img class="image pos-taeduri-${obj.rootPosName} get-${obj.spId}-img"  src="" alt="player">
                         <img class="image get-${obj.spId}-simg" style="position: absolute;bottom: 0;left: 0;width: 25%;height: 25%" src="" alt="season">
                             <img class="image" style="position: absolute;bottom: 0;right: 0;width: 25%;height: 25%" src="/img/e${obj.spGrade}.PNG">
                    </figure>
                    <p class="myp has-text-white" style="margin-top: -8px;margin-bottom: -8px;"><small class="get-${obj.spId}-name"></small></p>
                    <p class="myp sub pos-color-${obj.rootPosName}"><small>${obj.posName}</small></p>
            `);
        } else {
            $(`#sqd-${k}-${idx}-28`)
                .append(`<div class="column mywidth-16">
<figure class="image is-96x96 resp is-inline-block mt-1 is-relative">
                        <img class="image hovsub pos-taeduri-${obj.rootPosName} get-${obj.spId}-img im-${k}-${idx}"  src="" alt="player">
                         <img class="image get-${obj.spId}-simg" style="position: absolute;bottom: 0;left: 0;width: 29%;height: 29%" src="" alt="season">
                             <img class="image" style="position: absolute;bottom: 0;right: 0;width: 29%;height: 29%" src="/img/e${obj.spGrade}.PNG">
                    </figure>
                    <p class="myp has-text-black verysmall" style="margin-top: -8px;margin-bottom: -8px;"><small id="subs-${k}-${idx}-${obj.spId}" class="get-${obj.spId}-name"></small></p>                 
                         </div>                                        
`);
            $(`.image.hovsub.get-${obj.spId}-img.im-${k}-${idx}`).hover(function () {
                $(`#mobilehovertext-name-${k}-${idx}`).text($(`#subs-${k}-${idx}-${obj.spId}`).text());
            });
        }
    }
}


function getMatchCodeList(userid) {
    return $.ajax({
        type: 'POST',
        url: `/users/${userid}/matches`,
        contentType: "applicatiion/json",
        error: function (response) {
            $(`#id-section-nodata`).show();
            $(`#id-all-match`).hide();
            $(`#id-all-match-list`).hide();
            $(`#id-finderror`).append("[" + response.responseJSON.code + "] " + response.responseJSON.message);
        }
    });
}

function getMatchDetailList(userid, usermatchCode) {
    return $.ajax({
        type: 'PUT',
        url: `/users/${userid}/matches`,
        contentType: "application/json",
        data: usermatchCode
    });
}

function deleteFromLocal() {
    if (localStorage.length >= 7) {
        let min = 9999999999999;
        let id = "1";
        for (let k = 0; k < localStorage.length; k++) {
            let key = localStorage.key(k);
            let interval = JSON.parse(localStorage.getItem(key)).timestamp;
            if (interval < min) {
                min = interval;
                id = key;
            }
        }
        localStorage.removeItem(id);
    }
}


function getMoreMatch(id) {
    let index = $('.clas').length;
    let matchCodeList = JSON.parse(localStorage.getItem(id)).allcode;
    let newMatchList = matchCodeList.slice(index, index + 5);

    function asyncMoreMatches(param, k) {
        return new Promise((resolve) => {
            setTimeout(() => {
                getMatchDetailList(id, newMatchList[k])
                    .done(function (result, textStatus, jqXHR) {
                        if (jqXHR.status !== 204) {
                            resolve(result.data);
                        } else {
                            resolve(null);
                            $(`#id-finderror`).append(`<p class="clas">비정상적인 순위경기 제외됨</p>`);
                        }
                    });
            }, 3);
        });
    }

    async function paral(matches) {
        const result = matches.map((param, k) => asyncMoreMatches(param, k));
        return await Promise.all(result);
    }

    paral(newMatchList).then(value => {
        let val = value.filter(v => v !== null);
        for (let k = 0; k < val.length; k++) {
            getViewMatches(val[k], index + k);
        }
    });
    if (matchCodeList.length <= index + 5) {
        $('#id-derbogi').hide();
    }
}


function postGangsin(timestamp) {
    let now = new Date().getTime();
    let time = ((now - timestamp) / (1000 * 60));
    if (time <= 120) {
        $('#id-text-gangsintime').text(Math.round(time) + "분 전 갱신됨");
    } else {
        $('#id-text-gangsintime').text(Math.round(time / 60) + "시간 전 갱신됨");
        $('#id-btn-gangsin').removeAttr("disabled");
    }
}

function getGangsin(id) {
    localStorage.removeItem(id);
    window.location.reload();
}

function postDerBogi(len, cur) {
    if (len > cur) {
        $('#id-derbogi').show();
    }
}

function getSummaryInfo(userid, dtos) {
    let goal = [0, 0];
    let wdl = [0, 0, 0];
    let shoots1 = [0, 0];
    let shoots2 = [0, 0];
    let poss = 0;
    let idx = 0;
    let myplayers = [];
    let mypassDto = [];
    let yourpassDto = [];
    let dribbles = [0, 0];
    let goalTimes1 = [[], []];
    for (let dto of dtos) {
        let result = dto.basicDtoList[0].matchResult;
        if (result === "승") {
            wdl[0]++;
        } else if (result === "무") {
            wdl[1]++;
        } else {
            wdl[2]++;
        }
        if (dto.matchPlayerDtoList[0].length === 0 || dto.matchPlayerDtoList[1].length === 0) {
            continue;
        }
        idx += 1;
        let matchPlayers = dto.matchPlayerDtoList[0];
        for (let j = 0; j < matchPlayers.length; j++) {
            if (matchPlayers[j].spPosition !== 28) {
                myplayers.push(matchPlayers[j]);
                dribbles[0] += matchPlayers[j].dribbleTry;
                dribbles[1] += matchPlayers[j].dribbleSuccess;
            }
        }
        let summary = dto.summaryDtoList;
        poss += summary[0].possession;
        shoots1[0] += summary[0].shootTotal;
        shoots1[1] += summary[0].effectiveShootTotal;
        shoots2[0] += summary[1].shootTotal;
        shoots2[1] += summary[1].effectiveShootTotal;
        mypassDto.push(dto.passDtoList[0]);
        yourpassDto.push(dto.passDtoList[1]);
        goal[0] += dto.basicDtoList[0].goalTotal;
        goal[1] += dto.basicDtoList[1].goalTotal;
        let myShoots = dto.shootDtoList[0].filter(g => g.result === 3);
        let yourShoots = dto.shootDtoList[1].filter(g => g.result === 3);
        goalTimes1[0] = goalTimes1[0].concat(myShoots.map(value => value.goalTime));
        goalTimes1[1] = goalTimes1[1].concat(yourShoots.map(value => value.goalTime));
    }
    getGoalBar(goal, dtos.length);
    getWdlDonut(wdl, dtos.length);
    getGoalTimeBar(goalTimes1[0].sort(), goalTimes1[1].sort());
    getMvps(myplayers, userid);
    if (idx === 0) {
        return;
    }
    let passKeys = Object.keys(mypassDto[0]);
    let myPass = dtoReducer(passKeys, mypassDto);
    let yourPass = dtoReducer(passKeys, yourpassDto);
    getAnalSummary(poss, shoots1, shoots2, idx, goal, dribbles);
    getAnalPass(myPass, idx, [0, 1, 2], "성공");
    getAnalPass(yourPass, idx, [2, 1, 0], "허용");
}

function getAnalPass(passList, idx, arr, msg) {
    let allPass = passList["passTry"];
    //glass-martini-alt
    if (msg === "성공") {
        let totPassSuc = Math.round(passList["passSuccess"] / allPass * 100);
        $(`#anal-${(totPassSuc >= 92) ? arr[2] : (totPassSuc <= 82) ? arr[0] : arr[1]}`)
            .append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-shoe-prints"></i>&nbsp;[패스${msg}]</p>
<p class="has-text-white">${idx}경기에서 ${empText(totPassSuc + "%")}의 패스 ${msg}률을 보였습니다.</p><br>`);
    }
    let notShort = allPass - passList["shortPassTry"];
    let notShortSuccess = passList["passSuccess"] - passList["shortPassSuccess"];
    if (notShort / allPass >= 0.15 && notShortSuccess / notShort >= 0.8) {
        $(`#anal-${arr[2]}`)
            .append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-ruler-horizontal"></i>&nbsp;[고급패스${msg}]</p>
<p class="has-text-white">전체패스의 ${Math.round(notShort / allPass * 100)}%가 고급패스로 ${empText(Math.round(notShortSuccess / notShort * 100) + "%")}의 고급 패스를 ${msg}했습니다.</p><br>`);
    }

    let throughSuccess = Math.round(passList["throughPassSuccess"] / passList["throughPassTry"] * 100);
    $(`#anal-${(throughSuccess >= 90) ? arr[2] : (throughSuccess < 60) ? arr[0] : arr[1]}`)
        .append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-divide"></i>&nbsp;[스루패스${msg}]</p>
<p class="has-text-white">${passList["throughPassTry"]}번의 스루패스 중 ${empText(throughSuccess + "%")}를 ${msg}했습니다.</p><br>`);

}

function getAnalSummary(poss, shoots1, shoots2, idx, goal, dribbles) {
    let myPos = Math.round(poss / idx);
    $(`#anal-${(myPos < 40) ? 0 : (myPos > 60) ? 2 : 1}`)
        .append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-user-clock"></i>&nbsp;[점유율]</p>
<p class="has-text-white">경기 평균 ${empText(myPos + '%')} 의 점유율을 보였습니다.</p><br>`);

    let goalEff = parseInt(((goal[0] / shoots1[0]) * 100) / 33);
    $(`#anal-${Math.min(goalEff, 2)}`)
        .append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-futbol"></i>&nbsp;[득점효율]</p>
<p class="has-text-white">${empText("[ " + shoots1[1] + " / " + shoots1[0] + " ]")}개의 슈팅 중 ${empText(goal[0])} 득점을 만들었습니다.</p><br>`);

    let shootEff = parseInt(((shoots1[1] / shoots1[0]) * 100) / 33);
    $(`#anal-${Math.min(shootEff, 2)}`)
        .append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-exchange-alt"></i>&nbsp;[유효슈팅]</p>
<p class="has-text-white">${Math.round((shoots1[1] / shoots1[0]) * 100)}%의 슈팅이 골대를 향했습니다.</p><br>`);

    let yourShoot = (shoots2[1] / idx);
    $(`#anal-${(yourShoot >= 4) ? 0 : (yourShoot <= 2) ? 2 : 1}`)
        .append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-exclamation"></i>&nbsp;[슈팅허용]</p>
<p class="has-text-white">경기 평균 ${empText(Math.round(yourShoot * 10) / 10)}개의 유효슈팅을 허용했습니다.</p><br>`);

    let drbSucc = Math.round(dribbles[1] / dribbles[0] * 100);
    if (drbSucc >= 90) {
        $(`#anal-2`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-lightbulb"></i>&nbsp;[환상적인 발놀림]</p>
<p class="has-text-white">${drbSucc}%의 성공률을 자랑하는 환상적인 드리블러입니다.</p><br>`);
    } else if (drbSucc < 35) {
        $(`#anal-0`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-lightbulb"></i>&nbsp;[쓸데없는 발재간]</p>
<p class="has-text-white">${100 - drbSucc}%의 드리블 실패로 흐름을 끊는 플레이를 합니다.</p><br>`);
    }
}

function getMvps(players, userid) {
    return $.ajax({
        type: 'POST',
        url: `/users/${userid}/matches/mvp`,
        contentType: 'application/json',
        data: JSON.stringify(players),
        success: function (response) {
            let bestdto = response.data;
            yourGoalGetter(bestdto);
            yourAssister(bestdto);
            yourStar(bestdto);
        },
        error: function (response) {
            $('#mvps').hide();
            $('#id-tab-mvps')
                .append(`<p class="container has-text-white has-text-centered has-text-danger is-size-7">
[${response.responseJSON.code}] ${response.responseJSON.message}<p>최근순위경기에 몰수경기만 있었나요?</p></p>`)
        }
    });
}

function yourGoalGetter(bestdto) {
    const max = bestdto.reduce(function (prev, current) {
        return (prev.goal > current.goal) ? prev : current
    });
    getPlayerInfo(max.spId)
        .done(function (result) {
            let player = result.data;
            $('#mvp-goalgetter').append(`${getHTMLMvps(player.info.season.img, player.pimg, player.info.playerName)}<p><span class="has-text-primary has-text-weight-bold">${max.cnt}경기 ${max.goal} 득점</p>`);
        });

}

function yourAssister(bestdto) {
    const max = bestdto.reduce(function (prev, current) {
        return (prev.assist > current.assist) ? prev : current
    });
    getPlayerInfo(max.spId)
        .done(function (result) {
            let player = result.data;
            $('#mvp-assister').append(`${getHTMLMvps(player.info.season.img, player.pimg, player.info.playerName)}<p><span class="has-text-link has-text-weight-bold">${max.cnt}경기 ${max.assist} 도움</p>`);
        });
}

function yourStar(bestdto) {
    const max = bestdto.reduce(function (prev, current) {
        return (prev.spRating > current.spRating) ? prev : current
    });
    getPlayerInfo(max.spId)
        .done(function (result) {
            let player = result.data;
            $('#mvp-star').append(`${getHTMLMvps(player.info.season.img, player.pimg, player.info.playerName)}<p><span class="has-text-danger has-text-weight-bold">${max.cnt}경기 ${Math.round(max.spRating * 100) / 100} 평점</p>`);
        });
}

function getHTMLMvps(simg, pimg, pname) {
    return `<figure class="card-image is-128x128">
                                        <div style="width: fit-content; display: inline-block">
                                            <img class="is-rounded" src="${pimg}">
                                            <img style="position: absolute;top: 79%" class="image"
                                                 src="${simg}">
                                        </div>
                                    </figure>
                                    <p class="is-size-6 has-text-weight-bold">${pname}</p>`;
}

function getHTMLMatches(basic, owngoals, k) {
    let mybasic = basic[0];
    let yourbasic = basic[1];
    let endtype = ["", "몰수", "몰수"];
    let resultColor = mybasic.matchResult === "승" ? "#add8e6" : (mybasic.matchResult === "무") ? "#d3d3d3" : "#fcadc8";
    let controllers = (mybasic.controller === "etc") ? "question" : mybasic.controller;
    let htmls = `<div id="id-match-${k}" class="has-text-centered has-text-white clas">
                <div class="card">
                    <header class="card-header" style="background-color: ${resultColor}">&nbsp;&nbsp;&nbsp;
                        <p class="card-header-title is-size-4-desktop is-size-6-mobile">
                            ${endtype[mybasic.matchEndType]}${mybasic.matchResult}
                        </p>
                        <span class="card-header-icon has-text-right is-size-5-desktop is-size-7-mobile">${mybasic.matchDate}</span>
                        <button id="id-match-btn-${k}" class="card-header-icon hovable" aria-label="more options">
                            <span class="icon">
                            <i class="fa fa-angle-down" aria-hidden="true"></i>
                        </span>
                        </button>
                    </header>
                    <div class="card-content">
                        <div class="content has-text-weight-bold is-size-4-desktop is-size-7-mobile">
                            <span>
                                <i class="fa fa-${controllers}"></i>
                            &nbsp;&nbsp;<span id="uname-${k}-0">${mybasic.nickname}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${mybasic.goalTotal + owngoals[1]} : ${yourbasic.goalTotal + owngoals[0]}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="uname-${k}-1">${yourbasic.nickname}</span>&nbsp;&nbsp;
                                <i class="fa fa-${controllers}"></i>
                            </span>
                        </div>
                    </div>
                    <div id="id-match-detail-${k}" class="card-content pl-3 pr-3" style="display: none"></div>
                </div><br><br>
            </div>`;
    $(`#id-container-matchlist`).append(htmls);
}


function getAnalGita() {
    $('#anal-1').toggle();
}

function empText(val) {
    return `<span class="has-text-weight-bold">${val}</span>`;
}

function dtoReducer(keys, obj) {
    return obj.reduce(function (pre, cur) {
        for (let i of keys) {
            pre[i] += cur[i];
        }
        return pre;
    });
}

function sortDto(dto, key) {
    return dto.sort(function (o1, o2) {
        return Math.abs(o1[key]) - Math.abs(o2[key])
    });
}

function addPlayerCooc(playerDto) {
    console.log("과자먹을래?");
    playerDto.map(value => getPlayerInfo(value.spId)
        .done(function (result) {
            setCookie(value.spId, JSON.stringify(result.data), window.location.pathname);
        }));
}

function squadHtml(k, idx) {
    return `
<div class="column is-mobile" style="background: url(/img/soccerfield.png) no-repeat 0 0;background-size:100% 100%;">
<div class="columns is-mobile">
        <div class="column is-one-fifth has-text-centered">&nbsp;</div>
        <div id="sqd-${k}-${idx}-26" class="column is-one-fifth has-text-centered"></div>
        <div id="sqd-${k}-${idx}-25" class="column is-one-fifth has-text-centered"></div>
        <div id="sqd-${k}-${idx}-24" class="column is-one-fifth has-text-centered"></div>
        <div class="column is-one-fifth has-text-centered">&nbsp;</div>
</div>
<div class="columns is-mobile">
    <div id="sqd-${k}-${idx}-27" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-22" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-21" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-20" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-23" class="column is-one-fifth "></div>                                        
</div>
<div class="columns is-mobile">
    <div class="column is-one-fifth"></div>                                        
    <div id="sqd-${k}-${idx}-19" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-18" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-17" class="column is-one-fifth "></div>                                        
    <div class="column is-one-fifth "></div>                                     
</div>
<div class="columns is-mobile">
    <div id="sqd-${k}-${idx}-16" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-15" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-14" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-13" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-12" class="column is-one-fifth "></div>
    </div>
<div class="columns is-mobile">
    <div id="sqd-${k}-${idx}-8" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-11" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-10" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-9" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-2" class="column is-one-fifth "></div>                                        
</div>
<div class="columns is-mobile">
    <div id="sqd-${k}-${idx}-7" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-6" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-5" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-4" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-3" class="column is-one-fifth "></div>                                        
</div>
<div class="columns is-mobile">
    <div class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-1" class="column is-one-fifth "></div>                                        
    <div id="sqd-${k}-${idx}-0" class="column is-one-fifth "></div>                                        
    <div class="column is-one-fifth "></div>                                        
</div>                      
</div>
<div class="has-text-centered has-text-weight-bold is-size-3-desktop is-size-5-mobile mt-1"><p>교체선수</p><hr></div>
<div id="sqd-${k}-${idx}-28" class="columns is-mobile">
</div>
<p id="mobilehovertext-name-${k}-${idx}" class="has-text-weight-bold mobilehovertext-name">&nbsp;</p>`
}


function playerTableHTML(k, idx) {
    return `<table class="table is-size-6-desktop is-size-7-mobile" style="margin: 0 auto">
  <thead>
    <tr>
      <th class="mytable-no-scroll"><abbr title="선수">PLAYER</abbr></th>
      <th><abbr title="포지션">POS</abbr></th>
      <th><abbr title="평점">STAR</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},2,this)"></i></th>
      <th><abbr title="슈팅">SHOT</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},3,this)"></i></th>
      <th><abbr title="유효슈팅">E_SHOT</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},4,this)"></i></th>
      <th><abbr title="득점">GOL</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},5,this)"></i></th>
      <th><abbr title="어시스트">ASI</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},6,this)"></i></th>
      <th><abbr title="패스시도">PAS</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},7,this)"></i></th>
      <th><abbr title="패스성공">S_PAS</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},8,this)"></i></th>
      <th><abbr title="드리블시도">DRIB</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},9,this)"></i></th>
      <th><abbr title="드리블성공">S_DRIB</abbr><i class="fa fa-angle-down" onclick="tbSorter(${k},${idx},10,this)"></i></th>
      <th><abbr title="가로채기">INCT</abbr></th>
      <th><abbr title="선방">BLOK</abbr></th>
      <th><abbr title="태클시도">TAKLE</abbr></th>
      <th><abbr title="태클성공">S_TAKLE</abbr></th>
      <th><abbr title="경고">YCARD</abbr></th>
      <th><abbr title="퇴장">RCARD</abbr></th>
    </tr>
  </thead>
  <tbody id="tbod-${k}-${idx}">
  </tbody>
</table>`;
}



function addPlayerTableData(k, idx, playerDto) {
    let tableArr = ['spRating', 'shoot', 'effectiveShoot', 'goal', 'assist', 'passTry', 'passSuccess',
        'dribbleTry', 'dribbleSuccess', 'intercept', 'block', 'tackleTry', 'tackle', 'yellowCards', 'redCards'];
    let player = playerDto.slice(0, 11);
    for (let p of player) {
        let start = `<tr><td class="get-${p.spId}-name mytable-no-scroll has-text-left has-text-weight-bold" style="white-space: nowrap"></td><td class="pos-color-${p.rootPosName}"><small style="background-color: white">${p.posName}</small></td>`;
        tableArr.map(value => {
            start += '<td>' + p[value] + '</td>';
        });
        $(`#tbod-${k}-${idx}`).append(`${start}</tr>`);
    }
}

function tbSorter(k, idx, index, t) {
    let clas = $(t).attr('class');
    let sortType = (clas.indexOf('down') === -1) ? 'up' : 'down';
    var checkSort = true;
    var target = $(`#tbod-${k}-${idx}`).find('tr');
    while (checkSort) {
        checkSort = false;
        target.each(function (i, row) {
            if (row.nextSibling == null) return;
            var fCell = parseFloat(row.cells[index].innerHTML);
            var sCell = parseFloat(row.nextSibling.cells[index].innerHTML);
            if (sortType == 'up' && fCell > sCell) {
                $(row.nextSibling).insertBefore($(row));
                checkSort = true;
            }
            if (sortType == 'down' && fCell < sCell) {
                $(row.nextSibling).insertBefore($(row));
                checkSort = true;
            }
        });
    }
    $(t).attr('class', clas.replace(sortType, (sortType === 'up') ? 'down' : 'up'));
}


function getGoalTimeBar(myGoaltime, yourGoaltime) {
    let mygoals = decideGoalTime(myGoaltime);
    let yourgoals = decideGoalTime(yourGoaltime);
    var options = {
        series: [{
            name: '득점',
            data: mygoals
        }, {
            name: '실점',
            data: yourgoals
        }],
        chart: {
            type: 'bar',
            foreColor: '#FFFFFF',
            toolbar: {
                show: false
            },
            height: 300
        },
        plotOptions: {
            bar: {
                borderRadius: 4,
                horizontal: false,
                columnWidth: '55%',
                endingShape: 'rounded'
            },
        },
        dataLabels: {
            enabled: false
        },
        colors: ['#468af6', '#de5d5d'],
        stroke: {
            show: true,
            width: 2,
            colors: ['transparent']
        },
        xaxis: {
            categories: ['0-15', '15-30', '30-45', '45++', '45-60', '60-75', '75-90', '90++'],
        },
        yaxis: {
            labels: {
                show: false
            }
        },
        grid: {
            show: false
        },
        fill: {
            opacity: 1
        },
        tooltip: {
            theme: 'dark',
            x: {
                show: true
            },
            y: {
                title: {
                    formatter: function () {
                        return '';
                    }
                }
            }
        },
        title: {
            text: `득/실 시간분포`,
            align: 'center'
        }
    };

    var chart = new ApexCharts(document.querySelector("#basic-gtime-chart"), options);
    chart.render();
}
function decideGoalTime(goalTime) {
    let goals = [0, 0, 0, 0, 0, 0, 0, 0];
    for (let i = 0; i < goalTime.length; i++) {
        if (goalTime[i] < 0) {
            goals[3]++;
            continue;
        }
        if (goalTime[i] < 15) {
            goals[0]++;
            continue;
        }
        if (goalTime[i] < 30) {
            goals[1]++;
            continue;
        }
        if (goalTime[i] < 45) {
            goals[2]++;
            continue;
        }
        if (goalTime[i] < 60) {
            goals[4]++;
            continue;
        }
        if (goalTime[i] < 75) {
            goals[5]++;
            continue;
        }
        if (goalTime[i] < 90) {
            goals[6]++;
            continue;
        }
        goals[7]++;
    }
    return goals;
}

function getWdlDonut(wdl, len) {
    let options = {
        series: wdl,
        chart: {
            type: 'donut',
            height: 350,
            foreColor: '#FFFFFF',
            toolbar: {
                show: false
            }
        },
        labels: ["승", "무", "패"],
        title: {
            text: `${len}경기 ${wdl[0]}승 ${wdl[1]}무 ${wdl[2]}패`,
            align: 'center'
        },
        legend: {
            position: 'bottom',
            onItemClick: {
                toggleDataSeries: true
            },
            onItemHover: {
                highlightDataSeries: true
            },
            offsetY: 0,
            offsetX: 0
        }
    };
    let chart = new ApexCharts(document.querySelector("#basic-win-chart"), options);
    chart.render();
}

function getGoalBar(goalList, len) {
    var options = {
        series: [{
            data: goalList
        }],
        chart: {
            type: 'bar',
            foreColor: '#FFFFFF',
            toolbar: {
                show: false
            },
            height: 300
        },
        plotOptions: {
            bar: {
                borderRadius: 4,
                horizontal: false,
                distributed: true
            }
        },
        dataLabels: {
            enabled: true,
            style: {
                fontSize: "1.5em"
            }
        },
        colors: ['#468af6', '#de5d5d'],
        xaxis: {
            categories: ['득점', '실점'],
        },
        yaxis: {
            labels: {
                show: false
            }
        },
        grid: {
            show: false
        },
        title: {
            text: `${len} 경기 득/실점`,
            align: 'center'
        },
        tooltip: {
            theme: 'dark',
            x: {
                show: true
            },
            y: {
                title: {
                    formatter: function () {
                        return ''
                    }
                }
            }
        }
    };
    var chart = new ApexCharts(document.querySelector("#basic-goal-chart"), options);
    chart.render();
}

function getMatchHorBar(k, idx, summaryDtoList, keys, myname, yourname) {
    var options = {
        series: [
            {
                name: '',
                data: [1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5]
            },
            {
                name: '',
                data: [-1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5]
            },
            {
                name: yourname,
                data: keys.map(v => summaryDtoList[1][v] + 1)
            },
            {
                name: myname,
                data: keys.map(v => -(summaryDtoList[0][v]) - 1)
            }
        ],
        chart: {
            type: 'bar',
            height: 440,
            stacked: true,
            toolbar: {
                show: false
            }
        },
        colors: ['#FFFFFF', '#FFFFFF', '#FF4560', '#008FFB'],
        plotOptions: {
            bar: {
                horizontal: true,
                barHeight: '80%',
            },
        },
        dataLabels: {
            enabled: true,
            textAnchor: 'end',
            style: {
                colors: ['#000000', '#000000', '#FFFFFF', '#FFFFFF'],
                fontWeight: 'bold',
            },
            formatter: function (value, {seriesIndex, dataPointIndex, w}) {
                let lab = w.config.xaxis.categories[dataPointIndex].toString();
                let labLen = lab.length;
                if (seriesIndex === 0) {
                    return lab.substr(labLen / 2, labLen);
                } else if (seriesIndex === 1) {
                    return lab.substr(0, labLen / 2);
                } else {
                    return Math.abs(value) - 1;
                }
            }
        },
        stroke: {
            width: 1,
            colors: ["#fff"]
        },
        grid: {
            show: false,
            xaxis: {
                show: false
            },
            yaxis: {
                show: false
            }
        },
        fill: {opacity: [0, 0, 1, 1]},
        yaxis: {
            min: -13,
            max: 13,
            labels: {
                show: false,
                align: 'center',
                style: {
                    colors: ['#000000'],
                    zIndex: 9999,
                    opacity: 1.5,
                },
                offsetX: $(`#hobar-${k}-0`).innerWidth() * 0.5
            },
            axisBorder: {
                show: false
            }
        },
        tooltip: {
            enabled: false,
            x: {
                show: false
            },
            y: {
                show: false
            }
        },
        title: {
            text: myname + ' vs ' + yourname,
            align: 'center',
            style: {
                fontWeight: 'bold'
            }
        },
        xaxis: {
            categories: ['슈팅', '유효슛 ', '코너킥 ', '프리킥 ', '옵사', '파울', '자책골 ', '경고', '퇴장'
            ],
            labels: {
                show: false
            },
            axisBorder: {
                show: false
            },
            axisTicks: {
                show: false
            }
        },
        responsive: [
            {
                breakpoint: 780,
                options: {
                    dataLabels: {
                        style: {
                            fontSize: '8'
                        }
                    }
                }
            },
            {
                breakpoint: 480,
                options: {
                    dataLabels: {
                        style: {
                            fontSize: '7'
                        }
                    },
                    plotOptions: {
                        bar: {
                            barHeight: '80%'
                        }
                    },
                    chart: {
                        height: 380
                    }
                }
            }
        ]
    }
    var chart = new ApexCharts(document.querySelector(`#hobar-${k}-${idx}`), options);
    chart.render();
}

function getPassChart(k, myname, passDto) {

    var options = {
        series: [{
            name: '숏패스',
            data: [passDto.shortPassTry + 1, passDto.shortPassSuccess + 1]
        }, {
            name: '스루패스',
            data: [passDto.throughPassTry + 1, passDto.throughPassSuccess + 1]
        }, {
            name: '롱패스',
            data: [passDto.longPassTry + 1, passDto.longPassSuccess + 1]
        }, {
            name: '로빙패스',
            data: [passDto.lobbedThroughPassTry + passDto.bouncingLobPassTry + 1,
                passDto.lobbedThroughPassSuccess + passDto.bouncingLobPassSuccess + 1]
        }, {
            name: '드리븐패스',
            data: [passDto.drivenGroundPassTry + 1, passDto.drivenGroundPassSuccess + 1]
        }],
        chart: {
            type: 'bar',
            width: '99%',
            height: '80%',
            stacked: true,
            toolbar: {
                show: false
            }
        },
        plotOptions: {
            bar: {
                horizontal: true
            }
        },
        stroke: {
            width: 1,
            colors: ['#fff']
        },
        title: {
            text: myname + "님의 패스기록",
            style: {
                fontWeight: 'bolder',
            },
            align: 'center'
        },
        xaxis: {
            categories: ["시도", "성공"],
            labels: {
                show: false,
            },
            axisBorder: {
                show: false
            },
            axisTicks: {
                show: false
            }
        },
        yaxis: {
            labels: {
                show: true,
                align: 'right',
                style: {
                    colors: ['#000000', '#1E7CEC']
                }
            }
        },
        fill: {
            opacity: 0.9
        },
        legend: {
            position: 'top',
            horizontalAlign: 'left',
            offsetX: 0,
            formatter: function (seriesName) {
                return [seriesName.toString().replace("패스", "")];
            }
        },
        grid: {
            show: false,
            xaxis: {
                lines: {
                    show: false
                }
            },
            yaxis: {
                lines: {
                    show: false
                }
            }
        },
        dataLabels: {
            formatter(val) {
                return val - 1;
            }
        },
        tooltip: {
            shared: false,
            y: {
                formatter: function (val) {
                    return val - 1;
                }
            }
        },
        responsive: [
            {
                breakpoint: 480,
                options: {
                    yaxis: {
                        labels: {
                            style: {
                                fontSize: '8'
                            }
                        }
                    },
                    plotOptions: {
                        bar: {
                            barHeight: '45%'
                        }
                    }
                }
            }
        ]
    };
    var chart = new ApexCharts(document.querySelector(`#pass-chart-${k}`), options);
    chart.render();
}

function getPossChart(k, myPo, yourPo, myname, yourname) {
    var options = {
        series: [myPo, yourPo],
        chart: {
            width: 260,
            type: 'pie',
            foreColor: '#000000',
            toolbar: {
                show: false
            }
        },
        title: {
            text: `점유율`,
            align: 'center'
        },
        labels: [myname, yourname],
        dataLabels: {
            formatter(val, opts) {
                const name = opts.w.globals.labels[opts.seriesIndex]
                return [name, val.toFixed(1) + '%']
            }
        },
        legend: {
            show: false
        }
    };
    var chart = new ApexCharts(document.querySelector(`#poss-chart-${k}`), options);
    chart.render();
}

function makeDiscrete(shootDtoList, idx) {
    let result = [];
    let colors = ["#f003fc", "#00c9ff"];
    for (let i = 0; i < shootDtoList.length; i++) {
        let buralSize = Math.pow(shootDtoList[i]["prediction"], 1.6) * 15 + 5;
        result.push({
            seriesIndex: idx, dataPointIndex: i, size: buralSize.toFixed(2), shape: "circle"
            , fillColor: (shootDtoList[i]["result"] === 3) ? colors[idx] : ""
        })
    }
    return result;
}

function getShootChart(k, shootDtoList, myname, yourname) {
    let dtos = [shootDtoList[0].map(v => [(v['x']), v['y']]), shootDtoList[1].map(v => [1 - (v['x']), v['y']])];
    let type = ['', '일반적인 슈팅', '정교한 슈팅', '헤더'];
    let result = ['', '유효슈팅', '벗어나는 슈팅', '득점', '골대맞음'];
    let disc = makeDiscrete(shootDtoList[0], 0).concat(makeDiscrete(shootDtoList[1], 1));
    let mobDisc = JSON.parse(JSON.stringify(disc));
    mobDisc.map(val => val.size = Math.sqrt(val.size) - 0.2);
    var options = {
        series: [
            {
                name: myname,
                type: 'scatter',
                data: dtos[0]
            }, {
                name: yourname,
                type: 'scatter',
                data: dtos[1]
            }
        ],
        chart: {
            width: '100%',
            type: 'line',
            toolbar: {
                show: false
            },
            zoom: {
                enabled: false
            },
            events: {
                dataPointSelection: (event, chartContext, config) => {
                    let who = config.seriesIndex;
                    if (who >= 2) {
                        return;
                    }
                    let info = config.w.config.series[who].data[config.dataPointIndex];
                    let dto = shootDtoList[who].filter(it => (it.x === info[0] || it.x === (1 - info[0])) && it.y === info[1] && it.assist === true);
                    if (dto.length !== 0) {
                        config.w.config.series[2] = {
                            name: '어시스트',
                            data: [[info[0], info[1]], [Math.abs(who - dto[0].assistX), dto[0].assistY]]
                        }
                        chart.updateSeries(config.w.config.series);
                    }
                }
            }
        },
        fill: {
            type: ['solid', 'solid']
        },
        colors: ['#ff0000', '#0211f8', '#00FF00'],
        markers: {
            enabled: true,
            strokeWidth: 0.1,
            discrete: disc,
            // size: [5,7,0],
            hover: {
                size: 10
            }
        },
        xaxis: {
            min: 0,
            max: 1,
            labels: {
                show: false
            },
            axisBorder: {
                show: false
            },
            axisTicks: {
                show: false
            },
            tooltip: {
                enabled: false
            }
        },
        yaxis: {
            min: 0,
            max: 1,
            labels: {
                show: false
            }
        },
        grid: {
            show: false,
            xaxis: {
                show: false
            },
            yaxis: {
                show: false
            }
        },
        legend: {
            show: true,
            fontSize: '19em',
            fontWeight: 800,
            position: 'top',
            labels: {
                colors: '#FFFFFF'
            }
        },
        tooltip: {
            enabled: true,
            shared: false,
            intersect: true,
            enabledOnSeries: [0, 1],
            x: {
                show: true,
                formatter: function (series, value) {
                    let curData = shootDtoList[value['seriesIndex']][value['dataPointIndex']];
                    let gt = (curData.goalTime >= 0) ? curData.goalTime : "45+" + (-curData.goalTime - 45);
                    return [gt + '` ' + $(`.get-${curData.spId}-name`)[0].innerText + '\n<br>' + "기대득점(" + curData.prediction.toFixed(2) + ")=>" + ((curData.hitPost) ? result[4] : result[curData.result])];
                }
            },
            y: {
                show: true,
                formatter: function (series, value) {
                    let curData = shootDtoList[value['seriesIndex']][value['dataPointIndex']];
                    let x = curData.x;
                    let gType = type[curData.type];
                    return ['좌표(' + Math.round(x * 100) / 100 + ','
                    + Math.round(series * 100) / 100 + ')\n' + gType];
                }
            }
        },
        responsive: [
            {
                breakpoint: 480,
                options: {
                    markers: {
                        discrete: mobDisc,
                        hover: {
                            size: 3.5
                        }
                    },
                    legend: {
                        fontSize: '10px'
                    },
                    tooltip: {
                        style: {
                            fontSize: '5px'
                        }
                    }
                }
            }
        ]
    }
    if (isMobile()) {
        $(`#shoot-all-warn-${k}`).text("현재 기기에서는 어시스트 경로 확인이 제한됩니다.")
        options.chart.events.dataPointSelection = undefined;
    }
    var chart = new ApexCharts(document.querySelector(`#shoot-all-${k}`), options);
    chart.render();
}

function getXgChart(k, shootDto, myname, yourname) {
    let myXg = shootDto[0].map((v) => [Math.abs(v.goalTime), parseFloat(v.prediction.toFixed(2))]);
    myXg.map(v => v[1]).reduce(function (x, y, i) {
        return myXg[i][1] = Math.round((x + y) * 100) / 100;
    });
    let yourXg = shootDto[1].map((v) => [Math.abs(v.goalTime), parseFloat(v.prediction.toFixed(2))]);
    yourXg.map(v => v[1]).reduce(function (x, y, i) {
        return yourXg[i][1] = Math.round((x + y) * 100) / 100;
    });
    let maxTime = Math.max(myXg[myXg.length - 1][0], yourXg[yourXg.length - 1][0]);
    let maxVal = [myXg[myXg.length - 1][1], yourXg[yourXg.length - 1][1]];
    let goals = myXg.filter((v,i)=>shootDto[0][i].result===3)
        .concat(yourXg.filter((v,i)=>shootDto[1][i].result===3));
    let goalPoints = [];
    let futbullImage = {path:"/img/futbull.png",width:10,height:10};
    for(let i = 0 ; i < goals.length; i ++){
        goalPoints.push({x:goals[i][0],y:goals[i][1],image:futbullImage});
    }
    var options = {
        series: [
            {
                name: myname,
                data: [[0, 0]].concat(myXg).concat([[maxTime + 3, maxVal[0]]])
            },
            {
                name: yourname,
                data: [[0, 0]].concat(yourXg).concat([[maxTime + 3, maxVal[1]]])
            }
        ],
        chart: {
            type: 'area',
            height: 350,
            stacked: false,
            toolbar: {
                show: false
            }
        },
        colors: ['#fb0075', '#0079e3'],
        dataLabels: {
            enabled: false
        },
        stroke: {
            curve: 'stepline'
        },
        fill: {
            type: 'gradient',
            gradient: {
                opacityFrom: 0.6,
                opacityTo: 0.8,
            }
        },
        legend: {
            position: 'top',
            horizontalAlign: 'center'
        },
        xaxis: {
            type: 'numeric',
            labels: {
                formatter: function (value) {
                    return parseInt(value) + "분";
                }
            }
        },
        yaxis: {
            type: 'numeric',
            labels: {
                formatter: function (value) {
                    return (Math.round(value*100)/100)+"XG"
                }
            }
        },
        annotations: {
            points: goalPoints
        }
    };

    var chart = new ApexCharts(document.querySelector(`#xgchart-${k}`), options);
    chart.render();
}