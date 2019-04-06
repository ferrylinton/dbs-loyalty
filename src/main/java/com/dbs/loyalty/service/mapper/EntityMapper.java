package com.dbs.loyalty.service.mapper;

import java.util.List;

public abstract class EntityMapper <D, E> {

	public abstract E toEntity(D dto);

	public abstract D toDto(E entity);

	public abstract List<E> toEntity(List<D> dtoList);

	public abstract List<D> toDto(List<E> entityList);
    
}