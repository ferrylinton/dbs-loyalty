package com.dbs.loyalty.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.constant.Constant;
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

	@Around("@annotation(enableLogAuditCustomer) && args(request, response)")
	public Object log(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer enableLogAuditCustomer, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		return logGet(joinPoint, enableLogAuditCustomer, request, response);
	}
	
	@Around("@annotation(enableLogAuditCustomer) && execution(* *(@org.springframework.web.bind.annotation.PathVariable (*), ..)) && args(.., request, response)")
	public Object logWithPathVariable(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer enableLogAuditCustomer, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		return logGet(joinPoint, enableLogAuditCustomer, request, response);
	}
	
	@Around("@annotation(enableLogAuditCustomer) && execution(* *(@org.springframework.web.bind.annotation.RequestParam (*), ..)) && args(.., request, response)")
	public Object logWithRequestParam(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer enableLogAuditCustomer, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		return logGet(joinPoint, enableLogAuditCustomer, request, response);
	}
	
	@Around("@annotation(enableLogAuditCustomer) && execution(* *(@org.springframework.web.bind.annotation.RequestBody (*), ..)) && args(obj, request, response)")
	public Object logWithBody(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer enableLogAuditCustomer, Object obj, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		String url = UrlUtil.getFullUrl(request);
		String operation = enableLogAuditCustomer.operation();
		
		try {
			Object result = joinPoint.proceed();
			logAuditCustomerService.saveJson(operation, url, obj, request.getAttribute(Constant.OLD_DATA));
			return result;
		} catch (Throwable throwable) {
			log.error(throwable.getLocalizedMessage(), throwable);
			logAuditCustomerService.saveError(operation, url, throwable.getLocalizedMessage(), response.getStatus());
			throw throwable;
		}
	}

	public Object logGet(ProceedingJoinPoint joinPoint, EnableLogAuditCustomer enableLogAuditCustomer, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		String url = UrlUtil.getFullUrl(request);
		String operation = enableLogAuditCustomer.operation();
		
		try {
			Object result = joinPoint.proceed();
			logAuditCustomerService.save(operation, url);
			return result;
		} catch (Throwable throwable) {
			log.error(throwable.getLocalizedMessage(), throwable);
			logAuditCustomerService.saveError(operation, url, throwable.getLocalizedMessage(), response.getStatus());
			throw throwable;
		}
	}
	
}
