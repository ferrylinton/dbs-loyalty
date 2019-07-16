package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.Country;

@Component
public class CountryConverter implements Converter<Integer, Country> {

	@Override
	public Country convert(Integer id) {
		Country country = new Country();
		country.setId(id);
		return country;
	}

}
