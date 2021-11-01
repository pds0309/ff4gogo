package com.pds.serverlessapi.config;


import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


@Configuration
@Log4j2
@ComponentScan(basePackages = {"com.pds.serverlessapi.config"})
public class SlApiConfig {
    public SlApiConfig(){
        log.info("Serverless Api CONFIG");
    }
    @Bean
    public HttpHeaders httpHeaders(SlKey slKey){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", String.valueOf(slKey.getXgApikey()));
        return httpHeaders;
    }
}
