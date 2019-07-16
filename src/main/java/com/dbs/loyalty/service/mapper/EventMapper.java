package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.service.dto.EventDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class EventMapper{

	public abstract EventDto toDto(Event event);

	@AfterMapping
    public void doAfterMapping(@MappingTarget EventDto eventDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(PathConstant.API)
                .path(PathConstant.EVENTS)
                .path(PathConstant.SLASH + eventDto.getId())
                .path(PathConstant.IMAGE)
                .toUriString();
		
		String materialUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(PathConstant.API)
                .path(PathConstant.EVENTS)
                .path(PathConstant.SLASH + eventDto.getId())
                .path(PathConstant.MATERIAL)
                .toUriString();
		
		eventDto.setImageUrl(imageUrl);
		eventDto.setMaterialUrl(materialUrl);
    }

}
