package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.domain.Arrival;
import com.dbs.loyalty.service.dto.ArrivalDto;

@Mapper(componentModel = "spring", uses = AirportMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ArrivalMapper{

	@Mapping(source="flightDate", target="flightDate", dateFormat=DateConstant.UTC)
	@Mapping(source="pickupTime", target="pickupTime", dateFormat=DateConstant.UTC)
	public abstract Arrival toEntity(ArrivalDto dto);
	
	@Mapping(source="flightDate", target="flightDate", dateFormat=DateConstant.UTC)
	@Mapping(source="pickupTime", target="pickupTime", dateFormat=DateConstant.UTC)
	public abstract ArrivalDto toDto(Arrival entity);
	
}
