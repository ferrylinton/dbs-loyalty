package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.dto.PromoCategoryDto;

@Mapper(componentModel = "spring")
public interface PromoCategoryMapper extends EntityMapper<PromoCategoryDto, PromoCategory> {

}
