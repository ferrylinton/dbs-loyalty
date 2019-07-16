package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.med.Wellness;
import com.dbs.loyalty.service.dto.WellnessDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class WellnessMapper{

	public abstract WellnessDto toDto(Wellness wellness);
	
}
