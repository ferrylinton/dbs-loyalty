package com.dbs.priviledge.thymeleaf.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.dbs.priviledge.config.ApplicationProperties;

@Service
public class InstantService {

	private final ApplicationProperties applicationProperties;

	public InstantService(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	public String date(String text) {
		Instant instant = Instant.parse(text);
		LocalDate localDate =  LocalDate.ofInstant(instant, ZoneOffset.systemDefault());
		return localDate.format(DateTimeFormatter.ofPattern(applicationProperties.getDateFormat()));
	}
	
	public String dateTime(String text) {
		Instant instant = Instant.parse(text);
		LocalDateTime localDateTime =  LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
		return localDateTime.format(DateTimeFormatter.ofPattern(applicationProperties.getDateTimeFormat()));
	}
	
	public String dateTimeFull(String text) {
		Instant instant = Instant.parse(text);
		LocalDateTime localDateTime =  LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
		return localDateTime.format(DateTimeFormatter.ofPattern(applicationProperties.getDateTimeFullFormat()));
	}
	
}
