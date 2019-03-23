package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.service.dto.AuthorityDto;

@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDto, Authority> {
	
}
