package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
			operations.put("/api/authenticate", JWTRestController.AUTHENTICATE);
			operations.put("/api/addresses", AddressRestController.ADD_ADDRESS);
			operations.put("/api/arrivals", ArrivalRestController.ADD_ARRIVAL);
			operations.put("/api/departures", DepartureRestController.ADD_DEPARTURE);
			operations.put("/api/appointments", AppointmentRestController.ADD_APPOINTMENT);
			operations.put("/api/customers", CustomerRestController.UPDATE_CUSTOMER);
			operations.put("/api/customers/activate", CustomerActivateRestController.ACTIVATE_CUSTOMER);
			operations.put("/api/customers/image", CustomerImageRestController.UPDATE_CUSTOMER_IMAGE);
			operations.put("/api/customers/change-password", CustomerRestController.CHANGE_PASSWORD);
			operations.put("/api/feedback-answers", FeedbackAnswerRestController.ADD_FEEDBACK_CUSTOMER);
			operations.put("/api/trx-orders", TrxOrderRestController.ADD_TRX_ORDER);
			operations.put("/api/priviledge-orders", PriviledgeOrderRestController.CREATE_PRIVILEDGE_ORDER);
			operations.put(LovedOneRestController.ADD_BINDER_NAME, LovedOneRestController.ADD_LOVED_ONE);
			operations.put(LovedOneRestController.UPDATE_BINDER_NAME, LovedOneRestController.UPDATE_LOVED_ONE);
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
	
	public void save(String operation, String url, String id) {
		LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
		logAuditCustomer.setOperation(operation);
    	logAuditCustomer.setUrl(url);
    	logAuditCustomer.setRequestText(id);
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
	
	public void saveBadRequest(String url, String servletPath, Object requestJson, String responseJson) {
		if(getOperation().containsKey(servletPath)) {
			LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
	    	logAuditCustomer.setOperation(getOperation().get(servletPath));
	    	logAuditCustomer.setUrl(url);
	    	logAuditCustomer.setStatus(StatusConstant.FAIL);
	    	logAuditCustomer.setHttpStatus(400);
	        logAuditCustomer.setCreatedDate(Instant.now());
	        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());
	        logAuditCustomer.setRequestJson(toString(requestJson));
	        logAuditCustomer.setResponseJson(responseJson);
	        
	        save(logAuditCustomer);
		}else {
			log.warn(String.format("LogAuditCustomerService::getOperation() :: Warn :: %s is not found", servletPath));
		}
	}
	
	public void saveError(String operation, String url, Object requestJson, String error, Integer httpStatus) {
		LogAuditCustomer logAuditCustomer = new LogAuditCustomer();
		logAuditCustomer.setOperation(operation);
    	logAuditCustomer.setUrl(url);
    	logAuditCustomer.setHttpStatus(httpStatus);
    	logAuditCustomer.setStatus(StatusConstant.FAIL);
    	logAuditCustomer.setRequestJson(toString(requestJson));
    	logAuditCustomer.setResponseText(StringUtils.substring(error, 0, 250));
        logAuditCustomer.setCreatedDate(Instant.now());
        logAuditCustomer.setCustomer(SecurityUtil.getCustomer());

        save(logAuditCustomer);
	}
	
	public void saveError(String operation, String url, String error, Integer httpStatus) {
		saveError(operation, url, null, error, httpStatus);
	}
	
}
