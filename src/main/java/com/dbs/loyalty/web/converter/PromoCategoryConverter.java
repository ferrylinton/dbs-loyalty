package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.PromoCategory;

@Component
public class PromoCategoryConverter implements Converter<String, PromoCategory> {
	
	@Override
	public PromoCategory convert(String content) {
		String[] str = content.split(Constant.COMMA);
		
		if(str.length == 2) {
			PromoCategory promoCategory = new PromoCategory();
			promoCategory.setId(str[0]);
			promoCategory.setName(str[1]);
			
			return promoCategory;
		}else {
			return null;
		}
	}

}
