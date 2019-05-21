package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.MedicalProvider;
import com.dbs.loyalty.service.dto.MedicalProviderDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MedicalProviderMapper{
	
	public abstract MedicalProvider toEntity(MedicalProviderDto medicalProviderDto);
	
	public abstract MedicalProviderDto toDto(MedicalProvider medicalProvider);
	
	public abstract List<MedicalProviderDto> toDto(List<MedicalProvider> medicalProviders);
	
}
