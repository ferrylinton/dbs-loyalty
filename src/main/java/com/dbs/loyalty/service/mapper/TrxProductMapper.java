package com.dbs.loyalty.service.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StrSubstitutor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.CachingConstant;
import com.dbs.loyalty.domain.TrxProduct;
import com.dbs.loyalty.service.SettingService;
import com.dbs.loyalty.service.dto.TrxProductDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TrxProductMapper{
	
	private static final String IMAGE_URL = "/api/trx-products/%s/image";

	public abstract TrxProductDto toDto(TrxProduct entity, @Context SettingService settingService);
	
	public abstract List<TrxProductDto> toDto(List<TrxProduct> entities, @Context SettingService settingService);

	@AfterMapping
    public void doAfterMapping(@MappingTarget TrxProductDto dto, @Context SettingService settingService){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(String.format(IMAGE_URL, dto.getId()))
                .toUriString();

		if(dto.getDescription() != null) {
			Map<String, Object> valueMap = new HashMap<>();
			valueMap.put(CachingConstant.CURRENCY, settingService.getCurrency());
			valueMap.put(CachingConstant.POINT_TO_RUPIAH, settingService.getPointToRupiah());
			dto.setDescription(StrSubstitutor.replace(dto.getDescription(), valueMap));
		}
		
		dto.setImageUrl(imageUrl);
    }
	
}
