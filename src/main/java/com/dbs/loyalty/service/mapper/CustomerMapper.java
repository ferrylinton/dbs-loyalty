package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.UrlService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.dto.CustomerViewDto;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDto, Customer> {
	
	Customer toEntity(CustomerUpdateDto customerUpdateDto);

	CustomerViewDto toViewDto(Customer customer, @Context UrlService urlService);
	
	@AfterMapping
    default void doAfterMapping(@MappingTarget CustomerViewDto customerViewDto, @Context UrlService urlService){
		customerViewDto.setImageUrl(urlService.getUrl("customers", "image", null));
    }

}
