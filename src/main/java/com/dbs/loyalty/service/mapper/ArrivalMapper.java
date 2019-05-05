package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Arrival;
import com.dbs.loyalty.service.dto.ArrivalDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ArrivalMapper{

	public abstract Arrival toEntity(ArrivalDto airportDto);
	
}
