package com.pds.serverlessapi.controller;


import com.pds.serverlessapi.api.XgApi;
import com.pds.serverlessapi.api.XgDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class XgController {
    private final XgApi xgApi;

    @PostMapping("/expectedgoal")
    public ResponseEntity<XgDto> expectedGoalForShoot(@RequestBody String shootInfo){
            return ResponseEntity.ok(xgApi.getXgVal(shootInfo));
    }
}
