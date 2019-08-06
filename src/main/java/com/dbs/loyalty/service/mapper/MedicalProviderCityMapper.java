package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.MedicalCity;
import com.dbs.loyalty.service.dto.MedicalProviderCityDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MedicalProviderCityMapper{
	
	public abstract MedicalCity toEntity(MedicalProviderCityDto medicalProviderCity);

	public abstract MedicalProviderCityDto toDto(MedicalCity medicalProviderCity);
	
	public abstract List<MedicalProviderCityDto> toDto(List<MedicalCity> medicalProviderCities);
	
}
