package com.pds.web.controller;


import com.pds.web.api.MatchDeliver;
import com.pds.web.api.MatchDto;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.service.UserMatchService;
import com.pds.web.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
class UserMatchController {

    private final UserMatchService userMatchService;

    @PutMapping("/users/{id}/matches")
    public ResponseEntity<MatchDto.Info> getDetailFromMatchCode(@PathVariable String id,@RequestBody String mc){
        MatchDto.Info result = userMatchService.getDetailMatchList(mc,id);
        if(result!=null){
            return ResponseHandler.generateResponse("상세 순위경기 조회 OK", HttpStatus.CREATED,result);
        }
        else{
            return ResponseEntity.noContent().header("Content-Length", "0").build();
        }
    }
    @PostMapping("/users/{id}/matches/mvp")
    public ResponseEntity<List<MatchDto.BestDto>> myMvpPlayer(@RequestBody List<MatchDto.MatchPlayerDto> playerDtoList){
        return ResponseHandler.generateResponse("매치선수 데이터 획득 성공", HttpStatus.CREATED, MatchDeliver.getMyMvpList(playerDtoList));
    }
}
