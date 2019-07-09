package com.dbs.loyalty.service.specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DateConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateSpecification {
	
	public static final DateTimeFormatter FORMATTER =  DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE);
	
	public static Instant getStartDate(String startDate) {
		try {
			LocalDate localDate = LocalDate.parse(startDate, FORMATTER);
			return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
		}catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return Instant.now().truncatedTo(ChronoUnit.DAYS);
		}
	}
	
	public static Instant getEndDate(String endDate) {
		try {
			LocalDate localDate = LocalDate.parse(endDate, FORMATTER);
			localDate = localDate.plusDays(1);
			return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
		}catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return Instant.now().truncatedTo(ChronoUnit.DAYS);
		}
	}
	
	public static Instant getStartDate(HttpServletRequest request) {
		if(request.getParameter(Constant.START_DATE_PARAM) != null) {
			try {
				LocalDate localDate = LocalDate.parse(request.getParameter(Constant.START_DATE_PARAM), FORMATTER);
				return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
			}catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}
		
		return Instant.now().truncatedTo(ChronoUnit.DAYS);
	}
	
	public static Instant getEndDate(HttpServletRequest request) {
		if(request.getParameter(Constant.END_DATE_PARAM) != null) {
			try {
				LocalDate localDate = LocalDate.parse(request.getParameter(Constant.END_DATE_PARAM), FORMATTER);
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
