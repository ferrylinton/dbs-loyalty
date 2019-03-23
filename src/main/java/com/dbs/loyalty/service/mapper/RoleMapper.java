package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.dto.RoleDto;

@Mapper(componentModel = "spring", uses = {AuthorityMapper.class})
public interface RoleMapper extends EntityMapper<RoleDto, Role> {
	
}
