package com.pds.web.controller;


import com.pds.common.dto.StatDto;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.service.StatService;
import com.pds.web.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StatService statService;

    @Value("${vstatus}")
    private String status;

    @GetMapping("/")
    public String home(Model model){
        int thisSeason = statService.getThisSeason();
        int cnt = statService.getCnt75(thisSeason);
        model.addAttribute("thisseason",thisSeason);
        model.addAttribute("standardcnt",cnt);
        model.addAttribute("matchnum",statService.getThisSeasonMatchNum(thisSeason));

        model.addAttribute("topgoal",statService.getTopGoalPlayers(thisSeason,cnt));
        model.addAttribute("topwin",statService.getTopWinPlayers(thisSeason,cnt));
        model.addAttribute("topcnt",statService.getTopCntPlayers(thisSeason,cnt));
        model.addAttribute("toprating",statService.getTopRatingPlayers(thisSeason,cnt));
        model.addAttribute("svst",status);
        return "index";
    }

    @GetMapping("/stats/bestpos")
    @ResponseBody
    public ResponseEntity<List<StatDto .Info>> getHomeBest11(@RequestParam int cnt , @RequestParam int season){
        List<StatDto.Info> bestList = statService.getBest11Players(season,cnt);
        if(!bestList.isEmpty()){
            return ResponseHandler.generateResponse("시즌 포지션별 Top 선수 조회 성공", HttpStatus.OK,bestList);
        }
        else{
            ErrorInfo errorInfo = ErrorInfo.DB_DATA_ERROR;
            return ResponseHandler.generateResponse(errorInfo.getErrorMsg(),errorInfo.getErrorCode(), HttpStatus.NOT_FOUND,new ArrayList<>(),"/stats/bestpos");
        }
    }
}
