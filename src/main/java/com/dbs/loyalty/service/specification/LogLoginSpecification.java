package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.domain.LogLogin;

public class LogLoginSpecification {
	
	private static final Logger LOG = LoggerFactory.getLogger(LogLoginSpecification.class);
	
	public static Specification<LogLogin> getSpec(HttpServletRequest request) {
		return Specification
				.where(createdDate(request))
				.and(keyword(request));
	}
	
	public static Specification<LogLogin> all() {
		return (logLogin, cq, cb) -> cb.notEqual(logLogin.get(ID), EMPTY);
	}
	
	public static Specification<LogLogin> createdDate(HttpServletRequest request) {
		if(
			request.getParameter(START_DATE_PARAM) != null &&
			request.getParameter(END_DATE_PARAM) != null &&
			!request.getParameter(START_DATE_PARAM).equals(Constant.EMPTY) &&
			!request.getParameter(END_DATE_PARAM).equals(Constant.EMPTY)
			) {
			
			Instant startDate = getStartDate(request);
			Instant endDate = getEndDate(request);
			
			return (logLogin, cq, cb) -> cb.and(
				cb.greaterThanOrEqualTo(logLogin.get(CREATED_DATE), startDate),
				cb.lessThanOrEqualTo(logLogin.get(CREATED_DATE), endDate)
			);
		}else {
			return all();
		}		
	}
	
	public static Specification<LogLogin> keyword(HttpServletRequest request) {
		if(request.getParameter(KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(KY_PARAM))) {
			String keyword = String.format(LIKE_FORMAT, request.getParameter(KY_PARAM).trim().toLowerCase());
			return (logLogin, cq, cb) -> cb.or(
						cb.like(cb.lower(logLogin.get(EMAIL)), keyword),
						cb.like(cb.lower(logLogin.get(IP)), keyword),
						cb.like(cb.lower(logLogin.get(BROWSER)), keyword),
						cb.like(cb.lower(logLogin.get(DEVICE_TYPE)), keyword)
			);
		}else {
			return null;
		}
	}

	private static Instant getStartDate(HttpServletRequest request) {
		if(request.getParameter(START_DATE_PARAM) != null) {
			try {
				String startDate = String.format(START_DATE_FORMAT, request.getParameter(START_DATE_PARAM));
				LocalDateTime localDateTime = LocalDateTime.parse(startDate, FORMATTER);
				return localDateTime.toInstant(ZoneOffset.UTC);
			}catch (Exception e) {
				LOG.error(e.getLocalizedMessage());
			}
		}
		
		return Instant.now().truncatedTo(ChronoUnit.DAYS);
	}
	
	private static Instant getEndDate(HttpServletRequest request) {
		if(request.getParameter(END_DATE_PARAM) != null) {
			try {
				String endDate = String.format(END_DATE_FORMAT, request.getParameter(END_DATE_PARAM));
				LocalDateTime localDateTime = LocalDateTime.parse(endDate, FORMATTER);
				return localDateTime.toInstant(ZoneOffset.UTC);
			}catch (Exception e) {
				LOG.error(e.getLocalizedMessage());
			}
		}
		
		return Instant.now();
	}
	
}
