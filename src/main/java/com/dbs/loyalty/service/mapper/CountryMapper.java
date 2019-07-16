package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Country;
import com.dbs.loyalty.service.dto.CountryDto;

@Mapper(componentModel = "spring", uses = AirportMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CountryMapper {

	public abstract CountryDto toDto(Country country);
	
	public abstract List<CountryDto> toDto(List<Country> countries);
	
}
