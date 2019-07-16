package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.cst.LovedOne;
import com.dbs.loyalty.service.dto.LovedOneAddDto;
import com.dbs.loyalty.service.dto.LovedOneDto;
import com.dbs.loyalty.service.dto.LovedOneUpdateDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class LovedOneMapper{

	public abstract LovedOneDto toDto(LovedOne lovedOne);
	
	public abstract LovedOne toEntity(LovedOneAddDto dto);
	
	public abstract LovedOne toEntity(LovedOneUpdateDto dto);
	
}
