package com.pds.common.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@Log4j2
@PropertySource("classpath:application-apikey.properties")
final class Fifa4Key {
    @Value("${fifa4.key}")
    private String key;
    private Fifa4Key(){
        log.info("FIFA4 API KEY");
    }
}
