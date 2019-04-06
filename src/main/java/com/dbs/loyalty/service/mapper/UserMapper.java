package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.service.dto.UserDto;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends EntityMapper<UserDto, User> {
	
}
