package com.pds.serverlessapi.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class XgApi {
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    private final String errorLog = "Xg API";

    public XgDto getXgVal(String shootInfo){
        try{
            ResponseEntity<XgDto> responseEntity = restTemplate.exchange("https://0o2ma3bzal.execute-api.ap-northeast-2.amazonaws.com/Prod/xgrf", HttpMethod.POST, new HttpEntity<>(shootInfo,headers), XgDto.class);
            return responseEntity.getBody();
        }
        catch(HttpClientErrorException | HttpServerErrorException e){
            if (e.getRawStatusCode() == 429 || e.getRawStatusCode() == 403) {
                throw new HttpClientErrorException(HttpStatus.FORBIDDEN , errorLog);
            }  else {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, errorLog);
            }
        }
    }
}
