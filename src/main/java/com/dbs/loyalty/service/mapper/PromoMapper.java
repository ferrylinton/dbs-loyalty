package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.service.dto.CarouselDto;
import com.dbs.loyalty.service.dto.PromoDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PromoMapper{

	public abstract PromoDto toDto(Promo promo);
	
	public abstract CarouselDto toCarouselDto(Promo promo);
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget PromoDto promoDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(PathConstant.API)
                .path(PathConstant.PROMOS)
                .path(PathConstant.SLASH + promoDto.getId())
                .path(PathConstant.IMAGE)
                .toUriString();
		
		String termUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(PathConstant.API)
                .path(PathConstant.PROMOS)
                .path(PathConstant.SLASH + promoDto.getId())
                .path(PathConstant.TERM)
                .toUriString();
		
		promoDto.setImageUrl(imageUrl);
		promoDto.setTermUrl(termUrl);
    }
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget CarouselDto carouselDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(PathConstant.API)
                .path(PathConstant.PROMOS)
                .path(PathConstant.SLASH + carouselDto.getId())
                .path(PathConstant.IMAGE)
                .toUriString();
		
		carouselDto.setImageUrl(imageUrl);
    }
	
}
