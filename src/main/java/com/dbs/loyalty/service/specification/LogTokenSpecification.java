package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.CREATED_DATE;
import static com.dbs.loyalty.service.specification.Constant.EMPTY;
import static com.dbs.loyalty.service.specification.Constant.END_DATE_FORMAT;
import static com.dbs.loyalty.service.specification.Constant.END_DATE_PARAM;
import static com.dbs.loyalty.service.specification.Constant.FORMATTER;
import static com.dbs.loyalty.service.specification.Constant.ID;
import static com.dbs.loyalty.service.specification.Constant.KY_PARAM;
import static com.dbs.loyalty.service.specification.Constant.LIKE_FORMAT;
import static com.dbs.loyalty.service.specification.Constant.EMAIL;
import static com.dbs.loyalty.service.specification.Constant.START_DATE_FORMAT;
import static com.dbs.loyalty.service.specification.Constant.START_DATE_PARAM;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.domain.LogToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogTokenSpecification {

	public static Specification<LogToken> getSpec(HttpServletRequest request) {
		Specification<LogToken> spec = Specification.where(createdDate(request));
		
		if(request.getParameter(KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(KY_PARAM).trim())) {
			String keyword = String.format(LIKE_FORMAT, request.getParameter(KY_PARAM).trim().toLowerCase());
			return (logToken, cq, cb) -> cb.or(
						cb.like(cb.lower(logToken.get(ID)), keyword),
						cb.like(cb.lower(logToken.get(EMAIL)), keyword)
			);
		}
		
		return spec;
	}
	
	public static Specification<LogToken> all() {
		return (logToken, cq, cb) -> cb.notEqual(logToken.get(ID), EMPTY);
	}
	
	public static Specification<LogToken> createdDate(HttpServletRequest request) {
		if(
			request.getParameter(START_DATE_PARAM) != null &&
			request.getParameter(END_DATE_PARAM) != null &&
			!request.getParameter(START_DATE_PARAM).equals(EMPTY) &&
			!request.getParameter(END_DATE_PARAM).equals(EMPTY)
			) {
			
			Instant startDate = getStartDate(request);
			Instant endDate = getEndDate(request);
			
			return (logToken, cq, cb) -> cb.and(
				cb.greaterThanOrEqualTo(logToken.get(CREATED_DATE), startDate),
				cb.lessThanOrEqualTo(logToken.get(CREATED_DATE), endDate)
			);
		}else {
			return all();
		}		
	}

	private static Instant getStartDate(HttpServletRequest request) {
		if(request.getParameter(START_DATE_PARAM) != null) {
			try {
				String startDate = String.format(START_DATE_FORMAT, request.getParameter(START_DATE_PARAM));
				LocalDateTime localDateTime = LocalDateTime.parse(startDate, FORMATTER);
				return localDateTime.toInstant(ZoneOffset.UTC);
			}catch (Exception e) {
				log.error(e.getLocalizedMessage());
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
				log.error(e.getLocalizedMessage());
			}
		}
		
		return Instant.now();
	}
	
	private LogTokenSpecification() {
		// hide constructor
	}
	
}
