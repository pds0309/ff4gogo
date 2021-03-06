package com.pds.schedule.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.pds.common"})
@EnableJpaRepositories(basePackages = {"com.pds.common"})
@ComponentScan(basePackages = {"com.pds.common","com.pds.openapi"})
public class ModuleConfiguration {
}
