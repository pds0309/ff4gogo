package com.pds.web.controller;


import com.pds.web.api.MatchDto;
import com.pds.web.service.UserMatchService;
import com.pds.web.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class UserMatchController {

    private final UserMatchService userMatchService;

    @GetMapping("/users/{id}/matches")
    public ResponseEntity<MatchDto.Info> getDetailFromMatchCode(@PathVariable String id , @RequestParam String mc){
            return ResponseHandler.generateResponse("상세 순위경기 조회 OK", HttpStatus.OK,userMatchService.getDetailMatchList(mc,id));
    }
}
