package com.beha.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.beha.dao")
@EnableJpaRepositories("com.beha.dao")
public class SchoolSupplyApplicationConfig {

}
