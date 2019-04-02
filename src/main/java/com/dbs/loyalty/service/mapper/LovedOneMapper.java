package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.LovedOne;
import com.dbs.loyalty.service.dto.LovedOneAddDto;
import com.dbs.loyalty.service.dto.LovedOneDto;
import com.dbs.loyalty.service.dto.LovedOneUpdateDto;

@Mapper(componentModel = "spring")
public interface LovedOneMapper extends EntityMapper<LovedOneDto, LovedOne> {

	LovedOne toEntity(LovedOneAddDto dto);
	
	LovedOne toEntity(LovedOneUpdateDto dto);
}
