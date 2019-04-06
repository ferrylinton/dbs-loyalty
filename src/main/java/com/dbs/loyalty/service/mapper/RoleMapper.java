package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.dto.RoleDto;

@Mapper(componentModel = "spring", uses = {AuthorityMapper.class})
public abstract class RoleMapper extends EntityMapper<RoleDto, Role> {

	@Mapping(target="authorities", ignore=true)
	public abstract RoleDto toDto(Role role);
    
	@Named("toDtoWithAuthorities")
	public abstract RoleDto toDtoWithAuthorities(Role role);
	
}
