package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
import com.dbs.loyalty.web.controller.rest.CustomerImageRestController;
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
	
	private final LogAuditCustomerRepository logAuditCustomerRepository;
	
	private final ObjectMapper objectMapper;
	
	private Map<String, String> operations;
	
	public Optional<LogAuditCustomer> findWithCustomerById(String id) {
		return logAuditCustomerRepository.findWithCustomerById(id);
	}
	
	public Page<LogAuditCustomer> findAll(Map<String, String> params, Pageable pageable) {
		return logAuditCustomerRepository.findAll(new LogAuditCustomerSpec(params), pageable);
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
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(LogAuditCustomer logAuditCustomer) {
		try {
			logAuditCustomerRepository.save(logAuditCustomer);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	private Map<String, String> getOperation(){
		if(operations == null) {
			operations = new HashMap<>();
			operations.put("POST:/api/authenticate", JWTRestController.AUTHENTICATE);
			operations.put("POST:/api/addresses", AddressRestController.ADD_ADDRESS);
			operations.put("POST:/api/arrivals", ArrivalRestController.ADD_ARRIVAL);
			operations.put("POST:/api/departures", DepartureRestController.ADD_DEPARTURE);
			operations.put("POST:/api/appointments", AppointmentRestController.ADD_APPOINTMENT);
			operations.put("PUT:/api/customers", CustomerRestController.UPDATE_CUSTOMER);
			operations.put("POST:/api/customers/activate", CustomerActivateRestController.ACTIVATE_CUSTOMER);
			operations.put("PUT:/api/customers/image", CustomerImageRestController.UPDATE_CUSTOMER_IMAGE);
			operations.put("POST:/api/customers/change-password", CustomerRestController.CHANGE_PASSWORD);
			operations.put("POST:/api/feedback-answers", FeedbackAnswerRestController.ADD_FEEDBACK_CUSTOMER);
			operations.put("POST:/api/trx-orders", TrxOrderRestController.CREATE_TRX_ORDER);
			operations.put("POST:/api/priviledge-orders", PriviledgeOrderRestController.CREATE_PRIVILEDGE_ORDER);
			operations.put("POST:/api/loved-ones", LovedOneRestController.ADD_LOVED_ONE);
			operations.put("PUT:/api/loved-ones", LovedOneRestController.UPDATE_LOVED_ONE);
		}
		
		return operations;
	}
	
	public void save(String operation, String url) {
		LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
		logAuditCustomer.setOperation(operation);
    	logAuditCustomer.setUrl(url);
    	logAuditCustomer.setHttpStatus(200);
    	logAuditCustomer.setStatus(StatusConstant.SUCCEED);
        logAuditCustomer.setCreatedDate(Instant.now());
        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());

        save(logAuditCustomer);
	}
	
	public void saveJson(String operation, String url, Object requestJson, Object oldJson) {
		LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
		logAuditCustomer.setOperation(operation);
    	logAuditCustomer.setUrl(url);
    	logAuditCustomer.setHttpStatus(200);
    	logAuditCustomer.setStatus(StatusConstant.SUCCEED);
    	logAuditCustomer.setRequestJson(toString(requestJson));
    	logAuditCustomer.setOldJson(toString(oldJson));
        logAuditCustomer.setCreatedDate(Instant.now());
        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());

        save(logAuditCustomer);
	}
	
	public void saveFile(String operation, String url, byte[] requestFile, byte[] oldFile) {
		LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
		logAuditCustomer.setOperation(operation);
    	logAuditCustomer.setUrl(url);
    	logAuditCustomer.setHttpStatus(200);
    	logAuditCustomer.setStatus(StatusConstant.SUCCEED);
    	logAuditCustomer.setRequestFile(requestFile);
    	logAuditCustomer.setOldFile(oldFile);
        logAuditCustomer.setCreatedDate(Instant.now());
        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());

        save(logAuditCustomer);
	}
	
	public void saveBadRequest(String url, String operationKey, Object requestJson, Object responseJson) {
		if(getOperation().containsKey(operationKey)) {
			LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
	    	logAuditCustomer.setOperation(getOperation().get(operationKey));
	    	logAuditCustomer.setUrl(url);
	    	logAuditCustomer.setStatus(StatusConstant.FAIL);
	    	logAuditCustomer.setHttpStatus(400);
	        logAuditCustomer.setCreatedDate(Instant.now());
	        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());
	        logAuditCustomer.setRequestJson(toString(requestJson));
	        logAuditCustomer.setResponseJson(toString(responseJson));
	        
	        save(logAuditCustomer);
		}else {
			log.warn(String.format("LogAuditCustomerService::getOperation() :: Warn :: %s is not found", operationKey));
		}
	}
	
	public void saveError(String operation, String url, Object requestJson, String error, Integer httpStatus) {
		LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
		logAuditCustomer.setOperation(operation);
    	logAuditCustomer.setUrl(url);
    	logAuditCustomer.setHttpStatus(httpStatus);
    	logAuditCustomer.setStatus(StatusConstant.FAIL);
    	logAuditCustomer.setRequestJson(toString(requestJson));
    	
    	if(error instanceof String) {
    		logAuditCustomer.setResponseText(StringUtils.substring(error, 0, 250));
    	}else {
    		logAuditCustomer.setResponseJson(toString(error));
    	}
    	
        logAuditCustomer.setCreatedDate(Instant.now());
        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());

        save(logAuditCustomer);
	}
	
	public void saveError(String operation, String url, String error, Integer httpStatus) {
		saveError(operation, url, null, error, httpStatus);
	}
	
}
