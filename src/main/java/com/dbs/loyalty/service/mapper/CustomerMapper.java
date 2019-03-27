package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.dto.CustomerDto;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDto, Customer> {
	
}
