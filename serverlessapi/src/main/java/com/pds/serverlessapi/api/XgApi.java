package com.pds.serverlessapi.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class XgApi {
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public String getXgVal(String shootInfo) throws HttpClientErrorException , HttpServerErrorException { ;
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://0o2ma3bzal.execute-api.ap-northeast-2.amazonaws.com/Prod/xgrf", HttpMethod.POST, new HttpEntity<>(shootInfo,headers), String.class);
        return responseEntity.getBody();
    }
}
