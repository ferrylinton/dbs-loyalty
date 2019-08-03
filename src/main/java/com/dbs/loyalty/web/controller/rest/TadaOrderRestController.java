package com.dbs.loyalty.web.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.TadaOrder;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.service.TadaService;
import com.dbs.loyalty.util.HeaderUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for TADA API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Api(tags = { SwaggerConstant.ORDER })
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api")
public class TadaOrderRestController {

	public static final String RESULT_FORMAT = "{\"message\":\"%s\"}";

	public static final String GET_TADA_ORDER_BY_ID = "GetTadaOrderById";
	
	public static final String CREATE_TADA_ORDER = "CreateTadaOrder";

	private final LogAuditCustomerService logAuditCustomerService;
	
	private final TadaService tadaService;
	
	@ApiOperation(
		nickname = GET_TADA_ORDER_BY_ID, 
		value = GET_TADA_ORDER_BY_ID, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_ORDER_BY_ID)
	@GetMapping("/tada-orders/{id}")
	public ResponseEntity<String> getOrderById(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
		return tadaService.getOrder(id);
	}

	@ApiOperation(
		nickname = CREATE_TADA_ORDER, 
		value = CREATE_TADA_ORDER, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@PostMapping("/tada-orders")
	public ResponseEntity<String> post(@RequestBody @Valid TadaOrder tadaOrder, HttpServletRequest request, HttpServletResponse response) {
		String url = UrlUtil.getFullUrl(request);
		try {
			ResponseEntity<String> result = tadaService.createOrder(tadaOrder, SecurityUtil.getLogged());
			logAuditCustomerService.saveJson(CREATE_TADA_ORDER, url, tadaOrder, null);
			return result;
		} catch (BadRequestException e) {
			log.error(e.getLocalizedMessage(), e);
			String result = String.format(RESULT_FORMAT, e.getLocalizedMessage());
			logAuditCustomerService.saveError(CREATE_TADA_ORDER, url, tadaOrder, e.getLocalizedMessage(), 400);
			return new ResponseEntity<>(result, HeaderUtil.getJsonHeaders(), HttpStatus.BAD_REQUEST);
		} catch (HttpClientErrorException e) {
			log.error(e.getLocalizedMessage(), e);
			logAuditCustomerService.saveError(CREATE_TADA_ORDER, url, tadaOrder, e.getLocalizedMessage(), e.getStatusCode().value());
			return new ResponseEntity<>(e.getResponseBodyAsString(), HeaderUtil.getJsonHeaders(), e.getStatusCode());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			String result = String.format(RESULT_FORMAT, e.getLocalizedMessage());
			logAuditCustomerService.saveError(CREATE_TADA_ORDER, url, tadaOrder, e.getLocalizedMessage(), 500);
			return new ResponseEntity<>(result, HeaderUtil.getJsonHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
