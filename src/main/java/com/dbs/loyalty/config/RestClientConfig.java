package com.dbs.loyalty.config;

import java.util.Arrays;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableOAuth2Client
public class RestClientConfig {

	private final ApplicationProperties applicationProperties;
	
	@Bean
	public OAuth2ProtectedResourceDetails resource() {
		ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
		details.setAccessTokenUri(applicationProperties.getTada().getAccessTokenUri());
		details.setClientId(applicationProperties.getTada().getClientId());
		details.setClientSecret(applicationProperties.getTada().getClientSecret());
		details.setGrantType(applicationProperties.getTada().getGrantType());
		details.setUsername(applicationProperties.getTada().getUsername());
		details.setPassword(applicationProperties.getTada().getPassword());
		details.setScope(applicationProperties.getTada().getScope());
		return details;
	}
	
	@Bean
    public ClientHttpRequestFactory httpRequestFactory() {
		CloseableHttpClient httpClient = HttpClients
				.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
		
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(applicationProperties.getHttp().getConnectTimeout());
		requestFactory.setConnectionRequestTimeout(applicationProperties.getHttp().getConnectionRequestTimeout());
		requestFactory.setReadTimeout(applicationProperties.getHttp().getReadTimeout());
		requestFactory.setHttpClient(httpClient);
		return requestFactory;
    }
	
	@Bean
    public AccessTokenProvider accessTokenProvider() {
        ResourceOwnerPasswordAccessTokenProvider tokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
        tokenProvider.setRequestFactory(httpRequestFactory());
        return new AccessTokenProviderChain(Arrays.<AccessTokenProvider> asList(tokenProvider));
    }

	 
	@Bean("oauth2RestTemplate")
	public OAuth2RestTemplate oauth2RestTemplate() {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext());
		restTemplate.setRequestFactory(httpRequestFactory());
		restTemplate.setAccessTokenProvider(accessTokenProvider());
		return restTemplate;
	}

}
