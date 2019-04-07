package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.CustomerImage;
import com.dbs.loyalty.service.dto.CustomerImageDto;

@Mapper(componentModel = "spring")
public abstract class CustomerImageMapper extends EntityMapper<CustomerImageDto, CustomerImage>{
	    
}
