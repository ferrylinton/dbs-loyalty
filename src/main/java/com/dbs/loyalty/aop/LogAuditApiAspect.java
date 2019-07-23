package com.dbs.loyalty.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dbs.loyalty.service.LogApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LogAuditApiAspect {

	private final LogApiService logApiService;

	@Around("@annotation(logAuditApi) && execution(* *(@org.springframework.web.bind.annotation.RequestBody (*), ..)) && args(body)")
	public Object logPost(ProceedingJoinPoint joinPoint, LogAuditApi logAuditApi, Object body) throws Throwable {
		return logAuditApi(joinPoint, logAuditApi, body);
	}
	
	private Object logAuditApi(ProceedingJoinPoint joinPoint, LogAuditApi logAuditApi, Object body) throws Throwable {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		try {
			Object response = joinPoint.proceed();
			logApiService.save(getFullUrl(httpServletRequest), logAuditApi, body, response);
			return response;
		} catch (Throwable throwable) {
			log.error(throwable.getLocalizedMessage(), throwable);
			logApiService.saveError(getFullUrl(httpServletRequest), logAuditApi, body, throwable);
			throw throwable;
		}
	}
	
	private String getFullUrl(HttpServletRequest httpServletRequest) {
		StringBuilder requestURL = new StringBuilder(httpServletRequest.getRequestURL().toString());
		String queryString = httpServletRequest.getQueryString();
		
		if(queryString == null) {
			return requestURL.toString();
		}else {
			return requestURL.append("?").append(queryString).toString();
		}
		
	}
}
