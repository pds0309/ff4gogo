package com.pds.web.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedTomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            connector.setProperty("relaxedPathChars", "<>[\\]^`{|}");
            connector.setProperty("relaxedQueryChars", "<>[\\]^`{|}");
        });
    }
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory>
    containerCustomizer(){
        return new EmbeddedTomcatCustomizer();
    }
}