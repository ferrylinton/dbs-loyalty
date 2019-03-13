package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.PromoCategory;

@Component
public class PromoCategoryConverter implements Converter<String, PromoCategory> {
	
	@Override
	public PromoCategory convert(String content) {
		String[] str = content.split(Constant.COMMA);
		
		PromoCategory promoCategory = new PromoCategory();
		
		if(str.length == 2) {
			promoCategory.setId(str[0]);
			promoCategory.setName(str[1]);
		}
		
		return promoCategory;
	}

}
