package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.UrlService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.dto.CustomerViewDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Mapper(componentModel = "spring", uses = {CustomerImageMapper.class, LovedOneMapper.class})
public abstract class CustomerMapper extends EntityMapper<CustomerDto, Customer> {
	
	@Autowired
	private UrlService urlService;

	public abstract CustomerDto toDto(Customer customer);
	
	@Named("toDtoWithImage")
	@Mapping(source="customerImage", target="image")
	public abstract CustomerDto toDtoWithImage(Customer customer);

	public abstract CustomerViewDto toViewDto(Customer customer);
	
	public abstract Customer toEntity(CustomerUpdateDto customerUpdateDto);

	@AfterMapping
    public void doAfterMapping(@MappingTarget CustomerViewDto customerViewDto){
		customerViewDto.setImageUrl(urlService.getUrl("customers", "image", null));
    }

}
