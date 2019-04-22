package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.service.dto.CarouselDto;
import com.dbs.loyalty.service.dto.PromoFormDto;
import com.dbs.loyalty.service.dto.PromoViewDto;

@Mapper(componentModel = "spring", uses = {PromoCategoryMapper.class})
public abstract class PromoMapper extends EntityMapper<PromoFormDto, Promo> {

	public abstract PromoViewDto toViewDto(Promo promo);
	
	public abstract CarouselDto toCarouselDto(Promo promo);
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget PromoViewDto promoViewDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.PROMOS)
                .path("/" + promoViewDto.getId())
                .path(PathConstant.IMAGE)
                .toUriString();
		
		String termUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.PROMOS)
                .path("/" + promoViewDto.getId())
                .path(PathConstant.TERM)
                .toUriString();
		
		promoViewDto.setImageUrl(imageUrl);
		promoViewDto.setTermUrl(termUrl);
    }
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget CarouselDto carouselDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.PROMOS)
                .path("/" + carouselDto.getId())
                .path(PathConstant.IMAGE)
                .toUriString();
		
		carouselDto.setImageUrl(imageUrl);
    }
	
}
