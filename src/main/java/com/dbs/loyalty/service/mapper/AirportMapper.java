package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.ass.Airport;
import com.dbs.loyalty.service.dto.AirportDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AirportMapper{

	public abstract AirportDto toDto(Airport airport);
	
	public abstract List<AirportDto> toDto(List<Airport> airports);
	
}
