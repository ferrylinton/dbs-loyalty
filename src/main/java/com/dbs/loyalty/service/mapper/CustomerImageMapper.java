package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.CustomerImage;
import com.dbs.loyalty.service.dto.ImageDto;

@Mapper(componentModel = "spring")
public abstract class CustomerImageMapper extends EntityMapper<ImageDto, CustomerImage>{
	    
}
