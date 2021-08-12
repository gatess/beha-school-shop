package com.beha;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.beha"})
public class SchoolSupplyApplication {
	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC+3"));
	}
	
    public static void main(String[] args) {
        SpringApplication.run(SchoolSupplyApplication.class, args);
    }
}
