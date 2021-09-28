package com.pds.common.config;


import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Getter
@Configuration
@Log4j2
@ComponentScan(basePackages = {"com.pds.common.config"})
public class Fifa4Api {
    public Fifa4Api(){
        //Do Nothing
        log.info("FIFA4 APi CONFIG");
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public HttpEntity<String> httpEntity(Fifa4Key fifa4Key){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", String.valueOf(fifa4Key.getKey()));
        return new HttpEntity<>("",httpHeaders);
    }
}
