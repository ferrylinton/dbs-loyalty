package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.dto.CustomerFormDto;
import com.dbs.loyalty.service.dto.CustomerViewDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Mapper(componentModel = "spring", uses = {LovedOneMapper.class})
public abstract class CustomerMapper extends EntityMapper<CustomerFormDto, Customer> {

	public abstract CustomerViewDto toViewDto(Customer customer);
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget CustomerViewDto customerViewDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.CUSTOMERS)
                .path(PathConstant.IMAGE)
                .toUriString();
		
		customerViewDto.setImageUrl(imageUrl);
    }

}
