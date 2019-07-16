package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.service.dto.RewardDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class RewardMapper {

	public abstract RewardDto toDto(Reward reward);
	
}
