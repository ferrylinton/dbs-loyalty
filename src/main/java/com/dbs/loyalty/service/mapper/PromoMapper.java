package com.dbs.loyalty.service.mapper;

import java.io.IOException;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.util.Base64Util;

@Mapper(componentModel = "spring")
public interface PromoMapper extends EntityMapper<PromoDto, Promo> {

	@Mapping(source = "imageString", target = "imageBytes", qualifiedByName = "base64ToBytes")
	Promo toEntity(PromoDto promoDto);
	
	@Mapping(source = "imageBytes", target = "imageString", qualifiedByName = "bytesToBase64")
	PromoDto toDto(Promo promo);
	
	@Named("base64ToBytes")
    default byte[] base64ToBytes(String imageString) throws IOException {
		return Base64Util.getBytes(imageString);
    }
	
	@Named("bytesToBase64")
    default String bytesToBase64(byte[] imageBytes) throws IOException {
		return Base64Util.getString(imageBytes);
    }
	
}
