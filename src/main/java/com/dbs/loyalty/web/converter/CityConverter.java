package com.dbs.loyalty.web.converter;

import java.time.Instant;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.repository.CityRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CityConverter implements Converter<String, City> {

	private final CityRepository cityRespository;
	
	@Override
	public City convert(String name) {
		Optional<City> city = cityRespository.findByNameIgnoreCase(name);
		
		if(city.isPresent()) {
			return city.get();
		}else {
			try {
				City newCity = new City();
				newCity.setId(Integer.parseInt("99" + RandomStringUtils.randomNumeric(10)));
				newCity.setName(name);
				newCity.setActive(true);
				newCity.setCreatedAt(Instant.now());
				return cityRespository.save(newCity);
			} catch (Exception e) {
				return null;
			}
			
		}
	}

}
