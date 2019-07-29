package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.StatusConstant;
import com.dbs.loyalty.domain.LogAuditCustomer;
import com.dbs.loyalty.repository.LogAuditCustomerRepository;
import com.dbs.loyalty.service.specification.LogAuditCustomerSpec;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.controller.rest.AddressRestController;
import com.dbs.loyalty.web.controller.rest.AppointmentRestController;
import com.dbs.loyalty.web.controller.rest.ArrivalRestController;
import com.dbs.loyalty.web.controller.rest.CustomerActivateRestController;
import com.dbs.loyalty.web.controller.rest.CustomerRestController;
import com.dbs.loyalty.web.controller.rest.DepartureRestController;
import com.dbs.loyalty.web.controller.rest.FeedbackAnswerRestController;
import com.dbs.loyalty.web.controller.rest.JWTRestController;
import com.dbs.loyalty.web.controller.rest.LovedOneRestController;
import com.dbs.loyalty.web.controller.rest.PriviledgeOrderRestController;
import com.dbs.loyalty.web.controller.rest.TrxOrderRestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogAuditCustomerService {
	
	private static final String JSON = "json";

	private final LogAuditCustomerRepository logAuditCustomerRepository;
	
	private final ObjectMapper objectMapper;
	
	private Map<String, String> operations;
	
	public Page<LogAuditCustomer> findAll(Map<String, String> params, Pageable pageable) {
		return logAuditCustomerRepository.findAll(new LogAuditCustomerSpec(params), pageable);
	}
	
	public void save(String operation, String url) {
		save(operation, url, null, null);
	}

	public void save(String operation, String url, Object requestData) {
		save(operation, url, requestData, null);
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(String operation, String url, Object requestData, Object oldData) {
		LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
		logAuditCustomer.setOperation(operation);
    	logAuditCustomer.setUrl(url);
    	logAuditCustomer.setHttpStatus(200);
    	logAuditCustomer.setStatus(StatusConstant.SUCCEED);
    	logAuditCustomer.setContentType(JSON);
    	logAuditCustomer.setRequestData(toString(requestData));
    	logAuditCustomer.setOldData(toString(oldData));
        logAuditCustomer.setCreatedDate(Instant.now());
        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());

        save(logAuditCustomer);
	}

	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveError(String url, String servletPath, Object requestData, String responseData, Integer httpStatus) {
		if(getOperation().containsKey(servletPath)) {
			LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
	    	logAuditCustomer.setOperation(getOperation().get(servletPath));
	    	logAuditCustomer.setUrl(url);
	    	logAuditCustomer.setStatus(StatusConstant.FAIL);
	    	logAuditCustomer.setHttpStatus(httpStatus);
	    	logAuditCustomer.setContentType(JSON);
	        logAuditCustomer.setCreatedDate(Instant.now());
	        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());
	        logAuditCustomer.setRequestData(toString(requestData));
	        logAuditCustomer.setResponseData(responseData);
	        
	        save(logAuditCustomer);
		}else {
			log.warn(String.format("LogAuditCustomerService::getOperation() :: Warn :: %s is not found", servletPath));
		}
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveFileError(String url, String responseData) {
		LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
    	logAuditCustomer.setOperation("UploadFile");
    	logAuditCustomer.setUrl(url);
    	logAuditCustomer.setStatus(StatusConstant.FAIL);
    	logAuditCustomer.setContentType(JSON);
        logAuditCustomer.setCreatedDate(Instant.now());
        logAuditCustomer.setResponseData(responseData);
        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());
        
        save(logAuditCustomer);
	}

	private String toString(Object obj) {
		try {
			if(obj instanceof String) {
        		return (String) obj;
        	}else if(obj != null) {
        		return objectMapper.writeValueAsString(obj);
        	}else {
        		return null;
        	}
		} catch (JsonProcessingException e) {
			return "LogAuditCustomerService::toString()::Warn::" +  e.getLocalizedMessage();
		}
	}
	
	private void save(LogAuditCustomer logAuditCustomer) {
		try {
			logAuditCustomerRepository.save(logAuditCustomer);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	private Map<String, String> getOperation(){
		if(operations == null) {
			operations = new HashMap<>();
			operations.put("/api/authenticate", JWTRestController.AUTHENTICATE);
			operations.put("/api/addresses", AddressRestController.ADD_ADDRESS);
			operations.put("/api/arrivals", ArrivalRestController.ADD_ARRIVAL);
			operations.put("/api/departures", DepartureRestController.ADD_DEPARTURE);
			operations.put("/api/appointments", AppointmentRestController.ADD_APPOINTMENT);
			operations.put("/api/customers/activate", CustomerActivateRestController.ACTIVATE_CUSTOMER);
			operations.put(FeedbackAnswerRestController.BINDER_NAME, FeedbackAnswerRestController.ADD_FEEDBACK_CUSTOMER);
			operations.put(TrxOrderRestController.BINDER_NAME, TrxOrderRestController.ADD_TRX_ORDER);
			operations.put(PriviledgeOrderRestController.BINDER_NAME, PriviledgeOrderRestController.ADD_PRIVILEDGE_ORDER);
			operations.put(CustomerRestController.CUSTOMER_UPDATE_BINDER_NAME, CustomerRestController.UPDATE_CUSTOMER);
			operations.put(CustomerRestController.CUSTOMER_PASSWORD_BINDER_NAME, CustomerRestController.CHANGE_PASSWORD);
			operations.put(LovedOneRestController.ADD_BINDER_NAME, LovedOneRestController.ADD_LOVED_ONE);
			operations.put(LovedOneRestController.UPDATE_BINDER_NAME, LovedOneRestController.UPDATE_LOVED_ONE);
		}
		
		return operations;
	}
	
}
