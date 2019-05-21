package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.service.dto.TravelAssistanceDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TravelAssistanceMapper{

	public abstract TravelAssistanceDto toDto(AirportAssistance travelAssistance);
	
}
