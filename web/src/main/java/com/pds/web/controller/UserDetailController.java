package com.pds.web.controller;


import com.pds.web.api.MatchDto;
import com.pds.web.service.UserDetailService;
import com.pds.web.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserDetailController {

    private final UserDetailService detailService;

    @PostMapping("/users/{id}/matches/mvp")
    public ResponseEntity<List<MatchDto.BestDto>> myMvpPlayer(@RequestBody List<MatchDto.MatchPlayerDto> playerDtoList){
        return ResponseHandler.generateResponse("매치선수 데이터 획득 성공", HttpStatus.OK,detailService.getMyMvpList(playerDtoList));
    }
}
