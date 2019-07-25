package com.dbs.loyalty.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dbs.loyalty.service.LogApiService;
import com.dbs.loyalty.util.UrlUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LogAuditApiAspect {

	private final LogApiService logApiService;

	@Around("@annotation(logAuditApi) && args()")
	public Object logGet(ProceedingJoinPoint joinPoint, LogAuditApi logAuditApi) throws Throwable {
		return logAuditApi(joinPoint, logAuditApi);
	}
	
	@Around("@annotation(logAuditApi) && execution(* *(@org.springframework.web.bind.annotation.PathVariable (*), ..)) && args(id, ..)")
	public Object logGetById(ProceedingJoinPoint joinPoint, LogAuditApi logAuditApi, String id) throws Throwable {
		return logAuditApi(joinPoint, logAuditApi, id);
	}
	
	@Around("@annotation(logAuditApi) && execution(* *(@org.springframework.web.bind.annotation.RequestBody (*), ..)) && args(body, ..)")
	public Object logPost(ProceedingJoinPoint joinPoint, LogAuditApi logAuditApi, Object body) throws Throwable {
		return logAuditApi(joinPoint, logAuditApi, body);
	}
	
	private Object logAuditApi(ProceedingJoinPoint joinPoint, LogAuditApi logAuditApi) throws Throwable {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		try {
			Object response = joinPoint.proceed();
			logApiService.save(UrlUtil.getFullUrl(httpServletRequest), logAuditApi, response);
			return response;
		} catch (Throwable throwable) {
			log.error(throwable.getLocalizedMessage(), throwable);
			logApiService.saveError(UrlUtil.getFullUrl(httpServletRequest), logAuditApi, throwable);
			throw throwable;
		}
	}
	
	private Object logAuditApi(ProceedingJoinPoint joinPoint, LogAuditApi logAuditApi, Object body) throws Throwable {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		try {
			Object response = joinPoint.proceed();
			logApiService.save(UrlUtil.getFullUrl(httpServletRequest), logAuditApi, body, response);
			return response;
		} catch (Throwable throwable) {
			log.error(throwable.getLocalizedMessage(), throwable);
			logApiService.saveError(UrlUtil.getFullUrl(httpServletRequest), logAuditApi, body, throwable);
			throw throwable;
		}
	}
	
}
