package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.PromoCategory;

@Component
public class PromoCategoryConverter implements Converter<String, PromoCategory> {
	
	@Override
	public PromoCategory convert(String content) {
		if(content  != null && content.length() > 22) {
			PromoCategory promoCategory = new PromoCategory();
			promoCategory.setId(content.substring(0,22));
			promoCategory.setName(content.replace(promoCategory.getId(), Constant.EMPTY));
			return promoCategory;
		}else {
			return null;
		}
	}

}
