package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.dto.RoleDto;

@Mapper(componentModel = "spring", uses = {AuthorityMapper.class})
public interface RoleMapper{

	@Named("toDtoWithAuthorities")
	RoleDto toDtoWithAuthorities(Role role);
	
	@Mapping(target="authorities", ignore=true)
	Role toEntity(RoleDto roleDto);

	@Mapping(target="authorities", ignore=true)
	RoleDto toDto(Role role);

    List<RoleDto> toDto(List<Role> roles);
    
}
