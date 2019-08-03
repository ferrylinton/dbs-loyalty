package com.dbs.loyalty.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.domain.TadaItem;
import com.dbs.loyalty.domain.TadaOrder;
import com.dbs.loyalty.domain.TadaPayment;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.util.HeaderUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TadaService {
	
	public static final String LOG_FORMAT = "CALL TADA API :: %s?%s";
	
	public static final String LOG_FORMAT_SIMPLE = "CALL TADA API :: %s";

	public static final String RESULT_FORMAT = "{\"message\":\"%s\"}";

	public final OAuth2RestTemplate oauth2RestTemplate;
	
	private final ObjectMapper objectMapper;
	
	private final ApplicationProperties applicationProperties;
	
	private final TadaOrderService tadaOrderService;
	
	private final RewardService rewardService;
	
	private final SettingService settingService;
	
	public ResponseEntity<String> getItems(String queryString){
		return exchangeGet(applicationProperties.getTada().getItemsUrl(), queryString);
	}
	
	public ResponseEntity<String> getCategories(String queryString){
		return exchangeGet(applicationProperties.getTada().getCategoriesUrl(), queryString);
	}
	
	public ResponseEntity<String> getOrder(String id){
		return exchangeGet(applicationProperties.getTada().getOrdersByIdUrl().replace("{id}", id), null);
	}
	
	public ResponseEntity<String> getCountries(){
		return exchangeGet(applicationProperties.getTada().getCountriesUrl(), null);
	}
	
	public ResponseEntity<String> getProvinces(Integer countryId){
		return exchangeGet(String.format(applicationProperties.getTada().getProvincesUrl(), applicationProperties.getTada().getCountriesUrl(), countryId), null);
	}
	
	public ResponseEntity<String> getCities(Integer countryId, Integer provinceId){
		return exchangeGet(String.format(applicationProperties.getTada().getCitiesUrl(), applicationProperties.getTada().getCountriesUrl(), countryId, provinceId), null);
	}
	
	@Transactional
	public ResponseEntity<String> createOrder(TadaOrder tadaOrder, String email) throws BadRequestException, RestClientException, URISyntaxException, IOException {
		ResponseEntity<String> response = null;
		List<Reward> rewards = rewardService.findAllValid();
		int availablePoints = rewardService.getAvailablePoints(rewards);
		int orderPoints = getOrderPoints(tadaOrder);
		
		if(availablePoints > orderPoints) {
			response = exchangePost(initOrder(tadaOrder), email);
			rewardService.deduct(email, rewards, orderPoints);
			return response;
		}else {
			throw new BadRequestException(MessageConstant.INSUFFICIENT_POINTS);
		}
	}

	private ResponseEntity<String> exchangeGet(String url, String queryString) {
		try {
			ResponseEntity<String> result = oauth2RestTemplate.exchange(getURI(url, queryString), HttpMethod.GET, null, String.class);
			return new ResponseEntity<>(result.getBody(), HeaderUtil.getJsonHeaders(), result.getStatusCode());
		} catch (HttpClientErrorException e) {
			log.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(e.getResponseBodyAsString(), HeaderUtil.getJsonHeaders(), e.getStatusCode());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			String result = String.format(RESULT_FORMAT, e.getLocalizedMessage());
			return new ResponseEntity<>(result, HeaderUtil.getJsonHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private ResponseEntity<String> exchangePost(TadaOrder tadaOrder, String email) throws RestClientException, URISyntaxException, IOException, BadRequestException {
		HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(tadaOrder), HeaderUtil.getJsonHeaders());
		String url = applicationProperties.getTada().getOrdersUrl();
		ResponseEntity<String> response = oauth2RestTemplate.exchange(getURI(url, null), HttpMethod.POST, requestEntity, String.class);
		
		tadaOrder.setResponse(response.getBody());
		tadaOrder.setLastModifiedBy(email);
		tadaOrder.setLastModifiedDate(Instant.now());
		tadaOrderService.save(tadaOrder);
		return response;
	}
	
	private TadaOrder initOrder(TadaOrder tadaOrder) {
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
	
	private Integer getOrderPoints(TadaOrder tadaOrder){
		Integer totalPrice = 0;
		
		for(TadaItem tadaItem : tadaOrder.getTadaItems()) {
			totalPrice += tadaItem.getPrice() * tadaItem.getQuantity();
		}
		
		int remainder = totalPrice % settingService.getPointToRupiah();
		int totalPoint = totalPrice / settingService.getPointToRupiah();
		
		return remainder == 0 ? totalPoint : totalPoint + 1;
	}
	
	private URI getURI(String url, String queryString) throws URISyntaxException {
		if (queryString != null) {
			log.info(String.format(LOG_FORMAT, url, queryString));
			return new URI(url + Constant.QUESTION + queryString);
		} else {
			log.info(String.format(LOG_FORMAT_SIMPLE, url));
			return new URI(url);
		}
	}
	
}
