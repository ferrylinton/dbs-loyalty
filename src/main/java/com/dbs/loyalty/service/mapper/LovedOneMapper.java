package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.LovedOne;
import com.dbs.loyalty.service.dto.LovedOneAddDto;
import com.dbs.loyalty.service.dto.LovedOneDto;
import com.dbs.loyalty.service.dto.LovedOneUpdateDto;

@Mapper(componentModel = "spring")
public abstract class LovedOneMapper extends EntityMapper<LovedOneDto, LovedOne> {

	public abstract LovedOne toEntity(LovedOneAddDto dto);
	
	public abstract LovedOne toEntity(LovedOneUpdateDto dto);
	
}
