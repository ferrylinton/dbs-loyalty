package com.dbs.loyalty.service.mapper;

import java.util.List;
import java.util.Optional;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dbs.loyalty.domain.Address;
import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.service.CityService;
import com.dbs.loyalty.service.dto.AddressDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AddressMapper{

	@Mapping(source = "city.name", target = "city")
	public abstract AddressDto toDto(Address address);
	
	@Mapping(target="city", ignore=true)
	public abstract Address toEntity(AddressDto dto, @Context CityService cityService);
	
	public abstract List<AddressDto> toDto(List<Address> addresses);
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget Address entity, AddressDto dto, @Context CityService cityService){
		Optional<City> city = cityService.findByNameIgnoreCase(dto.getCity());
		
		if(city.isPresent()) {
			entity.setCity(city.get());
		}
    }
	
}
