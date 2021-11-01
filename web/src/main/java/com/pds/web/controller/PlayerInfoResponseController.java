package com.pds.web.controller;


import com.pds.common.dto.PlayerDto;
import com.pds.openapi.api.Fifa4PlayerImgApi;
import com.pds.web.service.PlayerResponseService;
import com.pds.web.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlayerInfoResponseController {

    private final PlayerResponseService playerService;

    @GetMapping("/players/info")
    public ResponseEntity<PlayerDto .InfoWithImage> getPlayerInfo(@RequestParam int pid){
        return ResponseHandler.generateResponse("선수데이터 응답 성공", HttpStatus.OK,playerService.getPlayer(pid));
    }
    @GetMapping("/players/image")
    public ResponseEntity<String> getPlayerImage(@RequestParam int pid){
        return ResponseHandler.generateResponse("선수이미지 응답 성공",HttpStatus.OK,Fifa4PlayerImgApi.findPlayerImgUrl(pid));
    }
}
