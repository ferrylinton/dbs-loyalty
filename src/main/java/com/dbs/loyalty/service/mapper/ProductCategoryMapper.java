package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.ProductCategory;
import com.dbs.loyalty.service.dto.ProductCategoryDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ProductCategoryMapper{
 
	public abstract ProductCategoryDto toDto(ProductCategory productCategory);
	
}
