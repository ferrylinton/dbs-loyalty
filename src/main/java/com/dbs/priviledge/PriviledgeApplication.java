package com.dbs.priviledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import com.dbs.priviledge.config.ApplicationProperties;

@SpringBootApplication
@EnableJdbcHttpSession
@EnableConfigurationProperties({ApplicationProperties.class})
public class PriviledgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriviledgeApplication.class, args);
	}
}
