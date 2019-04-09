package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.CustomerPromo;
import com.dbs.loyalty.service.dto.CustomerPromoDto;

@Mapper(componentModel = "spring", uses = { CustomerMapper.class, PromoMapper.class })
public abstract class CustomerPromoMapper extends EntityMapper<CustomerPromoDto, CustomerPromo> {

}
