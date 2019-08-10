package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.repository.PromoCategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PromoCategoryConverter implements Converter<String, PromoCategory> {
	
	private final PromoCategoryRepository promoCategoryRepository;
	
	@Override
	public PromoCategory convert(String id) {
		return promoCategoryRepository.getOne(id);
	}

}
