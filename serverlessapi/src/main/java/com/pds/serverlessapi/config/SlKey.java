package com.pds.serverlessapi.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@Log4j2
@PropertySource("classpath:application-skey.properties")
final class SlKey {
    @Value("${serverless.xgkey}")
    private String xgApikey;
    private SlKey(){
        log.info("Serverless API KEYs");
    }
}
