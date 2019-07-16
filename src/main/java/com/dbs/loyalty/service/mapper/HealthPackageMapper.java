package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.med.HealthPackage;
import com.dbs.loyalty.service.dto.HealthPackageDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class HealthPackageMapper{
	
	public abstract HealthPackage toEntity(HealthPackageDto healthPackageDto);
	
	public abstract HealthPackageDto toDto(HealthPackage healthPackage);
	
	public abstract List<HealthPackageDto> toDto(List<HealthPackage> healthPackages);
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget HealthPackageDto healthPackageDto){
		String contentUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(PathConstant.API)
                .path(PathConstant.HEALTH_PACKAGES)
                .path(PathConstant.SLASH + healthPackageDto.getId())
                .path(PathConstant.CONTENT)
                .toUriString();
		
		healthPackageDto.setContentUrl(contentUrl);
    }
	
}
