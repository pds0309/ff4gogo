package com.pds.serverlessapi.controller;


import com.pds.serverlessapi.api.XgApi;
import com.pds.serverlessapi.api.XgDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class XgController {
    private final XgApi xgApi;

    @PostMapping("/expectedgoal")
    public ResponseEntity<XgDto> expectedGoalForShoot(@RequestBody String shootInfo){
            return ResponseEntity.ok(xgApi.getXgVal(shootInfo));
    }
    @PostMapping("/expectedgoals")
    public ResponseEntity<List<XgDto>> expectedGoalListForShoots(@RequestBody String shootInfoList, HttpServletResponse response){
        return ResponseEntity.ok(xgApi.getXgListVal(shootInfoList));
    }
}
