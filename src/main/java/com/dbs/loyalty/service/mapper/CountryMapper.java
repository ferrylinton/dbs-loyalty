package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.dbs.loyalty.domain.Country;
import com.dbs.loyalty.service.dto.CountryDto;

@Mapper(componentModel = "spring", uses = AirportMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CountryMapper {
 
	@Named(value = "toDto")
	@Mapping(target="airports", ignore=true)
	public abstract CountryDto toDto(Country country);
	
	@Named(value = "toDtoWithAirports")
	public abstract CountryDto toDtoWithAirports(Country country);
	
	@IterableMapping(qualifiedByName = "toDto")
	public abstract List<CountryDto> toDto(List<Country> countries);
	
	@IterableMapping(qualifiedByName = "toDtoWithAirports")
	public abstract List<CountryDto> toDtoWithAirports(List<Country> countries);
	
}
