package com.pds.web.controller;


import com.pds.web.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
