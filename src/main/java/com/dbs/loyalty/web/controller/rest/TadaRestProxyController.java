package com.dbs.loyalty.web.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.SwaggerConstant;

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
	
	private static final String TADA_GET_CATEGORIES = "Tada :: Get Categories";
	
	private final ApplicationProperties applicationProperties;
	
	private final OAuth2RestTemplate oauth2RestTemplate;

	@ApiOperation(
			nickname=TADA_GET_CATEGORIES, 
			value=TADA_GET_CATEGORIES, 
			produces=SwaggerConstant.JSON, 
			authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=Resource.class)})
    @GetMapping("/**/**")
    public ResponseEntity<Resource> get(HttpServletRequest request) throws URISyntaxException, IOException{
		log.info("Call TADA API :: " + getURI(request));
		ResponseEntity<Resource> response = oauth2RestTemplate.exchange(getURI(request), HttpMethod.GET, null, Resource.class);
		InputStreamResource inputStreamResource = new InputStreamResource(response.getBody().getInputStream());
		return new ResponseEntity<>(inputStreamResource, response.getHeaders(), response.getStatusCode());
		
    }
	
	private URI getURI(HttpServletRequest request) throws URISyntaxException {
		String url = request.getRequestURI().replace(request.getContextPath() + Constant.API_TADA, Constant.EMPTY);
		return new URI(applicationProperties.getTada().getDomain() + url + Constant.QUESTION + request.getQueryString());
	}
}
