package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.domain.Country;
import com.dbs.loyalty.domain.Province;
import com.dbs.loyalty.repository.CityRepository;
import com.dbs.loyalty.repository.CountryRepository;
import com.dbs.loyalty.repository.ProvinceRepository;
import com.dbs.loyalty.service.specification.CountrySpec;
import com.dbs.loyalty.util.SortUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CountryService {

	private final ApplicationProperties applicationProperties;
	
	private final CountryRepository countryRepository;
	
	private final ProvinceRepository provinceRepository;
	
	private final CityRepository cityRepository;
	
	private final OAuth2RestTemplate oauth2RestTemplate;
	
	private final ObjectMapper objectMapper;
	
	public Optional<Country> findById(Integer id){
		return countryRepository.findById(id);
	}
	
	public List<Country> findAll(){
		return countryRepository.findAll(SortUtil.SORT_BY_NAME);
	}
	
	public List<Country> findWithAirports(){
		return countryRepository.findWithAirports();
	}
	
	public Page<Country> findAll(Map<String, String> params, Pageable pageable) {
		return countryRepository.findAll(new CountrySpec(params), pageable);
	}
	
	@Async
	public void sync(){
		try {
			ResponseEntity<Map<String, Object>> response = oauth2RestTemplate.exchange(applicationProperties.getTada().getCountriesUrl(), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>(){});
			List<Country> countries = objectMapper.readValue(objectMapper.writeValueAsString(response.getBody().get("data")), new TypeReference<List<Country>>(){});
			
			for(Country country : countries) {
				save(country);
			}
			
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	public void sync(Country country){
		try {
			ResponseEntity<Map<String, Object>> response = oauth2RestTemplate.exchange(applicationProperties.getTada().getCountriesUrl() + "/" + country.getId() + "/provinces", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>(){});
			List<Province> provinces = objectMapper.readValue(objectMapper.writeValueAsString(response.getBody().get("data")), new TypeReference<List<Province>>(){});
			
			for(Province province : provinces) {
				province.setCountry(country);
				save(country, province);
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	public void sync(Country country, Province province){
		try {
			ResponseEntity<Map<String, Object>> response = oauth2RestTemplate.exchange(applicationProperties.getTada().getCountriesUrl()+ "/" + country.getId() + "/provinces" + "/" + province.getId() + "/cities", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>(){});
			List<City> cities = objectMapper.readValue(objectMapper.writeValueAsString(response.getBody().get("data")), new TypeReference<List<City>>(){});
			
			for(City city : cities) {
				city.setProvince(province);
				save(city);
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	private void save(Country country) {
		try {
			sync(countryRepository.save(country));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	private void save(Country country, Province province) {
		try {
			sync(country, provinceRepository.save(province));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	private void save(City city) {
		try {
			cityRepository.save(city);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
}
