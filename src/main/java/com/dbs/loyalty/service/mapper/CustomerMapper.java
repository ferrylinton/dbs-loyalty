package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.util.CustomerUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Mapper(componentModel = "spring", uses = {AddressMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CustomerMapper {

	public abstract CustomerDto toDto(Customer customer);
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget CustomerDto customerDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.API)
                .path(PathConstant.CUSTOMERS)
                .path(PathConstant.IMAGE)
                .toUriString();
		
		customerDto.setCustomerType(CustomerUtil.getCustomerType(customerDto.getCustomerType()));
		customerDto.setImageUrl(imageUrl);
    }

}
