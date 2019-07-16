package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.prm.PromoCategory;

@Component
public class PromoCategoryConverter implements Converter<String, PromoCategory> {
	
	@Override
	public PromoCategory convert(String id) {
		PromoCategory promoCategory = new PromoCategory();
		promoCategory.setId(id);
		return promoCategory;
	}

}
