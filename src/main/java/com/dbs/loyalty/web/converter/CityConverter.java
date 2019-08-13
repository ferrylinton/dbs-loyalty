package com.dbs.loyalty.web.converter;

import java.time.Instant;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.repository.CityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CityConverter implements Converter<String, City> {
	
	private static final String PREFIX = "99";

	private final CityRepository cityRespository;
	
	@Override
	public City convert(String name) {
		if(!StringUtils.isBlank(name)) {
			Optional<City> city = cityRespository.findByNameIgnoreCase(name);
			
			if(city.isPresent()) {
				return city.get();
			}else{
				try {
					City newCity = new City();
					newCity.setId(Integer.parseInt(PREFIX + RandomStringUtils.randomNumeric(6)));
					newCity.setName(name);
					newCity.setActive(true);
					newCity.setCreatedAt(Instant.now());
					return cityRespository.save(newCity);
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				}
				
			}
		}
		
		return null;
	}

}
