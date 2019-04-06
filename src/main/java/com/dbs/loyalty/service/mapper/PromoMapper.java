package com.dbs.loyalty.service.mapper;

import java.io.IOException;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.service.UrlService;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.service.dto.PromoViewDto;
import com.dbs.loyalty.util.Base64Util;

@Mapper(componentModel = "spring", uses = {PromoCategoryMapper.class})
public abstract class PromoMapper extends EntityMapper<PromoDto, Promo> {

	@Mapping(source = "imageString", target = "imageBytes", qualifiedByName = "base64ToBytes")
	public abstract Promo toEntity(PromoDto promoDto);
	
	@Mapping(source = "imageBytes", target = "imageString", qualifiedByName = "bytesToBase64")
	public abstract PromoDto toDto(Promo promo);

	@Mapping(target = "imageUrl", ignore = true)
	@Mapping(target = "termAndConditionUrl", ignore = true)
	public abstract PromoViewDto toViewDto(Promo promo, @Context UrlService urlService);
	
	@Named("base64ToBytes")
	public byte[] base64ToBytes(String imageString) throws IOException {
		return Base64Util.getBytes(imageString);
    }
	
	@Named("bytesToBase64")
    public String bytesToBase64(byte[] imageBytes) throws IOException {
		return Base64Util.getString(imageBytes);
    }
	
	@AfterMapping
    public void doAfterMapping(@MappingTarget PromoViewDto promoViewDto, @Context UrlService urlService){
		promoViewDto.setImageUrl(urlService.getUrl("promos", "image", promoViewDto.getId()));
		promoViewDto.setTermAndConditionUrl(urlService.getUrl("promos", "term", promoViewDto.getId()));
    }
}
