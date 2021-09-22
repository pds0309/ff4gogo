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
        postDerBogi(los.allcode.length);
        getSummaryInfo(userid, los.detail);
        return;
    }
    getMatchCodeList(userid)
        .done(function (res1) {
            let matchList = res1["data"];
            let getMatch = matchList.slice(0, 10);

            let re = async () => {
                let md = [];
                let mc = [];
                for (let k = 0; k < getMatch.length; k++) {
                    let matches = getMatch[k];
                    await getMatchDetailList(userid, matches)
                        .done(function (res2) {
                            if (res2 != null) {
                                getViewMatches(res2.data, k);
                                md[k] = res2.data;
                                mc[k] = matches;
                            }
                        })
                }
                return [md, mc];
            }
            re().then(value => {
                let matches = {
                    detail: value[0],
                    codes: value[1],
                    timestamp: new Date().getTime(),
                    allcode: res1.data
                }
                deleteFromLocal();
                localStorage.setItem(userid, JSON.stringify(matches));
                postDerBogi(res1.data.length);
                getSummaryInfo(userid, value[0]);
            });
        })
});

function getViewMatches(dto, k) {
    let basic = dto["basicDtoList"];
    let owngoals = [dto["summaryDtoList"][0].ownGoal, dto["summaryDtoList"][1].ownGoal];
    getHTMLMatches(basic, owngoals, k);

    $(`#id-match-btn-${k}`).click(function () {
        clickDetail(k);
    });
}

function clickDetail(k) {
    let detailDiv = document.getElementById(`id-match-detail-${k}`);
    if (detailDiv.style.display === "none") {
        detailDiv.style.display = "block";
        if ($(`#id-match-detail-${k}`).text().length === 0) {
            $(`#id-match-detail-${k}`).append(`<div class="columns is-mobile has-background-info-light">
                            <div class="column">
                                <small><i class="fa fa-futbol"></i>&nbsp;45' 이브라히모비치</small><br>
                                <small>66' </small>
                            </div>
                            <div class="column">
                                <small>96' 이브라히모비치</small><br>
                                <small>hello</small>
                            </div>
                        </div>
                        <div class="tabs is-centered is-toggle is-fullwidth is-justify-content-center">
                            <ul class=" p-0 has-text-weight-bold column tabs inner ${k}">
                                <li class="is-active"><a href="#squad-${k}">Squad</a></li>
                                <li><a href="#match-${k}">경기</a></li>
                                <li><a href="#player-${k}">선수</a></li>
                                <li><a href="#shoot-${k}">슛</a></li>
                            </ul>
                        </div>
                        <div>
                            <div id="squad-${k}" class="container tab_content inner ${k}">
                                <p>스쿼드예용</p>
                            </div>
                            <div id="match-${k}" class="container tab_content inner ${k}">
                                경기예용
                            </div>
                            <div id="player-${k}" class="container tab_content inner ${k}">
                                선수예용
                            </div>
                            <div id="shoot-${k}" class="container tab_content inner ${k}">
                                슛이예여ㅛㅇ
                            </div>
                        </div>
                    </div>`);
        }
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

    } else {
        detailDiv.style.display = "none";
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
        type: 'GET',
        url: `/users/${userid}/matches?mc=${usermatchCode}`,
        error: function () {
            $(`#id-finderror`).append('<p>[-301]비정상적인 순위경기 식별</p>');
            return null;
        }
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
    for (let k = 0; k < newMatchList.length; k++) {
        getMatchDetailList(id, newMatchList[k])
            .done(function (result) {
                if (result != null) {
                    getViewMatches(result.data, index + k);
                }
            });
    }
    if (matchCodeList.length <= index + 5) {
        $('#id-derbogi').hide();
    }
}


function postGangsin(timestamp) {
    let now = new Date().getTime();
    let time = (((now - timestamp) / (1000 * 60))) / 60;
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

function postDerBogi(len) {
    if (len > 10) {
        $('#id-derbogi').show();
    }
}

function getSummaryInfo(userid, dtos) {
    let goal = [0, 0];
    let wdl = [0, 0, 0];
    let myplayers = [];
    for (let k = 0; k < dtos.length; k++) {
        goal[0] += dtos[k].basicDtoList[0].goalTotal;
        goal[1] += dtos[k].basicDtoList[1].goalTotal;

        let result = dtos[k].basicDtoList[0].matchResult;
        if (result === "승") {
            wdl[0]++;
        } else if (result === "무") {
            wdl[1]++;
        } else {
            wdl[2]++;
        }
        let matchPlayers = dtos[k].matchPlayerDtoList[0];
        for (let j = 0; j < matchPlayers.length; j++) {
            if (matchPlayers[j].spPosition !== 28) {
                myplayers.push(matchPlayers[j]);
            }
        }
    }
    getGoalBar(goal, dtos.length);
    getWdlDonut(wdl, dtos.length);
    getMvps(myplayers, userid)
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

function getWdlDonut(wdl, len) {
    let options = {
        series: wdl,
        chart: {
            type: 'donut',
            height: 300,
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
            position: 'right',
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
                            &nbsp;&nbsp;${mybasic.nickname}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${mybasic.goalTotal + owngoals[1]} : ${yourbasic.goalTotal + owngoals[0]}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${yourbasic.nickname}&nbsp;&nbsp;
                                <i class="fa fa-${controllers}"></i>
                            </span>
                        </div>
                    </div>
                    <div id="id-match-detail-${k}" class="card-content pl-3 pr-3" style="display: none"></div>
                </div><br><br>
            </div>`;
    $(`#id-container-matchlist`).append(htmls);
}