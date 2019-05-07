package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.Product;
import com.dbs.loyalty.service.dto.ProductDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ProductMapper{

	public abstract ProductDto toDto(Product troduct);

	@AfterMapping
    public void doAfterMapping(@MappingTarget ProductDto troductDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(PathConstant.API)
                .path(PathConstant.PRODUCTS)
                .path(PathConstant.SLASH + troductDto.getId())
                .path(PathConstant.IMAGE)
                .toUriString();
		
		String termUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(PathConstant.API)
                .path(PathConstant.PRODUCTS)
                .path(PathConstant.SLASH + troductDto.getId())
                .path(PathConstant.TERM)
                .toUriString();
		
		troductDto.setImageUrl(imageUrl);
		troductDto.setTermUrl(termUrl);
    }
	
}
