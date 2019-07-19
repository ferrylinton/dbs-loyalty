package com.dbs.loyalty.web.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.TadaOrder;
import com.dbs.loyalty.service.TadaOrderService;
import com.dbs.loyalty.util.SecurityUtil;
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
public class TadaRestProxyController {
	
	private static final String RESULT_FORMAT = "{\"message\":\"%s\"}";
	
	private static final String TADA_GET = "Tada :: Get API";
	
	private static final String TADA_POST = "Tada :: Post API";

	private final ApplicationProperties applicationProperties;
	
	private final OAuth2RestTemplate oauth2RestTemplate;
	
	private final TadaOrderService tadaOrderService;
	
	private final ObjectMapper objectMapper;

	@ApiOperation(
			nickname=TADA_GET, 
			value=TADA_GET, 
			produces=SwaggerConstant.JSON, 
			authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=Resource.class)})
    @GetMapping("/**/**")
    public ResponseEntity<String> get(HttpServletRequest request) throws URISyntaxException, IOException{
		log.info("Call TADA API :: GET :: " + getURI(request));
		
		try {
			return oauth2RestTemplate.exchange(getURI(request), HttpMethod.GET, null, String.class);
		} catch (HttpClientErrorException e) {
			log.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			String result = String.format(RESULT_FORMAT, e.getLocalizedMessage());
			return new ResponseEntity<>(result, getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@ApiOperation(
			nickname=TADA_POST, 
			value=TADA_POST, 
			produces=SwaggerConstant.JSON, 
			authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=Resource.class)})
    @PostMapping("/**/**")
    public ResponseEntity<String> post(@RequestBody String body, HttpServletRequest request) throws URISyntaxException, IOException{
		log.info("Call TADA API :: POST :: " + getURI(request));
		
		try {
			HttpEntity<String> requestEntity = new HttpEntity<>(body, getHeaders()); 
			ResponseEntity<String> response = oauth2RestTemplate.exchange(getURI(request), HttpMethod.POST, requestEntity, String.class);
			
			if(request.getRequestURI().contains("/orders")) {
				saveTadaOrder(body, response);
			}

			return response;
		} catch (HttpClientErrorException e) {
			log.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			String result = String.format(RESULT_FORMAT, e.getLocalizedMessage());
			return new ResponseEntity<>(result, getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
	
	private URI getURI(HttpServletRequest request) throws URISyntaxException {
		String url = request.getRequestURI().replace(request.getContextPath() + Constant.API_TADA, Constant.EMPTY);
		
		if(request.getQueryString() != null) {
			return new URI(applicationProperties.getTada().getDomain() + url + Constant.QUESTION + request.getQueryString());
		}else {
			return new URI(applicationProperties.getTada().getDomain() + url);
		}
	}
	
	private void saveTadaOrder(String body, ResponseEntity<String> response) {
		try {
			TadaOrder requestTadaOrder = objectMapper.readValue(body, TadaOrder.class);
			if(requestTadaOrder != null) {
				Optional<TadaOrder> pendingTadaOrder = tadaOrderService.findByOrderReferenceAndCreatedBy(requestTadaOrder.getOrderReference(), SecurityUtil.getLogged());
			
				if(pendingTadaOrder.isPresent()) {
					pendingTadaOrder.get().setContent(response.getBody());
					pendingTadaOrder.get().setPending(false);
					pendingTadaOrder.get().setLastModifiedBy(SecurityUtil.getLogged());
					pendingTadaOrder.get().setLastModifiedDate(Instant.now());
					tadaOrderService.save(pendingTadaOrder.get());
				}
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
}
