package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.HashMap;
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
import com.dbs.loyalty.web.controller.rest.AppointmentRestController;
import com.dbs.loyalty.web.controller.rest.CustomerActivateRestController;
import com.dbs.loyalty.web.controller.rest.CustomerRestController;
import com.dbs.loyalty.web.controller.rest.DepartureRestController;
import com.dbs.loyalty.web.controller.rest.FeedbackAnswerRestController;
import com.dbs.loyalty.web.controller.rest.PriviledgeOrderRestController;
import com.dbs.loyalty.web.controller.rest.TrxOrderRestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogApiService {
	
	private static final String JWT_AUTHENTICATE_NAME = "JWTAuthentiate";
	
	private static final String REQUEST_FORMAT = "{\"email\": \"%s\", \"password\" : \"********\"}";

	private final LogApiRepository logApiRepository;
	
	private final ObjectMapper objectMapper;
	
	private Map<String, String> logApiNames = null;
	
	public Page<LogApi> findAll(Map<String, String> params, Pageable pageable) {
		return logApiRepository.findAll(new LogApiSpec(params), pageable);
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
        
        save(logApi);
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
        
        save(logApi);
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
        
        save(logApi);
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

        save(logApi);
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
       
        save(logApi);
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
        
        save(logApi);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveError(String url, String binderName , Customer customer, Object request, Map<String, String> errors) {
		if(getLogApiName().containsKey(binderName)) {
			LogApi logApi = new LogApi();
	    	logApi.setName(getLogApiName().get(binderName));
	    	logApi.setUrl(url);
	    	logApi.setStatus(StatusConstant.FAIL);
	        logApi.setCreatedDate(Instant.now());
	        logApi.setError(toString(errors));
	        logApi.setRequest(toString(request));
	        
	        if(customer != null) {
	        	logApi.setCustomer(customer);
	        }
	        
	        save(logApi);
		}else {
			log.warn(String.format("LogApiService::getLogApiName()::Warn:: %s is not found", binderName));
		}
		
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveFileError(String url, Customer customer, String error) {
		LogApi logApi = new LogApi();
    	logApi.setName("File");
    	logApi.setUrl(url);
    	logApi.setStatus(StatusConstant.FAIL);
        logApi.setCreatedDate(Instant.now());
        logApi.setError(error);
        
        if(customer != null) {
        	logApi.setCustomer(customer);
        }
        
        save(logApi);
	}
	
	private Map<String, String> getLogApiName(){
		if(logApiNames == null) {
			logApiNames = new HashMap<>();
			logApiNames.put(AppointmentRestController.BINDER_NAME, AppointmentRestController.ADD_APPOINTMENT);
			logApiNames.put(DepartureRestController.BINDER_NAME, DepartureRestController.ADD_DEPARTURE);
			logApiNames.put(CustomerActivateRestController.BINDER_NAME, CustomerActivateRestController.ACTIVATE_CUSTOMER);
			logApiNames.put(FeedbackAnswerRestController.BINDER_NAME, FeedbackAnswerRestController.ADD_FEEDBACK_CUSTOMER);
			logApiNames.put(TrxOrderRestController.BINDER_NAME, TrxOrderRestController.ADD_TRX_ORDER);
			logApiNames.put(PriviledgeOrderRestController.BINDER_NAME, PriviledgeOrderRestController.ADD_PRIVILEDGE_ORDER);
			
			logApiNames.put(CustomerRestController.CUSTOMER_UPDATE_BINDER_NAME, CustomerRestController.UPDATE_CUSTOMER);
			logApiNames.put(CustomerRestController.CUSTOMER_PASSWORD_BINDER_NAME, CustomerRestController.CHANGE_PASSWORD);
		}
		
		return logApiNames;
	}
	
	private String toString(Object obj) {
		try {
			if(obj instanceof String) {
        		return (String) obj;
        	}else {
        		return objectMapper.writeValueAsString(obj);
        	}
		} catch (JsonProcessingException e) {
			return "LogApiService::toString()::Warn::" +  e.getLocalizedMessage();
		}
	}
	
	private void save(LogApi logApi) {
		try {
			logApiRepository.save(logApi);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
}
