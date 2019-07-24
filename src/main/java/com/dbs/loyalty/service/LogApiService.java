package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.StatusConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.LogApi;
import com.dbs.loyalty.repository.LogApiRepository;
import com.dbs.loyalty.service.specification.LogApiSpec;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogApiService {
	
	private static final String JWT_AUTHENTICATE_NAME = "JWT Authentiate";
	
	private static final String REQUEST_FORMAT = "{\"email\": \"%s\", \"password\" : \"********\"}";

	private final LogApiRepository logApiRepository;
	
	private final ObjectMapper objectMapper;
	
	public Page<LogApi> findAll(Map<String, String> params, Pageable pageable) {
		return logApiRepository.findAll(new LogApiSpec(params), pageable);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(LogApi logApi) {
		logApiRepository.save(logApi);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(String url, LogAuditApi logAuditApi, Object response) {
		LogApi logApi = new LogApi();
    	logApi.setName(logAuditApi.name());
    	logApi.setUrl(url);
    	logApi.setStatus(StatusConstant.SUCCEED);
        logApi.setCreatedDate(Instant.now());
        logApi.setCustomer(SecurityUtil.getCustomer());
        
        if(logAuditApi.saveResponse()) {
        	logApi.setResponse(toString(response));
        }
        
        logApiRepository.save(logApi);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveError(String url, LogAuditApi logAuditApi, Throwable throwable) {
		LogApi logApi = new LogApi();
    	logApi.setName(logAuditApi.name());
    	logApi.setUrl(url);
    	logApi.setStatus(StatusConstant.FAIL);
        logApi.setCreatedDate(Instant.now());
        logApi.setCustomer(SecurityUtil.getCustomer());
        logApi.setError(throwable.getLocalizedMessage());
        
        logApiRepository.save(logApi);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(String url, LogAuditApi logAuditApi, Object request, Object response) {
		LogApi logApi = new LogApi();
    	logApi.setName(logAuditApi.name());
    	logApi.setUrl(url);
    	logApi.setStatus(StatusConstant.SUCCEED);
        logApi.setCreatedDate(Instant.now());
        logApi.setCustomer(SecurityUtil.getCustomer());
        
        if(logAuditApi.saveRequest()) {
        	logApi.setRequest(toString(request));
        }
        
        if(logAuditApi.saveResponse()) {
        	logApi.setResponse(toString(response));
        }
        
        logApiRepository.save(logApi);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveError(String url, LogAuditApi logAuditApi, Object request, Throwable throwable) {
		LogApi logApi = new LogApi();
    	logApi.setName(logAuditApi.name());
    	logApi.setUrl(url);
    	logApi.setStatus(StatusConstant.FAIL);
        logApi.setCreatedDate(Instant.now());
        logApi.setCustomer(SecurityUtil.getCustomer());
        logApi.setError(throwable.getLocalizedMessage());
        
        if(logAuditApi.saveRequest()) {
        	logApi.setRequest(toString(request));
        }

        logApiRepository.save(logApi);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(String url, Customer customer) {
		LogApi logApi = new LogApi();
    	logApi.setName(JWT_AUTHENTICATE_NAME);
    	logApi.setUrl(url);
    	logApi.setStatus(StatusConstant.SUCCEED);
        logApi.setCreatedDate(Instant.now());
        logApi.setCustomer(customer);
       
        logApiRepository.save(logApi);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveError(String url, Authentication authentication, Customer customer, String error) {
		LogApi logApi = new LogApi();
    	logApi.setName(JWT_AUTHENTICATE_NAME);
    	logApi.setUrl(url);
    	logApi.setStatus(StatusConstant.FAIL);
        logApi.setCreatedDate(Instant.now());
        logApi.setError(error);
        logApi.setRequest(String.format(REQUEST_FORMAT, authentication.getName()));
        
        if(customer != null) {
        	logApi.setCustomer(customer);
        }
        
        logApiRepository.save(logApi);
	}
	
	private String toString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return e.getLocalizedMessage();
		}
	}
}
