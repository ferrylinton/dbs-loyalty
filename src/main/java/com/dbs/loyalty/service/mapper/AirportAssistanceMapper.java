package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.service.dto.AirportAssistanceDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AirportAssistanceMapper{

	public abstract AirportAssistanceDto toDto(AirportAssistance airportAssistance);
	
}
