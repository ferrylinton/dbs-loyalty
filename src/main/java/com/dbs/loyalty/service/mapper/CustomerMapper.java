package com.dbs.loyalty.service.mapper;

import java.io.IOException;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.UrlService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.util.Base64Util;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDto, Customer> {
	
	@Mapping(source = "imageString", target = "imageBytes", qualifiedByName = "base64ToBytes")
	Customer toEntity(CustomerDto customerDto);
	
	@Mapping(source = "imageBytes", target = "imageString", qualifiedByName = "bytesToBase64")
	@Mapping(target = "imageUrl", ignore = true)
	CustomerDto toDto(Customer customer, @Context UrlService urlService);

	@Named("base64ToBytes")
    default byte[] base64ToBytes(String imageString) throws IOException {
		return Base64Util.getBytes(imageString);
    }
	
	@Named("bytesToBase64")
	default String bytesToBase64(byte[] imageBytes) throws IOException {
		return Base64Util.getString(imageBytes);
	}
	
	@AfterMapping
    default void doAfterMapping(@MappingTarget CustomerDto customerDto, @Context UrlService urlService){
		customerDto.setImageUrl(urlService.getUrl("customers", "image"));
    }

}
