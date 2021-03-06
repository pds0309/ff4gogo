"use strict";

function getAnalGita() {
    $("#anal-1").toggle()
}

function sortDto(t, e) {
    return t.sort(function (t, s) {
        return Math.abs(t[e]) - Math.abs(s[e])
    })
}

function tbSorter(t, e, s, i) {
    let a = $(i).attr("class"), n = -1 === a.indexOf("down") ? "up" : "down";
    for (var o = !0, l = $(`#tbod-${t}-${e}`).find("tr"); o;) o = !1, l.each(function (t, e) {
        if (null != e.nextSibling) {
            var i = parseFloat(e.cells[s].innerHTML), a = parseFloat(e.nextSibling.cells[s].innerHTML);
            "up" == n && i > a && ($(e.nextSibling).insertBefore($(e)), o = !0), "down" == n && i < a && ($(e.nextSibling).insertBefore($(e)), o = !0)
        }
    });
    $(i).attr("class", a.replace(n, "up" === n ? "down" : "up"))
}

$(document).ready(function () {
    const t = $("#userid").text();
    if (null != localStorage.getItem(t)) {
        let s = JSON.parse(localStorage.getItem(t));
        for (let t = 0; t < s.codes.length; t++) e(s.detail[t], t);
        !function (t) {
            let e = ((new Date).getTime() - t) / 6e4;
            e <= 120 ? $("#id-text-gangsintime").text(Math.round(e) + "분 전 갱신됨") : ($("#id-text-gangsintime").text(Math.round(e / 60) + "시간 전 갱신됨"), $("#id-btn-gangsin").removeAttr("disabled"))
        }(s.timestamp), l(s.allcode.length, s.codes.length), s = JSON.parse(localStorage.getItem(t)), r(t, s.detail)
    } else (function (t) {
        return $.ajax({
            type: "POST", url: `/users/${t}/matches`, contentType: "applicatiion/json", error: function (t) {
                $("#id-section-nodata").show(), $("#id-all-match").hide(), $("#id-all-match-list").hide(), $("#id-finderror").append("[" + t.responseJSON.code + "] " + t.responseJSON.message)
            }
        })
    })(t).done(function (s) {
        let i = s.data, a = i.slice(0, 10), n = [], d = [];
        (async function (e) {
            const s = e.map((e, s) => (function (e, s) {
                return new Promise((a, l) => {
                    setTimeout(() => {
                        o(t, e).done(function (t, e, o) {
                            204 !== o.status ? (a(n[s] = t.data), a(d[s] = i[s])) : (a(null), i[s] = null, $("#id-finderror").append("<p>비정상적인 순위경기 제외됨</p>"))
                        })
                    }, 3)
                })
            })(e, s));
            return await Promise.all(s)
        })(a).then(s => {
            let a = s.filter(t => null !== t), n = d.filter(t => null !== t), o = i.filter(t => null !== t);
            for (let t = 0; t < a.length; t++) e(a[t], t);
            let c = {detail: a, codes: n, timestamp: (new Date).getTime(), allcode: o};
            !function () {
                if (localStorage.length >= 7) {
                    let t = 9999999999999, e = "1";
                    for (let s = 0; s < localStorage.length; s++) {
                        let i = localStorage.key(s), a = JSON.parse(localStorage.getItem(i)).timestamp;
                        a < t && (t = a, e = i)
                    }
                    localStorage.removeItem(e)
                }
            }(), localStorage.setItem(t, JSON.stringify(c)), l(o.length, n.length), r(t, JSON.parse(localStorage.getItem(t)).detail)
        })
    });

    function e(t, e) {
        !function (t, e, s) {
            let i = t[0], a = t[1],
                n = "승" === i.matchResult ? "#add8e6" : "무" === i.matchResult ? "#d3d3d3" : "#fcadc8",
                o = "etc" === i.controller ? "question" : i.controller,
                l = `<div id="id-match-${s}" class="has-text-centered has-text-white clas">\n                <div class="card">\n                    <header class="card-header" style="background-color: ${n}">&nbsp;&nbsp;&nbsp;\n                        <p class="card-header-title is-size-4-desktop is-size-6-mobile">\n                            ${["", "몰수", "몰수"][i.matchEndType]}${i.matchResult}\n                        </p>\n                        <span class="card-header-icon has-text-right is-size-5-desktop is-size-7-mobile">${i.matchDate}</span>\n                        <button id="id-match-btn-${s}" class="card-header-icon hovable" aria-label="more options">\n                            <span class="icon">\n                            <i class="fa fa-angle-down" aria-hidden="true"></i>\n                        </span>\n                        </button>\n                    </header>\n                    <div class="card-content">\n                        <div class="content has-text-weight-bold is-size-4-desktop is-size-7-mobile">\n                            <span>\n                                <i class="fa fa-${o}"></i>\n                            &nbsp;&nbsp;<span id="uname-${s}-0">${i.nickname}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${i.goalTotal + e[1]} : ${a.goalTotal + e[0]}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="uname-${s}-1">${a.nickname}</span>&nbsp;&nbsp;\n                                <i class="fa fa-${o}"></i>\n                            </span>\n                        </div>\n                    </div>\n                    <div id="id-match-detail-${s}" class="card-content pl-3 pr-3" style="display: none"></div>\n                </div><br><br>\n            </div>`;
            $("#id-container-matchlist").append(l)
        }(t.basicDtoList, [t.summaryDtoList[0].ownGoal, t.summaryDtoList[1].ownGoal], e), $(`#id-match-btn-${e}`).click(function () {
            0 !== t.matchPlayerDtoList[0].length && 0 !== t.matchPlayerDtoList[1].length || ($(`#id-match-detail-${e}`).append('<p class="has-text-danger is-size-5-desktop is-size-7-mobile">조기 몰수 처리로 해당 경기의 데이터가 불온전하여 조회하실 수 없습니다.</p>'), $(`#id-match-btn-${e}`).attr("disabled", !0)), function (t, e) {
                let o = $(`#uname-${t}-1`).text(), l = $(`#uname-${t}-0`).text(),
                    r = document.getElementById(`id-match-detail-${t}`);
                if ("none" === r.style.display) {
                    if (r.style.display = "block", 0 === $(`#id-match-detail-${t}`).text().length) {
                        $(`#id-match-detail-${t}`).append(`<div class="columns is-mobile has-background-info-light">\n                            <div id="match-goals-${t}-0" class="column">\n                            </div>\n                            <div id="match-goals-${t}-1"  class="column">\n                            </div>\n                        </div>\n                        <div class="tabs is-centered is-toggle is-fullwidth is-justify-content-center">\n                            <ul class=" p-0 has-text-weight-bold column tabs inner ${t}">\n                                <li class="is-active"><a href="#squad-${t}">Squad</a></li>\n                                <li><a href="#match-${t}">경기</a></li>\n                                <li><a href="#player-${t}">선수</a></li>\n                                <li id="id-shoot-${t}-li" class="ele-no-react"><a id="id-shoot-${t}" href="#shoot-${t}">슛</a></li>\n                            </ul>\n                        </div>\n                        <div>\n                            <div id="squad-${t}" class="container tab_content inner ${t}">\n<div class="field has-text-centered">\n  <span class="has-text-weight-bold">${o}&nbsp;</span>\n  <input id="switch-squad-${t}" type="checkbox" name="switch-squad-${t}" class="switch is-success" checked="checked">\n  <label class="has-text-weight-bold" for="switch-squad-${t}">&nbsp;</label><span class="has-text-weight-bold">&nbsp;${l}</span>\n</div>\n    <div id="squad-all-${t}-true">${u(t, 0)}</div>\n    <div id="squad-all-${t}-false" style="display: none">${u(t, 1)}</div>\n</div>\n                            </div>\n                            <div id="match-${t}" class="container tab_content inner ${t}">\n<div class="columns">\n<div class="column is-one-third has-text-centered is-justify-content-center">\n    <div id="poss-chart-${t}" style="width: fit-content; display: inline-block"></div>\n</div>\n<hr>\n<div id="pass-chart-${t}" class="column is-two-thirds p-0"></div>\n</div>\n<hr>\n<div class="has-text-centered is-fullwidth p-0">\n    <div id="hobar-${t}-0"></div>\n</div>                               \n                            </div>\n                            <div id="player-${t}" class="container tab_content inner ${t}">\n<div class="field has-text-centered">\n    <span class="has-text-weight-bold">${o}&nbsp;</span>\n    <input id="switch-player-${t}" type="checkbox" name="switch-player-${t}" class="switch is-danger" checked="checked">\n    <label class="has-text-weight-bold" for="switch-player-${t}">&nbsp;</label><span class="has-text-weight-bold">&nbsp;${l}</span>\n</div>\n<div id="player-all-${t}-true" style="width: 100%;overflow: auto;">\n    ${m(t, 0)}\n</div>\n<div id="player-all-${t}-false" style="width: 100%;overflow: auto; display: none">\n    ${m(t, 1)}\n</div>\n                            </div>\n                            <div id="shoot-${t}" class="container tab_content inner ${t}"></div>\n                            </div>\n                        </div>\n                    </div>`), $(`#switch-squad-${t}`).click(function () {
                            let e = $(`#switch-squad-${t}`).is(":checked");
                            $(`#squad-all-${t}-${!e}`).css("display", "none"), $(`#squad-all-${t}-${e}`).css("display", "block")
                        }), $(`#switch-player-${t}`).click(function () {
                            let e = $(`#switch-player-${t}`).is(":checked");
                            $(`#player-all-${t}-${!e}`).css("display", "none"), $(`#player-all-${t}-${e}`).css("display", "block")
                        });
                        let r = sortDto(e.shootDtoList[0], "goalTime"), d = sortDto(e.shootDtoList[1], "goalTime"),
                            c = [r, d];
                        i(s(r), r, t), i(s(d), d, t);
                        sorter(t,0);
                        sorter(t,1);
                        let h = $(window).width();
                        if ($(`#id-shoot-${t}`).click(function () {
                            0 === $(`#shoot-${t}`).text().length && (h = $(window).width(), $(`#shoot-${t}`).append(`\n                        <div class="has-text-black is-size-3-desktop is-size-5-mobile has-text-centered has-text-weight-bold">\n                            <p>슈팅분포</p>\n                            <hr>\n                                <p id="shoot-all-warn-${t}" class="is-size-7-mobile has-text-danger"></p><br>\n                        </div>\n                    <div id="shoot-all-${t}" class="has-text-centered is-fullwidth p-0 shootscatter">\n                    </div>\n                    <hr>\n                        <div class="has-text-black is-size-3-desktop is-size-5-mobile has-text-centered has-text-weight-bold">\n                            <p>경기기대득점[XG] 추이</p>\n                            <hr>\n                                <p id="xgchart-${t}" class="is-size-5-desktop is-size-7-mobile"></p>\n                        </div>`), setTimeout(function () {
                                !function (t, e, s, i) {
                                    let a = [e[0].map(t => [t.x, t.y]), e[1].map(t => [1 - t.x, t.y])],
                                        n = ["", "일반적인 슈팅", "정교한 슈팅", "헤더"], o = ["", "유효슈팅", "벗어나는 슈팅", "득점", "골대맞음"],
                                        l = g(e[0], 0, 15).concat(g(e[1], 1, 15));
                                    var r = {
                                        series: [{name: s, type: "scatter", data: a[0]}, {
                                            name: i,
                                            type: "scatter",
                                            data: a[1]
                                        }],
                                        chart: {
                                            width: "100%",
                                            type: "line",
                                            toolbar: {show: !1},
                                            zoom: {enabled: !1},
                                            events: {
                                                dataPointSelection: (t, s, i) => {
                                                    let a = i.seriesIndex;
                                                    if (a >= 2) return;
                                                    let n = i.w.config.series[a].data[i.dataPointIndex],
                                                        o = e[a].filter(t => (t.x === n[0] || t.x === 1 - n[0]) && t.y === n[1] && !0 === t.assist);
                                                    0 !== o.length && (i.w.config.series[2] = {
                                                        name: "어시스트",
                                                        data: [[n[0], n[1]], [Math.abs(a - o[0].assistX), o[0].assistY]]
                                                    }, d.updateSeries(i.w.config.series))
                                                }
                                            }
                                        },
                                        fill: {type: ["solid", "solid"]},
                                        colors: ["#f003fc", "#00c9ff", "#00FF00"],
                                        markers: {enabled: !0, strokeWidth: .1, discrete: l, hover: {size: 10}},
                                        xaxis: {
                                            min: 0,
                                            max: 1,
                                            labels: {show: !1},
                                            axisBorder: {show: !1},
                                            axisTicks: {show: !1},
                                            tooltip: {enabled: !1}
                                        },
                                        yaxis: {min: 0, max: 1, labels: {show: !1}},
                                        grid: {show: !1, xaxis: {show: !1}, yaxis: {show: !1}},
                                        legend: {
                                            show: !0,
                                            fontSize: "19em",
                                            fontWeight: 800,
                                            position: "top",
                                            inverseOrder: !0,
                                            labels: {colors: "#FFFFFF"},
                                            onItemClick: {toggleDataSeries: !1},
                                            onItemHover: {highlightDataSeries: !1}
                                        },
                                        tooltip: {
                                            enabled: !0,
                                            shared: !1,
                                            intersect: !0,
                                            enabledOnSeries: [0, 1],
                                            x: {
                                                show: !0, formatter: function (t, s) {
                                                    let i = e[s.seriesIndex][s.dataPointIndex];
                                                    if (0 === i.spId) return "정보없음!";
                                                    let a = i.goalTime >= 0 ? i.goalTime : "45+" + (-i.goalTime - 45);
                                                    return [a + "` " + $(`.get-${i.spId}-name`)[0].innerText + "\n<br>기대득점(" + i.prediction.toFixed(2) + ")=>" + (i.hitPost ? o[4] : o[i.result])]
                                                }
                                            },
                                            y: {
                                                show: !0, formatter: function (t, s) {
                                                    let i = e[s.seriesIndex][s.dataPointIndex], a = i.x, o = n[i.type];
                                                    return ["좌표(" + Math.round(100 * a) / 100 + "," + Math.round(100 * t) / 100 + ")\n" + o]
                                                }
                                            }
                                        }
                                    };
                                    if (isMobile()) {
                                        $(`#shoot-all-warn-${t}`).text("현재 기기에서는 어시스트 경로 확인이 제한됩니다."), r.chart.events.dataPointSelection = void 0;
                                        let s = $(".card-header").width(),
                                            i = g(e[0], 0, Math.sqrt(.025 * s, 1.45) + .9).concat(g(e[1], 1, Math.sqrt(.025 * s, 1.45) + .9));
                                        r.markers.discrete = i, r.markers.hover.size = 3, $("body").width() <= 480 && (r.chart.height = .86 * s, r.chart.width = 1.92 * s, $(`#shoot-all-${t}`).addClass("shootscatter-small"), $(`#shoot-all-${t}`).css("marginBottom", r.chart.width - $(`#shoot-all-${t}`).width()), r.legend.position = "right", r.legend.floating = "true", r.legend.fontSize = "10px", $(".shootscatter-small-legend").width(.8 * s))
                                    }
                                    var d = new ApexCharts(document.querySelector(`#shoot-all-${t}`), r);
                                    d.render(), isMobile() && $("body").width()
                                }(t, c, l, o), function (t, e, s, i) {
                                    let a = e[0].map(t => [Math.abs(t.goalTime), parseFloat(t.prediction.toFixed(2))]);
                                    a.map(t => t[1]).reduce(function (t, e, s) {
                                        return a[s][1] = Math.round(100 * (t + e)) / 100
                                    });
                                    let n = e[1].map(t => [Math.abs(t.goalTime), parseFloat(t.prediction.toFixed(2))]);
                                    n.map(t => t[1]).reduce(function (t, e, s) {
                                        return n[s][1] = Math.round(100 * (t + e)) / 100
                                    });
                                    let o = Math.max(a[a.length - 1][0], n[n.length - 1][0]),
                                        l = [a[a.length - 1][1], n[n.length - 1][1]],
                                        r = a.filter((t, s) => 3 === e[0][s].result).concat(n.filter((t, s) => 3 === e[1][s].result)),
                                        d = [], c = {path: "/img/futbull.png", width: 10, height: 10};
                                    for (let t = 0; t < r.length; t++) d.push({x: r[t][0], y: r[t][1], image: c});
                                    var h = {
                                        series: [{
                                            name: s,
                                            data: [[0, 0]].concat(a).concat([[o + 3, l[0]]])
                                        }, {name: i, data: [[0, 0]].concat(n).concat([[o + 3, l[1]]])}],
                                        chart: {type: "area", height: 350, stacked: !1, toolbar: {show: !1}},
                                        colors: ["#fb0075", "#0079e3"],
                                        dataLabels: {enabled: !1},
                                        stroke: {curve: "stepline"},
                                        fill: {type: "gradient", gradient: {opacityFrom: .6, opacityTo: .8}},
                                        legend: {position: "top", horizontalAlign: "center"},
                                        xaxis: {
                                            type: "numeric", labels: {
                                                formatter: function (t) {
                                                    return parseInt(t) + "분"
                                                }
                                            }
                                        },
                                        yaxis: {
                                            type: "numeric", labels: {
                                                formatter: function (t) {
                                                    return Math.round(100 * t) / 100 + "XG"
                                                }
                                            }
                                        },
                                        annotations: {points: d}
                                    };
                                    new ApexCharts(document.querySelector(`#xgchart-${t}`), h).render()
                                }(t, c, l, o)
                            }, 400))
                        }), isMobile()) {
                            let e = null;
                            $(window).on("resize", function () {
                                clearTimeout(e), 0 !== $(`#shoot-${t}`).text().length && (e = setTimeout(function () {
                                    Math.abs(h - $(window).width()) > 100 && ($(`#shoot-all-${t}`).removeClass("shootscatter-small"), $(`#xgchart-${t}`).empty(), $(`#shoot-all-${t}`).empty(), $(`#shoot-${t}`).empty(), $(`#id-shoot-${t}-li`).hasClass("is-active") && $(`#id-shoot-${t}`).trigger("click"))
                                }, 1200))
                            })
                        }
                        !function (t, e) {
                            let s = sortDto(t.shootDtoList[0], "goalTime"), i = sortDto(t.shootDtoList[1], "goalTime");
                            a(s, e, 0), a(i, e, 1), n(t.matchPlayerDtoList[0], e, 0), n(t.matchPlayerDtoList[1], e, 1)
                        }(e, t);
                        let p = e.summaryDtoList[0], b = e.summaryDtoList[1];
                        f(t, 0, sortDto(e.matchPlayerDtoList[0], "spPosition")), f(t, 1, sortDto(e.matchPlayerDtoList[1], "spPosition"));
                        let y = ["shootTotal", "effectiveShootTotal", "cornerKick", "shootFreekick", "offsideCount", "foul", "ownGoal", "yellowCards", "redCards"];
                        !function (t, e, s, i, a, n) {
                            var o = {
                                series: [{name: "", data: [1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5]}, {
                                    name: "",
                                    data: [-1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5]
                                }, {name: n, data: i.map(t => s[1][t] + 1)}, {name: a, data: i.map(t => -s[0][t] - 1)}],
                                chart: {type: "bar", height: 440, stacked: !0, toolbar: {show: !1}},
                                colors: ["#FFFFFF", "#FFFFFF", "#FF4560", "#008FFB"],
                                plotOptions: {bar: {horizontal: !0, barHeight: "80%"}},
                                dataLabels: {
                                    enabled: !0,
                                    textAnchor: "end",
                                    style: {colors: ["#000000", "#000000", "#FFFFFF", "#FFFFFF"], fontWeight: "bold"},
                                    formatter: function (t, {seriesIndex: e, dataPointIndex: s, w: i}) {
                                        let a = i.config.xaxis.categories[s].toString(), n = a.length;
                                        return 0 === e ? a.substr(n / 2, n) : 1 === e ? a.substr(0, n / 2) : Math.abs(t) - 1
                                    }
                                },
                                stroke: {width: 1, colors: ["#fff"]},
                                grid: {show: !1, xaxis: {show: !1}, yaxis: {show: !1}},
                                fill: {opacity: [0, 0, 1, 1]},
                                yaxis: {
                                    min: -13,
                                    max: 13,
                                    labels: {
                                        show: !1,
                                        align: "center",
                                        style: {colors: ["#000000"], zIndex: 9999, opacity: 1.5},
                                        offsetX: .5 * $(`#hobar-${t}-0`).innerWidth()
                                    },
                                    axisBorder: {show: !1}
                                },
                                tooltip: {enabled: !1, x: {show: !1}, y: {show: !1}},
                                title: {text: a + " vs " + n, align: "center", style: {fontWeight: "bold"}},
                                xaxis: {
                                    categories: ["슈팅", "유효슛 ", "코너킥 ", "프리킥 ", "옵사", "파울", "자책골 ", "경고", "퇴장"],
                                    labels: {show: !1},
                                    axisBorder: {show: !1},
                                    axisTicks: {show: !1}
                                },
                                legend: {inverseOrder: !0},
                                responsive: [{
                                    breakpoint: 780,
                                    options: {dataLabels: {style: {fontSize: "8"}}}
                                }, {
                                    breakpoint: 480,
                                    options: {
                                        dataLabels: {style: {fontSize: "7"}},
                                        plotOptions: {bar: {barHeight: "90%"}},
                                        chart: {height: 400}
                                    }
                                }]
                            };
                            new ApexCharts(document.querySelector(`#hobar-${t}-${e}`), o).render()
                        }(t, 0, e.summaryDtoList, y, l, o), function (t, e, s, i, a) {
                            var n = {
                                series: [e, s],
                                chart: {width: 260, type: "pie", foreColor: "#000000", toolbar: {show: !1}},
                                title: {text: "점유율", align: "center"},
                                labels: [i, a],
                                dataLabels: {
                                    formatter(t, e) {
                                        const s = e.w.globals.labels[e.seriesIndex];
                                        return [s, t.toFixed(1) + "%"]
                                    }
                                },
                                legend: {show: !1}
                            };
                            new ApexCharts(document.querySelector(`#poss-chart-${t}`), n).render()
                        }(t, p.possession, b.possession, l, o), function (t, e, s) {
                            var i = {
                                series: [{
                                    name: "숏패스",
                                    data: [s.shortPassTry + 1, s.shortPassSuccess + 1]
                                }, {name: "스루패스", data: [s.throughPassTry + 1, s.throughPassSuccess + 1]}, {
                                    name: "롱패스",
                                    data: [s.longPassTry + 1, s.longPassSuccess + 1]
                                }, {
                                    name: "로빙패스",
                                    data: [s.lobbedThroughPassTry + s.bouncingLobPassTry + 1, s.lobbedThroughPassSuccess + s.bouncingLobPassSuccess + 1]
                                }, {name: "드리븐패스", data: [s.drivenGroundPassTry + 1, s.drivenGroundPassSuccess + 1]}],
                                chart: {type: "bar", width: "99%", height: "80%", stacked: !0, toolbar: {show: !1}},
                                plotOptions: {bar: {horizontal: !0}},
                                stroke: {width: 1, colors: ["#fff"]},
                                title: {text: e + "님의 패스기록", style: {fontWeight: "bolder"}, align: "center"},
                                xaxis: {
                                    categories: ["시도", "성공"],
                                    labels: {show: !1},
                                    axisBorder: {show: !1},
                                    axisTicks: {show: !1}
                                },
                                yaxis: {labels: {show: !0, align: "right", style: {colors: ["#000000", "#1E7CEC"]}}},
                                fill: {opacity: .9},
                                legend: {
                                    position: "top", horizontalAlign: "left", offsetX: 0, formatter: function (t) {
                                        return [t.toString().replace("패스", "")]
                                    }
                                },
                                grid: {show: !1, xaxis: {lines: {show: !1}}, yaxis: {lines: {show: !1}}},
                                dataLabels: {formatter: t => t - 1},
                                tooltip: {
                                    shared: !1, y: {
                                        formatter: function (t) {
                                            return t - 1
                                        }
                                    }
                                },
                                responsive: [{
                                    breakpoint: 480,
                                    options: {
                                        yaxis: {labels: {style: {fontSize: "8"}}},
                                        plotOptions: {bar: {barHeight: "40%"}},
                                        chart: {height: "50%"}
                                    }
                                }]
                            };
                            new ApexCharts(document.querySelector(`#pass-chart-${t}`), i).render()
                        }(t, l, e.passDtoList[0]);
                        let x = new Set;
                        e.matchPlayerDtoList.map(t => t.forEach(function (t) {
                            x.add(t.spId)
                        })), x.forEach(t => v(t).done(function (t) {
                            let e = t.data;
                            !function (t) {
                                let e = t.info.playerId, s = t.info.playerName;
                                Array.prototype.slice.call(document.querySelectorAll(`.get-${e}-name`)).map(t => t.textContent = s.length < 8 ? s : s.split(" ")[1]), Array.prototype.slice.call(document.querySelectorAll(`.get-${e}-img`)).map(e => e.src = t.pimg), Array.prototype.slice.call(document.querySelectorAll(`.get-${e}-simg`)).map(e => e.src = t.info.season.img)
                            }(e)
                        })), $(`.tab_content.inner.${t}`).hide(), $(`#squad-${t}`).addClass("active").show(), $(`#squad-${t}`).show(), $(`ul.tabs.inner.${t} li`).click(function () {
                            $(`ul.tabs.inner.${t} li`).removeClass("is-active"), $(this).addClass("is-active"), $(`.tab_content.inner.${t}`).hide();
                            var e = $(this).find("a").attr("href");
                            return $(e).fadeIn(), !1
                        })
                    }
                } else r.style.display = "none"
            }(e, t)
        })
    }

    function s(t) {
        let e = {inner: []};
        for (let s = 0; s < t.length; s++) {
            let i = [0, 0, 0, 0];
            i[t[s].type] = 1;
            let a = {
                assist: t[s].assist ? 1 : 0,
                asx: t[s].assistX,
                asy: t[s].assistY,
                inv: 0 === s ? t[s].goalTime : t[s].goalTime - t[s - 1].goalTime,
                x: t[s].x,
                y: t[s].y,
                nom: i[1],
                fin: i[2],
                hed: i[3]
            };
            e.inner.push(a)
        }
        return e
    }

    function i(t, e, s) {
        $.ajax({
            type: "POST",
            url: "/expectedgoals",
            contentType: "application/json",
            data: JSON.stringify(t),
            success: function (t) {
                e.map((e, s) => e.prediction = t[s].prediction), $(`#id-shoot-${s}-li`).hasClass("ele-no-react") && setTimeout(function () {
                    $(`#id-shoot-${s}-li`).removeClass("ele-no-react")
                }, 400)
            }
        })
    }

    function a(t, e, s) {
        for (let i of t) if (3 === i.result) {
            let t = i.goalTime;
            t = t >= 0 ? t : "45+" + (-t - 45), $(`#match-goals-${e}-${s}`).append(`\n            <small><i class="fa fa-futbol"></i>&nbsp;${t}'<span class="get-${i.spId}-name"></span></small><br>`)
        }
    }

    function n(t, e, s) {
        for (let i of t) 28 !== i.spPosition ? $(`#sqd-${e}-${s}-${i.spPosition}`).append(`\n            <figure class="image is-96x96 resp is-inline-block mt-1 is-relative">\n                        <img class="image pos-taeduri-${i.rootPosName} get-${i.spId}-img"  src="" alt="player">\n                         <img class="image get-${i.spId}-simg" style="position: absolute;bottom: 0;left: 0;width: 25%;height: 25%" src="" alt="season">\n                             <img class="image" style="position: absolute;bottom: 0;right: 0;width: 25%;height: 25%" src="/img/e${i.spGrade}.PNG">\n                    </figure>\n                    <p class="myp has-text-white" style="margin-top: -8px;margin-bottom: -8px;"><small class="get-${i.spId}-name"></small></p>\n                    <p class="myp sub pos-color-${i.rootPosName}"><small>${i.posName}</small></p>\n            `) : ($(`#sqd-${e}-${s}-28`).append(`<div class="column mywidth-16">\n<figure class="image is-96x96 resp is-inline-block mt-1 is-relative">\n                        <img class="image hovsub pos-taeduri-${i.rootPosName} get-${i.spId}-img im-${e}-${s}"  src="" alt="player">\n                         <img class="image get-${i.spId}-simg" style="position: absolute;bottom: 0;left: 0;width: 29%;height: 29%" src="" alt="season">\n                             <img class="image" style="position: absolute;bottom: 0;right: 0;width: 29%;height: 29%" src="/img/e${i.spGrade}.PNG">\n                    </figure>\n                    <p class="myp has-text-black verysmall" style="margin-top: -8px;margin-bottom: -8px;"><small id="subs-${e}-${s}-${i.spId}" class="get-${i.spId}-name"></small></p>                 \n                         </div>                                        \n`), $(`.image.hovsub.get-${i.spId}-img.im-${e}-${s}`).hover(function () {
            $(`#mobilehovertext-name-${e}-${s}`).text($(`#subs-${e}-${s}-${i.spId}`).text())
        }))
    }

    function o(t, e) {
        return $.ajax({type: "PUT", url: `/users/${t}/matches`, contentType: "application/json", data: e})
    }

    function l(t, e) {
        t > e && $("#id-derbogi").show()
    }

    function r(t, e) {
        let s = [0, 0], i = [0, 0, 0], a = [0, 0], n = [0, 0], o = 0, l = 0, r = [], u = [], m = [], f = [0, 0],
            g = [[], []];
        for (let t of e) {
            let e = t.basicDtoList[0].matchResult;
            if ("승" === e ? i[0]++ : "무" === e ? i[1]++ : i[2]++, 0 === t.matchPlayerDtoList[0].length || 0 === t.matchPlayerDtoList[1].length) continue;
            l += 1;
            let d = t.matchPlayerDtoList[0];
            for (let t = 0; t < d.length; t++) 28 !== d[t].spPosition && (r.push(d[t]), f[0] += d[t].dribbleTry, f[1] += d[t].dribbleSuccess);
            let c = t.summaryDtoList;
            o += c[0].possession, a[0] += c[0].shootTotal, a[1] += c[0].effectiveShootTotal, n[0] += c[1].shootTotal, n[1] += c[1].effectiveShootTotal, u.push(t.passDtoList[0]), m.push(t.passDtoList[1]), s[0] += t.basicDtoList[0].goalTotal, s[1] += t.basicDtoList[1].goalTotal;
            let h = t.shootDtoList[0].filter(t => 3 === t.result), p = t.shootDtoList[1].filter(t => 3 === t.result);
            g[0] = g[0].concat(h.map(t => t.goalTime)), g[1] = g[1].concat(p.map(t => t.goalTime))
        }
        var y, x, w;
        if (y = s, x = e.length, w = {
            series: [{data: y}],
            chart: {type: "bar", foreColor: "#FFFFFF", toolbar: {show: !1}, height: 300},
            plotOptions: {bar: {borderRadius: 4, horizontal: !1, distributed: !0}},
            dataLabels: {enabled: !0, style: {fontSize: "1.5em"}},
            colors: ["#468af6", "#de5d5d"],
            xaxis: {categories: ["득점", "실점"]},
            yaxis: {labels: {show: !1}},
            grid: {show: !1},
            title: {text: `${x} 경기 득/실점`, align: "center"},
            tooltip: {
                theme: "dark", x: {show: !0}, y: {
                    title: {
                        formatter: function () {
                            return ""
                        }
                    }
                }
            }
        }, new ApexCharts(document.querySelector("#basic-goal-chart"), w).render(), function (t, e) {
            let s = {
                series: t,
                chart: {type: "donut", height: 350, foreColor: "#FFFFFF", toolbar: {show: !1}},
                labels: ["승", "무", "패"],
                title: {text: `${e}경기 ${t[0]}승 ${t[1]}무 ${t[2]}패`, align: "center"},
                legend: {
                    position: "bottom",
                    onItemClick: {toggleDataSeries: !0},
                    onItemHover: {highlightDataSeries: !0},
                    offsetY: 0,
                    offsetX: 0
                }
            };
            new ApexCharts(document.querySelector("#basic-win-chart"), s).render()
        }(i, e.length), function (t, e) {
            let s = b(t), i = b(e);
            var a = {
                series: [{name: "득점", data: s}, {name: "실점", data: i}],
                chart: {type: "bar", foreColor: "#FFFFFF", toolbar: {show: !1}, height: 300},
                plotOptions: {bar: {borderRadius: 4, horizontal: !1, columnWidth: "55%", endingShape: "rounded"}},
                dataLabels: {enabled: !1},
                colors: ["#468af6", "#de5d5d"],
                stroke: {show: !0, width: 2, colors: ["transparent"]},
                xaxis: {categories: ["0-15", "15-30", "30-45", "45++", "45-60", "60-75", "75-90", "90++"]},
                yaxis: {labels: {show: !1}},
                grid: {show: !1},
                fill: {opacity: 1},
                tooltip: {
                    theme: "dark", x: {show: !0}, y: {
                        title: {
                            formatter: function () {
                                return ""
                            }
                        }
                    }
                },
                title: {text: "득/실 시간분포", align: "center"}
            };
            new ApexCharts(document.querySelector("#basic-gtime-chart"), a).render()
        }(g[0].sort(), g[1].sort()), function (t, e) {
            $.ajax({
                type: "POST",
                url: `/users/${e}/matches/mvp`,
                contentType: "application/json",
                data: JSON.stringify(t),
                success: function (t) {
                    let e = t.data;
                    !function (t) {
                        const e = t.reduce(function (t, e) {
                            return t.goal > e.goal ? t : e
                        });
                        v(e.spId).done(function (t) {
                            let s = t.data;
                            $("#mvp-goalgetter").append(`${c(s.info.season.img, s.pimg, s.info.playerName)}<p><span class="has-text-primary has-text-weight-bold">${e.cnt}경기 ${e.goal} 득점</p>`)
                        })
                    }(e), function (t) {
                        const e = t.reduce(function (t, e) {
                            return t.assist > e.assist ? t : e
                        });
                        v(e.spId).done(function (t) {
                            let s = t.data;
                            $("#mvp-assister").append(`${c(s.info.season.img, s.pimg, s.info.playerName)}<p><span class="has-text-link has-text-weight-bold">${e.cnt}경기 ${e.assist} 도움</p>`)
                        })
                    }(e), function (t) {
                        const e = t.reduce(function (t, e) {
                            return t.spRating > e.spRating ? t : e
                        });
                        v(e.spId).done(function (t) {
                            let s = t.data;
                            $("#mvp-star").append(`${c(s.info.season.img, s.pimg, s.info.playerName)}<p><span class="has-text-danger has-text-weight-bold">${e.cnt}경기 ${Math.round(100 * e.spRating) / 100} 평점</p>`)
                        })
                    }(e)
                },
                error: function (t) {
                    $("#mvps").hide(), $("#id-tab-mvps").append(`<p class="container has-text-white has-text-centered has-text-danger is-size-7">\n[${t.responseJSON.code}] ${t.responseJSON.message}<p>최근순위경기에 몰수경기만 있었나요?</p></p>`)
                }
            })
        }(r, t), 0 === l) return;
        let S = Object.keys(u[0]), k = p(S, u), T = p(S, m);
        !function (t, e, s, i, a, n) {
            let o = Math.round(t / i);
            $(`#anal-${o < 40 ? 0 : o > 60 ? 2 : 1}`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-user-clock"></i>&nbsp;[점유율]</p>\n<p class="has-text-white">경기 평균 ${h(o + "%")} 의 점유율을 보였습니다.</p><br>`);
            let l = parseInt(a[0] / e[0] * 100 / 33);
            $(`#anal-${Math.min(l, 2)}`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-futbol"></i>&nbsp;[득점효율]</p>\n<p class="has-text-white">${h("[ " + e[1] + " / " + e[0] + " ]")}개의 슈팅 중 ${h(a[0])} 득점을 만들었습니다.</p><br>`);
            let r = parseInt(e[1] / e[0] * 100 / 33);
            $(`#anal-${Math.min(r, 2)}`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-exchange-alt"></i>&nbsp;[유효슈팅]</p>\n<p class="has-text-white">${Math.round(e[1] / e[0] * 100)}%의 슈팅이 골대를 향했습니다.</p><br>`);
            let d = s[1] / i;
            $(`#anal-${d >= 4 ? 0 : d <= 2 ? 2 : 1}`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-exclamation"></i>&nbsp;[슈팅허용]</p>\n<p class="has-text-white">경기 평균 ${h(Math.round(10 * d) / 10)}개의 유효슈팅을 허용했습니다.</p><br>`);
            let c = Math.round(n[1] / n[0] * 100);
            c >= 90 ? $("#anal-2").append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-lightbulb"></i>&nbsp;[환상적인 발놀림]</p>\n<p class="has-text-white">${c}%의 성공률을 자랑하는 환상적인 드리블러입니다.</p><br>`) : c < 35 && $("#anal-0").append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-lightbulb"></i>&nbsp;[쓸데없는 발재간]</p>\n<p class="has-text-white">${100 - c}%의 드리블 실패로 흐름을 끊는 플레이를 합니다.</p><br>`)
        }(o, a, n, l, s, f), d(k, l, [0, 1, 2], "성공"), d(T, l, [2, 1, 0], "허용")
    }

    function d(t, e, s, i) {
        let a = t.passTry;
        if ("성공" === i) {
            let n = Math.round(t.passSuccess / a * 100);
            $(`#anal-${n >= 92 ? s[2] : n <= 82 ? s[0] : s[1]}`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-shoe-prints"></i>&nbsp;[패스${i}]</p>\n<p class="has-text-white">${e}경기에서 ${h(n + "%")}의 패스 ${i}률을 보였습니다.</p><br>`)
        }
        let n = a - t.shortPassTry, o = t.passSuccess - t.shortPassSuccess;
        n / a >= .15 && o / n >= .8 && $(`#anal-${s[2]}`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-ruler-horizontal"></i>&nbsp;[고급패스${i}]</p>\n<p class="has-text-white">전체패스의 ${Math.round(n / a * 100)}%가 고급패스로 ${h(Math.round(o / n * 100) + "%")}의 고급 패스를 ${i}했습니다.</p><br>`);
        let l = Math.round(t.throughPassSuccess / t.throughPassTry * 100);
        $(`#anal-${l >= 90 ? s[2] : l < 60 ? s[0] : s[1]}`).append(`<p class="is-size-3-desktop is-size-5-mobile"><i class="fa fa-divide"></i>&nbsp;[스루패스${i}]</p>\n<p class="has-text-white">${t.throughPassTry}번의 스루패스 중 ${h(l + "%")}를 ${i}했습니다.</p><br>`)
    }

    function c(t, e, s) {
        return `<figure class="card-image is-128x128">\n                                        <div style="width: fit-content; display: inline-block">\n                                            <img class="is-rounded" src="${e}">\n                                            <img style="position: absolute;top: 79%" class="image"\n                                                 src="${t}">\n                                        </div>\n                                    </figure>\n                                    <p class="is-size-6 has-text-weight-bold">${s}</p>`
    }

    function h(t) {
        return `<span class="has-text-weight-bold">${t}</span>`
    }

    function p(t, e) {
        return e.reduce(function (e, s) {
            for (let i of t) e[i] += s[i];
            return e
        })
    }

    function u(t, e) {
        return `\n<div class="column is-mobile" style="background: url(/img/soccerfield.png) no-repeat 0 0;background-size:100% 100%;">\n<div class="columns is-mobile">\n        <div class="column is-one-fifth has-text-centered">&nbsp;</div>\n        <div id="sqd-${t}-${e}-26" class="column is-one-fifth has-text-centered"></div>\n        <div id="sqd-${t}-${e}-25" class="column is-one-fifth has-text-centered"></div>\n        <div id="sqd-${t}-${e}-24" class="column is-one-fifth has-text-centered"></div>\n        <div class="column is-one-fifth has-text-centered">&nbsp;</div>\n</div>\n<div class="columns is-mobile">\n    <div id="sqd-${t}-${e}-27" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-22" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-21" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-20" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-23" class="column is-one-fifth "></div>                                        \n</div>\n<div class="columns is-mobile">\n    <div class="column is-one-fifth"></div>                                        \n    <div id="sqd-${t}-${e}-19" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-18" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-17" class="column is-one-fifth "></div>                                        \n    <div class="column is-one-fifth "></div>                                     \n</div>\n<div class="columns is-mobile">\n    <div id="sqd-${t}-${e}-16" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-15" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-14" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-13" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-12" class="column is-one-fifth "></div>\n    </div>\n<div class="columns is-mobile">\n    <div id="sqd-${t}-${e}-8" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-11" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-10" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-9" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-2" class="column is-one-fifth "></div>                                        \n</div>\n<div class="columns is-mobile">\n    <div id="sqd-${t}-${e}-7" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-6" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-5" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-4" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-3" class="column is-one-fifth "></div>                                        \n</div>\n<div class="columns is-mobile">\n    <div class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-1" class="column is-one-fifth "></div>                                        \n    <div id="sqd-${t}-${e}-0" class="column is-one-fifth "></div>                                        \n    <div class="column is-one-fifth "></div>                                        \n</div>                      \n</div>\n<div class="has-text-centered has-text-weight-bold is-size-3-desktop is-size-5-mobile mt-1"><p>교체선수</p><hr></div>\n<div id="sqd-${t}-${e}-28" class="columns is-mobile">\n</div>\n<p id="mobilehovertext-name-${t}-${e}" class="has-text-weight-bold mobilehovertext-name">&nbsp;</p>`
    }

    function m(t, e) {
        return `<table class="table is-size-6-desktop is-size-7-mobile" style="margin: 0 auto">\n  <thead>\n    <tr>\n      <th class="mytable-no-scroll"><abbr title="선수">PLAYER</abbr></th>\n      <th><abbr title="포지션">POS</abbr></th>\n      <th><abbr title="평점">STAR</abbr><a id="id-${t}-${e}-2"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="슈팅">SHOT</abbr><a id="id-${t}-${e}-3"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="유효슈팅">E_SHOT</abbr><a id="id-${t}-${e}-4"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="득점">GOL</abbr><a id="id-${t}-${e}-5"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="어시스트">ASI</abbr><a id="id-${t}-${e}-6"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="패스시도">PAS</abbr><a id="id-${t}-${e}-7"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="패스성공">S_PAS</abbr><a id="id-${t}-${e}-8"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="드리블시도">DRIB</abbr><a id="id-${t}-${e}-9"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="드리블성공">S_DRIB</abbr><a id="id-${t}-${e}-10"><i class="fa fa-angle-down"></i></a></th>\n      <th><abbr title="가로채기">INCT</abbr></th>\n      <th><abbr title="선방">BLOK</abbr></th>\n      <th><abbr title="태클시도">TAKLE</abbr></th>\n      <th><abbr title="태클성공">S_TAKLE</abbr></th>\n      <th><abbr title="경고">YCARD</abbr></th>\n      <th><abbr title="퇴장">RCARD</abbr></th>\n    </tr>\n  </thead>\n  <tbody id="tbod-${t}-${e}">\n  </tbody>\n</table>`
    }

    function f(t, e, s) {
        let i = ["spRating", "shoot", "effectiveShoot", "goal", "assist", "passTry", "passSuccess", "dribbleTry", "dribbleSuccess", "intercept", "block", "tackleTry", "tackle", "yellowCards", "redCards"],
            a = s.slice(0, 11);
        for (let s of a) {
            let a = `<tr><td class="get-${s.spId}-name mytable-no-scroll has-text-left has-text-weight-bold" style="white-space: nowrap"></td><td class="pos-color-${s.rootPosName}"><small style="background-color: white">${s.posName}</small></td>`;
            i.map(t => {
                a += "<td>" + s[t] + "</td>"
            }), $(`#tbod-${t}-${e}`).append(`${a}</tr>`)
        }
    }

    function b(t) {
        let e = [0, 0, 0, 0, 0, 0, 0, 0];
        for (let s = 0; s < t.length; s++) t[s] < 0 ? e[3]++ : t[s] < 15 ? e[0]++ : t[s] < 30 ? e[1]++ : t[s] < 45 ? e[2]++ : t[s] < 60 ? e[4]++ : t[s] < 75 ? e[5]++ : t[s] < 90 ? e[6]++ : e[7]++;
        return e
    }

    function g(t, e, s) {
        let i = [], a = ["#ff0000", "#0211f8"];
        for (let n = 0; n < t.length; n++) {
            let o = Math.pow(t[n].prediction, 1.25) * s + 5.2;
            i.push({
                seriesIndex: e,
                dataPointIndex: n,
                size: o.toFixed(2),
                shape: "circle",
                fillColor: 3 === t[n].result ? a[e] : ""
            })
        }
        return i
    }

    function v(t) {
        return $.ajax({type: "GET", url: `/players/info?pid=${t}`})
    }
    function sorter(t , e){
        for(let i = 2; i <= 10; i++){
            $(`#id-${t}-${e}-${i}`).on("click",function(){
               tbSorter(t,e,i,$(`#id-${t}-${e}-${i} > svg`));
            });
        }
    }
    $("#id-derbogi > .button").click(function () {
        !function (t) {
            let s = $(".clas").length, i = JSON.parse(localStorage.getItem(t)).allcode, a = i.slice(s, s + 5);
            (async function (e) {
                const s = e.map((e, s) => (function (e, s) {
                    return new Promise(e => {
                        setTimeout(() => {
                            o(t, a[s]).done(function (t, s, i) {
                                204 !== i.status ? e(t.data) : (e(null), $("#id-finderror").append('<p class="clas">비정상적인 순위경기 제외됨</p>'))
                            })
                        }, 3)
                    })
                })(0, s));
                return await Promise.all(s)
            })(a).then(t => {
                let i = t.filter(t => null !== t);
                for (let t = 0; t < i.length; t++) e(i[t], s + t)
            }), i.length <= s + 5 && $("#id-derbogi").hide()
        }(t)
    })
    $('#id-btn-gangsin').click(function () {
        getGangsin(t);
    });
    $('#id-anal-gita').click(function () {
        getAnalGita();
    });
});

function getGangsin(id) {
    localStorage.removeItem(id);
    window.location.reload();
}