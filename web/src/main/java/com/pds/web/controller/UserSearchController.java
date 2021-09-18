package com.pds.web.controller;


import com.pds.common.dto.UsersDto;
import com.pds.common.enums.Ranks;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import com.pds.web.utils.ResponseHandler;
import com.pds.web.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserSearchController {
    private final UserSearchService userSearchService;

    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<UsersDto.Info> saveUser(@RequestBody(required = false) String nn) {
        log.info(nn + "으로 접근 시도됨");
        return ResponseHandler.generateResponse("유저 등록 성공",HttpStatus.OK , userSearchService.createUser(userSearchService.findUser(nn)));
    }

    @GetMapping("/users/{id}")
    public String getUser(Model model , @PathVariable String id){
        try{
            UsersDto.Info userDto = userSearchService.getUserInfo(id);
            model.addAttribute("user",userDto);
            model.addAttribute("rank", Ranks.getRanks(userDto.getHighRank()).getRankInfo());
            return "searchuser";
        }
        catch (UserRequestException e){
            model.addAttribute("errorcode",400);
            model.addAttribute("errorname","BAD_REQUEST");
            model.addAttribute("errormsg","["+e.getCode()+"]\n"+e.getMessage());
            return "error";
        }
    }
    @PostMapping("/users/{id}/matches")
    @ResponseBody
    public ResponseEntity<List<String>> saveUserMatches(@PathVariable String id){
        return ResponseHandler.generateResponse("유저 매치리스트 수집 성공",
                HttpStatus.OK, userSearchService.getMatchListFirstTime(id));
    }
}
