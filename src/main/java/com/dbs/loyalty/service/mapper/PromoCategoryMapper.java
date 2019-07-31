package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.dto.PromoCategoryDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PromoCategoryMapper{
 
	public abstract PromoCategoryDto toDto(PromoCategory promoCategory);
	
	public abstract List<PromoCategoryDto> toDto(List<PromoCategory> promoCategories);
	
}
