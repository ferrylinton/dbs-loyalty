package com.dbs.loyalty.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dbs.loyalty.config.constant.PathConstant;
import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.service.dto.EventFormDto;
import com.dbs.loyalty.service.dto.EventViewDto;

@Mapper(componentModel = "spring")
public abstract class EventMapper extends EntityMapper<EventFormDto, Event> {

	public abstract EventViewDto toViewDto(Event event);

	@AfterMapping
    public void doAfterMapping(@MappingTarget EventViewDto eventViewDto){
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.EVENTS)
                .path(PathConstant.SLASH + eventViewDto.getId())
                .path(PathConstant.IMAGE)
                .toUriString();
		
		String materialUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PathConstant.EVENTS)
                .path(PathConstant.SLASH + eventViewDto.getId())
                .path(PathConstant.MATERIAL)
                .toUriString();
		
		eventViewDto.setImageUrl(imageUrl);
		eventViewDto.setMaterialUrl(materialUrl);
    }

}
