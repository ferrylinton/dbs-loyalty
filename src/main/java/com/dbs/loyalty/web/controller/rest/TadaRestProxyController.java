package com.dbs.loyalty.web.controller.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dbs.loyalty.config.constant.SwaggerConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for TADA API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Api(tags = { SwaggerConstant.TADA })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/tada")
public class TadaRestProxyController {
	
	private static final String TADA_GET_CATEGORIES = "Tada :: Get Categories";
	
	private final OAuth2RestTemplate restTemplate;

	@ApiOperation(
			nickname=TADA_GET_CATEGORIES, 
			value=TADA_GET_CATEGORIES, 
			produces=SwaggerConstant.JSON, 
			authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=Resource.class)})
    @GetMapping("/**/**")
    public ResponseEntity<Resource> get(HttpServletRequest request) throws URISyntaxException, IOException{
//		CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                .build();
//		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//		requestFactory.setHttpClient(httpClient);
//		RestTemplate restTemplate = new RestTemplate(requestFactory);
//		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
//			
//        	@Override
//			public void handleError(ClientHttpResponse response) throws IOException {
//        		// skip error handle
//			}
//        	
//		});

		
		
//		URI tadaHost = new URI  ("https", null, "https://staging-distribution-api.gift.id", 80, request.getRequestURI(), request.getQueryString(), null);
//		ResponseEntity<Resource> response = restTemplate.exchange(tadaHost, HttpMethod.GET, null, Resource.class);
//		InputStreamResource inputStreamResource = new InputStreamResource(response.getBody().getInputStream());
//		return new ResponseEntity<>(inputStreamResource, response.getHeaders(), response.getStatusCode());
//		
		String url = request.getRequestURI().replace(request.getContextPath() + "/api/tada", "");
		url = "https://staging-distribution-api.gift.id" + url + "?" + request.getQueryString();
		
		System.out.println("-------------- tada url : " + url);
		
//		InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(url.getBytes(Charset.forName("UTF-8"))));
//		return new ResponseEntity<Resource>(inputStreamResource, HttpStatus.OK);
		

		ResponseEntity<Resource> response = restTemplate.exchange(new URI(url), HttpMethod.GET, null, Resource.class);
		InputStreamResource inputStreamResource = new InputStreamResource(response.getBody().getInputStream());
		return new ResponseEntity<>(inputStreamResource, response.getHeaders(), response.getStatusCode());
		
    }
	
}
