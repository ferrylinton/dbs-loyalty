package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.service.dto.PromoViewDto;

@Mapper(componentModel = "spring", uses = {PromoCategoryMapper.class})
public abstract class PromoMapper extends EntityMapper<PromoDto, Promo> {

	public abstract PromoViewDto toViewDto(Promo promo);
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget PromoViewDto promoViewDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.PROMOS)
                .path(PathConstant.IMAGE)
                .toUriString();
		
		String termUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.PROMOS)
                .path(PathConstant.TERM)
                .toUriString();
		
		promoViewDto.setImageUrl(imageUrl);
		promoViewDto.setTermAndConditionUrl(termUrl);
    }
}
