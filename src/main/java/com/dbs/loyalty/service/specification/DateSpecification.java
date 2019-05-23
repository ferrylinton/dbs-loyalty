package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.END_DATE_PARAM;
import static com.dbs.loyalty.service.specification.Constant.START_DATE_PARAM;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;

import com.dbs.loyalty.service.SettingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateSpecification {
	
	public static final DateTimeFormatter FORMATTER =  DateTimeFormatter.ofPattern(SettingService.JAVA_DATE);

	public static Instant getStartDate(HttpServletRequest request) {
		if(request.getParameter(START_DATE_PARAM) != null) {
			try {
				LocalDate localDate = LocalDate.parse(request.getParameter(START_DATE_PARAM), FORMATTER);
				return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
			}catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}
		
		return Instant.now().truncatedTo(ChronoUnit.DAYS);
	}
	
	public static Instant getEndDate(HttpServletRequest request) {
		if(request.getParameter(END_DATE_PARAM) != null) {
			try {
				LocalDate localDate = LocalDate.parse(request.getParameter(END_DATE_PARAM), FORMATTER);
				return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
			}catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}
		
		return Instant.now();
	}
	
	private DateSpecification() {
		// hide constructor
	}
	
}
