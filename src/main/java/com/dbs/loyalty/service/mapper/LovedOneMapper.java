package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.LovedOne;
import com.dbs.loyalty.service.dto.LovedOneAddDto;
import com.dbs.loyalty.service.dto.LovedOneDto;
import com.dbs.loyalty.service.dto.LovedOneUpdateDto;

@Mapper(componentModel = "spring", uses = CustomerMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class LovedOneMapper{

	public abstract LovedOneDto toDto(LovedOne lovedOne);
	
	public abstract List<LovedOneDto> toDto(List<LovedOne> lovedOnes);
	
	public abstract LovedOne toEntity(LovedOneAddDto dto);
	
	public abstract LovedOne toEntity(LovedOneUpdateDto dto);
	
}
