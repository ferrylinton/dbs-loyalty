package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Departure;
import com.dbs.loyalty.service.dto.DepartureDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class DepartureMapper{

	public abstract Departure toEntity(DepartureDto departureDto);
	
}
