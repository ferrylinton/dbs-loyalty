package com.dbs.loyalty.web.converter;

import java.util.Optional;

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
			return null;
		}
	}

}
