package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.service.dto.PromoCategoryDto;

@Component
public class PromoCategoryConverter implements Converter<String, PromoCategoryDto> {
	
	@Override
	public PromoCategoryDto convert(String content) {
		String[] str = content.split(Constant.COMMA);
		
		if(str.length == 2) {
			return new PromoCategoryDto(str[0], str[1]);
		}else {
			return null;
		}
	}

}
