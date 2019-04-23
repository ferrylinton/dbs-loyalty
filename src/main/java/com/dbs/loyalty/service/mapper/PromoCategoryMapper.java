package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.dto.PromoCategoryDto;

@Mapper(componentModel = "spring")
public abstract class PromoCategoryMapper{
 
	public abstract PromoCategoryDto toDto(PromoCategory PromoCategory);
	
}
