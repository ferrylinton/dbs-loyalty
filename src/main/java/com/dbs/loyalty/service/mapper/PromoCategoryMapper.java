package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.prm.PromoCategory;
import com.dbs.loyalty.service.dto.PromoCategoryDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PromoCategoryMapper{
 
	public abstract PromoCategoryDto toDto(PromoCategory promoCategory);
	
}
