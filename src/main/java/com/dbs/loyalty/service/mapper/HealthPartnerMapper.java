package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.MedicalProvider;
import com.dbs.loyalty.service.dto.HealthPartnerDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class HealthPartnerMapper{

	public abstract HealthPartnerDto toDto(MedicalProvider healthPartner);
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget HealthPartnerDto healthPartnerDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.API)
                .path(PathConstant.HEALTH_PARTNERS)
                .path(PathConstant.SLASH + healthPartnerDto.getId())
                .path(PathConstant.IMAGE)
                .toUriString();
		
		healthPartnerDto.setImageUrl(imageUrl);
    }
	
}
