package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.service.dto.LogLoginDto;

@Mapper(componentModel = "spring")
public interface LogLoginMapper extends EntityMapper<LogLoginDto, LogLogin> {

}
