package com.dbs.loyalty.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DateService {
	
	public static final String DATETIME = "datetime";
	
	public static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm";
	
	public static final String DATE = "date";
	
	public static final String DATE_PATTERN = "dd/MM/yyyy";
	
	public static final String TIME = "time";
	
	public static final String TIME_PATTERN = "HH:mm";
	
	private final SettingService settingService;
	
	public String datetime(Instant date) {
		if(settingService.settings().containsKey(DATETIME)) {
			return format(LocalDateTime.ofInstant(date, ZoneOffset.UTC), settingService.settings().get(DATETIME));
		}else {
			return format(LocalDateTime.ofInstant(date, ZoneOffset.UTC), DATETIME_PATTERN);
		}
	}
	
	public String date(Instant date) {
		if(settingService.settings().containsKey(DATE)) {
			return format(LocalDateTime.ofInstant(date, ZoneOffset.UTC), settingService.settings().get(DATE));
		}else {
			return format(LocalDateTime.ofInstant(date, ZoneOffset.UTC), DATE_PATTERN);
		}
	}
	
	public String time(Instant date) {
		if(settingService.settings().containsKey(TIME)) {
			return format(LocalDateTime.ofInstant(date, ZoneOffset.UTC), settingService.settings().get(TIME));
		}else {
			return format(LocalDateTime.ofInstant(date, ZoneOffset.UTC), TIME_PATTERN);
		}
	}

	public String format(Instant date, String pattern) {
		return format(LocalDateTime.ofInstant(date, ZoneOffset.UTC), pattern);
	}
	
	public String format(LocalDateTime date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
	}
	
}
