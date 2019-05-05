package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Airport;
import com.dbs.loyalty.service.dto.AirportDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AirportMapper{

	public abstract AirportDto toDto(Airport airport);
	
}
