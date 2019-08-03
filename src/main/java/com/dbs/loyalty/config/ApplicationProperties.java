package com.dbs.loyalty.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
 
	private Security security;

	private Async async;
	
	private Scheduler scheduler;
	
	private Mail mail;
	
	private File file;
	
	private Tada tada;
	
	private TadaPayment tadaPayment;
	
	private Http http;

	@Getter
	@Setter
	public static class Security{
		
		private int maxAttempt = 3;

		private String secret;
		
		private long tokenValidity = 1440;
		
		private long tokenValidityForRememberMe = 43200;
		
		private long verificationTokenValidity = 15;
		
	}
	
	@Getter
	@Setter
	public static class Async{
		
		private int corePoolSize = 5;
		
	    private int maxPoolSize = 10;
	    
	    private int queueCapacity = 1000;
	    
	}
	
	@Getter
	@Setter
	public static class Scheduler{
		
		private String customerCron = "0 0 1 * * ?";
		
	    private String filePath = "/home/ferry/loyalty/customers.csv";
	    
	}
	
	@Getter
	@Setter
	public static class Mail{
		
		private String from = "test@dbs.com";
		
	}
	
	@Getter
	@Setter
	public static class File{
		
		private long imageMaxSize = 5;
		
		private long pdfMaxSize = 5;
		
		private long csvMaxSize = 20;
		
		private String pdfContentType = "application/pdf";
		
		private List<String> imageContentTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg");
		
	}
	
	@Getter
	@Setter
	public static class Tada{
		
		private String accessTokenUri = "https://staging-distribution-api.gift.id/oauth/token";
		
		private String clientId = "54ud8nSSMxHughBhNe7JQWep9";
		
		private String clientSecret = "OIBA5lyZXm8x3cyo2XC6NFjuD8tooMzbTQHp5MN1SV7JzuHxAU";
		
		private String grantType = "password";
		
		private String username = "17527002";
		
		private String password = "2571";
		
		private List<String> scope = Arrays.asList("offline_access");

		private String categoriesUrl = "https://staging-distribution-api.gift.id/v2/categories";
		
		private String itemsUrl = "https://staging-distribution-api.gift.id/v2/items";
		
		private String ordersUrl = "https://staging-distribution-api.gift.id/v2/orders";
		
		private String ordersByIdUrl = "https://staging-distribution-api.gift.id/v2/orders/{id}";
		
		private String countriesUrl = "https://staging-distribution-api.gift.id/v2/countries";
		
		private String provincesUrl = "%s/%d/provinces";
		
		private String citiesUrl = "%s/%d/provinces/%d/cities";

	}
	
	@Getter
	@Setter
	public static class TadaPayment{
		
		private String type = "tada";
		
		private String walletType = "balance";
		
		private String cardNumber = "3671287772281932";
		
		private String cardPin = "286812";
		
	}
	
	@Getter
	@Setter
	public static class Http{
		
		private int connectionRequestTimeout;
		
		private int connectTimeout;
		
		private int readTimeout;
		
	}
	
}
