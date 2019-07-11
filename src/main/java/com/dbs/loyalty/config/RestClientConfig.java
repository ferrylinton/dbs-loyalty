package com.dbs.loyalty.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Configuration
@EnableOAuth2Client
public class RestClientConfig {

	@Bean
	public OAuth2ProtectedResourceDetails resource() {
		 ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
		 details.setAccessTokenUri("https://staging-distribution-api.gift.id/oauth/token");
		 details.setClientId("54ud8nSSMxHughBhNe7JQWep9");
		 details.setClientSecret("OIBA5lyZXm8x3cyo2XC6NFjuD8tooMzbTQHp5MN1SV7JzuHxAU");
		 details.setGrantType("password");
		 details.setUsername("17527002");
		 details.setPassword("2571");
		 details.setScope(Arrays.asList("offline_access"));
		 return details;
	}
	
	@Bean
    public ClientHttpRequestFactory httpRequestFactory() {
		CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		return requestFactory;
    }
	
	@Bean
    public AccessTokenProvider accessTokenProvider() {
        ResourceOwnerPasswordAccessTokenProvider tokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
        tokenProvider.setRequestFactory(httpRequestFactory());
        return new AccessTokenProviderChain(
                  Arrays.<AccessTokenProvider> asList(tokenProvider)
                );
    }

	 
	@Bean("restTemplate")
	public OAuth2RestTemplate restTemplate() {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext());
		restTemplate.setRequestFactory(httpRequestFactory());
		restTemplate.setAccessTokenProvider(accessTokenProvider());
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			
        	@Override
			public void handleError(ClientHttpResponse response) throws IOException {
        		// skip error handle
			}
        	
		});
		
		return restTemplate;
	}
	
}
