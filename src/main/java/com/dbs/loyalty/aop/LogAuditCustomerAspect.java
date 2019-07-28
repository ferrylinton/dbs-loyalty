package com.dbs.loyalty.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.util.UrlUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LogAuditCustomerAspect {

	private final LogAuditCustomerService logAuditCustomerService;

	@Around("@annotation(logAudit)")
	public Object logGet(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer logAudit) throws Throwable {
		return logAudit(joinPoint, logAudit);
	}

	private Object logAudit(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer logAudit) throws Throwable {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		try {
			Object response = joinPoint.proceed();
			logAuditCustomerService.save(logAudit.operation(), UrlUtil.getFullUrl(httpServletRequest));
			return response;
		} catch (Throwable throwable) {
			log.error(throwable.getLocalizedMessage(), throwable);
			logAuditCustomerService.saveError(logAudit.operation(), UrlUtil.getFullUrl(httpServletRequest), throwable);
			throw throwable;
		}
	}
	
}
