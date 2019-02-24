package com.dbs.priviledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dbs.priviledge.config.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class PriviledgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriviledgeApplication.class, args);
	}
}
