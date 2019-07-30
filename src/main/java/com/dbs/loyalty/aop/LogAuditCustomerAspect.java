package com.dbs.loyalty.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.util.UrlUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LogAuditCustomerAspect {

	private static final String ERROR_FORMAT = "{\"error\" : \"%s\"}";
	
	private final LogAuditCustomerService logAuditCustomerService;

	@Around("@annotation(logAudit) && args(request, response)")
	public Object log(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer logAudit, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		return logAudit(joinPoint, logAudit, request, response);
	}
	
	@Around("@annotation(logAudit) && args(requestData, request, response)")
	public Object log(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer logAudit, Object requestData, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		return logAudit(joinPoint, logAudit, requestData, request, response);
	}

	private Object logAudit(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer logAudit, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			Object result = joinPoint.proceed();
			logAuditCustomerService.save(logAudit.operation(), UrlUtil.getFullUrl(request));
			return result;
		} catch (Throwable throwable) {
			log.error(throwable.getLocalizedMessage(), throwable);
			logAuditCustomerService.saveError(logAudit.operation(), UrlUtil.getFullUrl(request), String.format(ERROR_FORMAT, throwable.getLocalizedMessage()), response.getStatus());
			throw throwable;
		}
	}
	
	private Object logAudit(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer logAudit, Object requestData, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			Object result = joinPoint.proceed();
			logAuditCustomerService.save(logAudit.operation(), UrlUtil.getFullUrl(request), requestData);
			return result;
		} catch (Throwable throwable) {
			log.error(throwable.getLocalizedMessage(), throwable);
			logAuditCustomerService.saveError(logAudit.operation(), UrlUtil.getFullUrl(request), requestData, String.format(ERROR_FORMAT, throwable.getLocalizedMessage()), response.getStatus());
			throw throwable;
		}
	}
	
}
