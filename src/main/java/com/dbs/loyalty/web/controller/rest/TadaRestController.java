package com.dbs.loyalty.web.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.domain.TadaItem;
import com.dbs.loyalty.domain.TadaOrder;
import com.dbs.loyalty.domain.TadaPayment;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.service.RewardService;
import com.dbs.loyalty.service.SettingService;
import com.dbs.loyalty.service.TadaOrderService;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@Api(tags = { SwaggerConstant.TADA })
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/tada")
public class TadaRestController {
	
	public static final String GET_TADA_ITEMS = "GetTadaItems";
	
	public static final String GET_TADA_CATEGORIES = "GetTadaCategories";
	
	public static final String GET_TADA_ORDER_BY_ID = "GetTadaOrderById";
	
	public static final String CREATE_TADA_ORDER = "CreateTadaOrder";
	
	private static final String RESULT_FORMAT = "{\"message\":\"%s\"}";
	
	private final ApplicationProperties applicationProperties;

	private final OAuth2RestTemplate oauth2RestTemplate;

	private final TadaOrderService tadaOrderService;
	
	private final SettingService settingService;
	
	private final RewardService rewardService;

	private final ObjectMapper objectMapper;
	
	private final LogAuditCustomerService logAuditCustomerService;
	
	@ApiOperation(
		nickname = GET_TADA_ITEMS, 
		value = GET_TADA_ITEMS, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_ITEMS)
	@GetMapping("/items")
	public ResponseEntity<String> getItems(HttpServletRequest request, HttpServletResponse response) {
		String url = applicationProperties.getTada().getItemsUrl();
		return exchangeGet(url, request);
	}
	
	@ApiOperation(
		nickname = GET_TADA_CATEGORIES, 
		value = GET_TADA_CATEGORIES, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_CATEGORIES)
	@GetMapping("/categories")
	public ResponseEntity<String> getCategories(HttpServletRequest request, HttpServletResponse response) {
		String url = applicationProperties.getTada().getCategoriesUrl();
		return exchangeGet(url, request);
	}
	
	@ApiOperation(
		nickname = GET_TADA_ORDER_BY_ID, 
		value = GET_TADA_ORDER_BY_ID, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@EnableLogAuditCustomer(operation=GET_TADA_ORDER_BY_ID)
	@GetMapping("/orders/{id}")
	public ResponseEntity<String> getTadaOrderById(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
		String url = applicationProperties.getTada().getOrdersByIdUrl().replace("{id}", id);
		return exchangeGet(url, request);
	}

	@ApiOperation(
		nickname = CREATE_TADA_ORDER, 
		value = CREATE_TADA_ORDER, 
		produces = "application/json", 
		authorizations = { @Authorization(value = "JWT") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	@PostMapping("/orders")
	public ResponseEntity<String> post(@RequestBody @Valid TadaOrder tadaOrder, HttpServletRequest request, HttpServletResponse response) {
		String url = UrlUtil.getFullUrl(request);
		try {
			ResponseEntity<String> result = saveTadaOrder(tadaOrder, SecurityUtil.getLogged(), request);
			logAuditCustomerService.saveJson(CREATE_TADA_ORDER, url, tadaOrder, null);
			return result;
		} catch (BadRequestException e) {
			log.error(e.getLocalizedMessage(), e);
			String result = String.format(RESULT_FORMAT, e.getLocalizedMessage());
			logAuditCustomerService.saveError(CREATE_TADA_ORDER, url, tadaOrder, e.getLocalizedMessage(), 400);
			return new ResponseEntity<>(result, getHeaders(), HttpStatus.BAD_REQUEST);
		} catch (HttpClientErrorException e) {
			log.error(e.getLocalizedMessage(), e);
			logAuditCustomerService.saveError(CREATE_TADA_ORDER, url, tadaOrder, e.getLocalizedMessage(), e.getStatusCode().value());
			return new ResponseEntity<>(e.getResponseBodyAsString(), getHeaders(), e.getStatusCode());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			String result = String.format(RESULT_FORMAT, e.getLocalizedMessage());
			logAuditCustomerService.saveError(CREATE_TADA_ORDER, url, tadaOrder, e.getLocalizedMessage(), 500);
			return new ResponseEntity<>(result, getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}	
	
	private URI getURI(String url, HttpServletRequest request) throws URISyntaxException {
		if (request.getQueryString() != null) {
			return new URI(url + Constant.QUESTION + request.getQueryString());
		} else {
			return new URI(url);
		}
	}
	
	private ResponseEntity<String> exchangeGet(String url, HttpServletRequest request) {
		try {
			return oauth2RestTemplate.exchange(getURI(url, request), HttpMethod.GET, null, String.class);
		} catch (HttpClientErrorException e) {
			log.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			String result = String.format(RESULT_FORMAT, e.getLocalizedMessage());
			return new ResponseEntity<>(result, getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private TadaOrder prepareTadaOrder(TadaOrder tadaOrder) {
		TadaPayment tadaPayment = new TadaPayment();
		tadaPayment.setPaymentType(applicationProperties.getTadaPayment().getType());
		tadaPayment.setPaymentWalletType(applicationProperties.getTadaPayment().getWalletType());
		tadaPayment.setCardNumber(applicationProperties.getTadaPayment().getCardNumber());
		tadaPayment.setCardPin(applicationProperties.getTadaPayment().getCardPin());
		tadaOrder.setTadaPayment(tadaPayment);
		
		for(TadaItem tadaItem : tadaOrder.getTadaItems()) {
			tadaItem.setPrice(null);
		}
		
		tadaOrder.setOrderReference(tadaOrderService.generate());
		return tadaOrder;
	}
	
	@Transactional
	private ResponseEntity<String> saveTadaOrder(TadaOrder tadaOrder, String email, HttpServletRequest request) throws BadRequestException, RestClientException, URISyntaxException, IOException {
		ResponseEntity<String> response = null;
		List<Reward> rewards = rewardService.findAllValid();
		int availablePoints = rewardService.getAvailablePoints(rewards);
		int orderPoints = getOrderPoints(tadaOrder);
		
		if(availablePoints > orderPoints) {
			response = exchange(prepareTadaOrder(tadaOrder), email, request);
			rewardService.deduct(email, rewards, orderPoints);
			return response;
		}else {
			throw new BadRequestException(MessageConstant.INSUFFICIENT_POINTS);
		}
	}
	
	private ResponseEntity<String> exchange(TadaOrder tadaOrder, String email, HttpServletRequest request) throws RestClientException, URISyntaxException, IOException, BadRequestException {
		HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(tadaOrder), getHeaders());
		String url = applicationProperties.getTada().getOrdersUrl();
		ResponseEntity<String> response = oauth2RestTemplate.exchange(getURI(url, request), HttpMethod.POST, requestEntity, String.class);
		
		tadaOrder.setResponse(response.getBody());
		tadaOrder.setLastModifiedBy(email);
		tadaOrder.setLastModifiedDate(Instant.now());
		tadaOrderService.save(tadaOrder);
		return response;
	}

	private Integer getOrderPoints(TadaOrder tadaOrder){
		Integer totalPrice = 0;
		
		for(TadaItem tadaItem : tadaOrder.getTadaItems()) {
			totalPrice += tadaItem.getPrice() * tadaItem.getQuantity();
		}
		
		int remainder = totalPrice % settingService.getPointToRupiah();
		int totalPoint = totalPrice / settingService.getPointToRupiah();
		
		return remainder == 0 ? totalPoint : totalPoint + 1;
	}
	
}
